package com.github.rzilyn.multifilepicker.interfaces;

import android.graphics.SurfaceTexture;
import android.view.SurfaceHolder;

/**
 * Created by Rizal Fahmi on 22-Jan-18.
 */

public interface CameraSupport {
    CameraSupport open(int cameraId, int width, int height);
    int getOrientation(int orientation);
    void setPreview(SurfaceTexture surface);
    void surfaceChanged(SurfaceTexture texture, int width, int height);
    void destroy();
    void pause();
}
