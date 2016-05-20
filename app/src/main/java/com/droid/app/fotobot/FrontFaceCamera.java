package com.droid.app.fotobot;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by voran on 5/16/16.
 */
public class FrontFaceCamera {

    private Context context;

    private boolean hasCamera;

    private Camera camera;
    private int cameraId;

    private SurfaceHolder holder=null;

    public FrontFaceCamera(Context c, SurfaceHolder sh){

        context = c.getApplicationContext();
        holder = sh;

        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            cameraId = getFrontCameraId();

            if(cameraId != -1){
                hasCamera = true;
            }else{
                hasCamera = false;
            }
        }else{
            hasCamera = false;
        }
    }

    public boolean hasCamera(){
        return hasCamera;
    }

    public void getCameraInstance(){
        camera = null;

        if(hasCamera){
            try{
                camera = Camera.open(cameraId);
            }
            catch(Exception e){

            }
        }
    }

    public String getCameraParameters(){
        return camera.getParameters().flatten();
    }

    public void takePicture(){
        if(hasCamera){

            try {
                camera.setPreviewDisplay(holder);
            //    camera.stopPreview();
                camera.startPreview();
        //        SendMessage(".");
                Log.d("DEBUG", "Preview started");
            } catch (Exception e) {
                e.printStackTrace();
//                SendMessage("...");
                Log.d("DEBUG", "Problem with starting of preview");
            }

            camera.takePicture(null,null,mPicture);
        }

    }

    public void releaseCamera(){
        if(camera != null){
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private int getFrontCameraId(){
        int camId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for(int i = 0;i < numberOfCameras;i++){
            Camera.getCameraInfo(i,ci);
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                camId = i;
            }
        }

        return camId;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback(){
        @Override
        public void onPictureTaken(byte[] data, Camera camera){
            File pictureFile = getOutputMediaFile();

            if(pictureFile == null){
                Log.d("TEST", "Error creating media file, check storage permissions");
                return;
            }

            try{
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            }catch(FileNotFoundException e){
                Log.d("TEST","File not found: "+e.getMessage());
            } catch (IOException e){
                Log.d("TEST","Error accessing file: "+e.getMessage());
            }
        }
    };

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"FFC");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediaFile;
        mediaFile = new File("/storage/external_SD/"+"IMG_"+timeStamp+".jpg");

        return mediaFile;
    }

}
