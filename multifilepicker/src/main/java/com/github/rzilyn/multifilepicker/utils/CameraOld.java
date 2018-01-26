package com.github.rzilyn.multifilepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.util.Pair;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;

import com.github.rzilyn.multifilepicker.interfaces.CameraSupport;
import com.github.rzilyn.multifilepicker.view_component.AutoFitTextureView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Rizal Fahmi on 22-Jan-18.
 */

@SuppressWarnings("deprecation")
public class CameraOld implements CameraSupport {

    private Camera mCamera;
    private Context mContext;
    private final String TAG = getClass().getSimpleName();
    private AutoFitTextureView mTextureView;

    private int mCameraId;

    public CameraOld(Context context,AutoFitTextureView mTextureView) {
        this.mTextureView = mTextureView;
        this.mContext = context;
    }

    @Override
    public CameraSupport open(int cameraId, int width, int height) {
        this.mCameraId = cameraId;
        this.mCamera = Camera.open(cameraId);
        configureTransform();
        return this;
    }

    @Override
    public int getOrientation(int cameraId) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId,cameraInfo);
        return cameraInfo.orientation;
    }

    @Override
    public void setPreview(SurfaceTexture texture) {
        try {
//            mCamera.setDisplayOrientation(90);
            mCamera.getParameters();
            mCamera.setPreviewTexture(texture);
            mCamera.startPreview();
//            updateTextureMatrix(320,320);
        }
        catch (IOException e){
            e.printStackTrace();
            Log.d(TAG,"Error configuring camera : "+e.getMessage());
        }
    }

    public void configureTransform(){
        Activity activity = (Activity) mContext;
        if(activity == null)
            return;
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId,info);
        int degree = 0;
        switch (rotation){
            case Surface.ROTATION_0:degree = 0;break;
            case Surface.ROTATION_90:degree = 90;break;
            case Surface.ROTATION_180:degree = 180;break;
            case Surface.ROTATION_270:degree = 270;break;
        }

        int result;
        if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = (info.orientation + degree) % 360;
            result = (360-result) % 360;
        }
        else {
            result = (info.orientation - degree + 360 ) % 360;
        }
        Log.d(TAG,"Orientation : "+rotation);
        mCamera.setDisplayOrientation(result);

    }

    @Override
    public void surfaceChanged(SurfaceTexture texture, int width, int height) {
        configureTransform();
    }


    @Override
    public void destroy() {
        if(mCamera!=null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void pause() {
        if(mCamera!=null) {
            mCamera.stopPreview();
            mCamera.release();
        }
    }

    private void updateTextureMatrix(int width, int height)
    {
        int orgPreviewWidth;
        int orgPreviewHeight;

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

        Pair<Integer, Integer> size = getMaxSize(parameters.getSupportedPreviewSizes());
        parameters.setPreviewSize(size.first, size.second);

        orgPreviewWidth = size.first;
        orgPreviewHeight = size.second;

        mCamera.setParameters(parameters);

        int previewWidth = orgPreviewWidth;
        int previewHeight = orgPreviewHeight;


        float ratioSurface = (float) width / height;
        float ratioPreview = (float) previewWidth / previewHeight;

        float scaleX;
        float scaleY;

        if (ratioSurface > ratioPreview)
        {
            scaleX = (float) height / previewHeight;
            scaleY = 1;
        }
        else
        {
            scaleX = 1;
            scaleY = (float) width / previewWidth;
        }

        Matrix matrix = new Matrix();

        matrix.setScale(scaleX, scaleY);
        mTextureView.setTransform(matrix);

        float scaledWidth = width * scaleX;
        float scaledHeight = height * scaleY;

        float dx = (width - scaledWidth) / 2;
        float dy = (height - scaledHeight) / 2;
        mTextureView.setTranslationX(dx);
        mTextureView.setTranslationY(dy);
    }

    private Pair<Integer, Integer> getMaxSize(List<Camera.Size> list)
    {
        int width = 0;
        int height = 0;

        for (Camera.Size size : list) {
            if (size.width * size.height > width * height)
            {
                width = size.width;
                height = size.height;
            }
        }

        return new Pair<Integer, Integer>(width, height);
    }
}
