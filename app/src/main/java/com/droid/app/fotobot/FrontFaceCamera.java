package com.droid.app.fotobot;


import android.hardware.Camera;
import android.util.Log;

/**
 * Created by voran on 5/16/16.
 */
public class FrontFaceCamera {

    public void FrontFaceCamera(){
        Camera cam = getFFC();
    }

    private Camera getFFC(){

        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e("FFC", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }


        return cam;

    }

}
