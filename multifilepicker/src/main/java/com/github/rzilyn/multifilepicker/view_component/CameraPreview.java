package com.github.rzilyn.multifilepicker.view_component;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.github.rzilyn.multifilepicker.interfaces.CameraSupport;

/**
 * Created by Rizal Fahmi on 22-Jan-18.
 */

public class CameraPreview implements TextureView.SurfaceTextureListener {

    private CameraSupport mCamera;

    public CameraPreview(CameraSupport mCamera) {
        this.mCamera = mCamera;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mCamera.open(0,i,i1);
        mCamera.setPreview(surfaceTexture);
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        mCamera.surfaceChanged(surfaceTexture,i,i1);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        mCamera.destroy();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    private void updateTextureMatrix(int width, int height){


    }
}
