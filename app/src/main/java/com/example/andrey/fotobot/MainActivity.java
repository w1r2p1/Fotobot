package com.example.andrey.fotobot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import android.util.Size;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private final static String FILE_DIR = "/MySampleFolder/";

    int n;
    FotoBot fb;
    String log;

    //a variable to store a reference to the Image View at the main.xml file
    private ImageView iv_image;

    //a variable to store a reference to the Surface View at the main.xml file
    private SurfaceView sv;

    //a bitmap to display the captured image
    private Bitmap bmp;

    //Camera variables
    //a surface holder
    private SurfaceHolder sHolder;
    //a variable to control the camera
    private Camera mCamera;
    //the camera parameters
    private Camera.Parameters parameters;
    final String LOG_TAG = "Logs";

    private UnexpectedTerminationHelper mUnexpectedTerminationHelper = new UnexpectedTerminationHelper();

    private class UnexpectedTerminationHelper {
        private Thread mThread;
        private Thread.UncaughtExceptionHandler mOldUncaughtExceptionHandler = null;
        private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) { // gets called on the same (main) thread

                releaseCamera();


                if (mOldUncaughtExceptionHandler != null) {
                    // it displays the "force close" dialog
                    mOldUncaughtExceptionHandler.uncaughtException(thread, ex);
                }
            }
        };

        void init() {
            mThread = Thread.currentThread();
            mOldUncaughtExceptionHandler = mThread.getUncaughtExceptionHandler();
            mThread.setUncaughtExceptionHandler(mUncaughtExceptionHandler);
        }

        void fini() {
            mThread.setUncaughtExceptionHandler(mOldUncaughtExceptionHandler);
            mOldUncaughtExceptionHandler = null;
            mThread = null;
        }
    }

    /**
     * Печатает в консоль общее число памяти, доступная память, занятую память
     *
     * @return используемая память
     */
    public long getUsedMemorySize() {
        final FotoBot fb = (FotoBot) getApplicationContext();
        final String LOG_USED_MEMORY = "UsedMem";

        long freeSize = 0L;
        long totalSize = 0L;
        long usedSize = -1L;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            totalSize = info.totalMemory();
            usedSize = totalSize - freeSize;
            //    fb.SendMessage("MEMORY (TOTAL/FREE/USED MB)" + totalSize + "/" + freeSize + "/" + usedSize );
            Log.d(LOG_USED_MEMORY, "***** totalSize, freeSize, usedSize  " + totalSize + ";" + freeSize + ";" + usedSize);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return usedSize;

    }

    /**
     * постобработка фото
     */
    Camera.PictureCallback mCall = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            final FotoBot fb = (FotoBot) getApplicationContext();
//            fb.SendMessage("Camera.PictureCallback is started");
            fb.LoadData();

            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyWakelockTag");


            wakeLock.acquire();


            Log.d(LOG_TAG, "***** mCall started: " + getUsedMemorySize());

            //decode the data obtained by the camera into a Bitmap
            // bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            //set the iv_image
            //iv_image.setImageBitmap(bmp);


            //decode the data obtained by the camera into a Bitmap
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;


            // options.inJustDecodeBounds = true;
            //       bmp = BitmapFactory.decodeByteArray(data, 0, data.length,options);


            // Calculate inSampleSize

            if (fb.Photo_Post_Processing_Method.contains("Hardware")) {

                options.inSampleSize = 8;

                switch (fb.Image_Scale) {
                    case "1/16":
                        options.inSampleSize = 16;
                        break;
                    case "1/8":
                        options.inSampleSize = 8;
                        break;
                    case "1/4":
                        options.inSampleSize = 4;
                        break;
                    case "1/2":
                        options.inSampleSize = 2;
                        break;
                    default:
                        options.inSampleSize = 1;
                        break;
                }

            }

            //fb.SendMessage("options.inSampleSize = " + options.inSampleSize);


            options.inPreferredConfig = Bitmap.Config.RGB_565;

            Log.d(LOG_TAG, "***** Options are defined: " + getUsedMemorySize());

            //fb.SendMessage("BitmapFactory.decodeByteArray is started");

            bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            //fb.SendMessage("BitmapFactory.decodeByteArray finished");

            Log.d(LOG_TAG, "***** BitmapFactory.decodeByteArray(data,0,data.length,options) done " + getUsedMemorySize());

            // fb.fbpause(h,5);

            //  try {
            //      TimeUnit.SECONDS.sleep(5);
            //  } catch (InterruptedException e) {
            //      e.printStackTrace();
            //  }


            // String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath();


            // Log.d(LOG_TAG, "fullPath: " + fullPath);
            // try {
            //     File dir = new File(fullPath);
            //     if (!dir.exists()) {
            //         dir.mkdirs();
            //     }

            FileOutputStream fOut = null;
            File file = new File(getApplicationContext().getFilesDir(), "fotobot.jpg");

            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                fOut = new FileOutputStream(file);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //     Bitmap bmp_m = bmp.createScaledBitmap(bmp, 320,
            //           240, false);

            //  fb.fbpause(h,5);

            //   try {
            //       TimeUnit.SECONDS.sleep(5);
            //   } catch (InterruptedException e) {
            //       e.printStackTrace();
            //   }

            Log.d(LOG_TAG, "***** fotobot.jpg is created) done " + getUsedMemorySize());

// 100 means no compression, the lower you go, the stronger the compression

            // fb.SendMessage("Bitmap.CompressFormat started");

            bmp.compress(Bitmap.CompressFormat.JPEG, fb.JPEG_Compression, fOut);

            //  fb.SendMessage("Bitmap.CompressFormat finished");

            Log.d(LOG_TAG, "***** bmp.compress(Bitmap.CompressFormat.JPEG, 50, fOut); " + getUsedMemorySize());

            // fb.fbpause(h,3);

            // try {
            //     TimeUnit.SECONDS.sleep(3);
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }

            try {
                fOut.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                fOut.getFD().sync();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //    try {
            //    fOut.getFD().sync();
            //    } catch (Exception e) {
            //        e.printStackTrace();
            //    }


            try {
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            //  try {
            //      TimeUnit.SECONDS.sleep(1);
            //  } catch (InterruptedException e) {
            //       e.printStackTrace();
            //  }

            bmp.recycle();

            bmp = null;

            Log.d(LOG_TAG, "***** fOut.flush();  + getUsedMemorySize()");

        }

    };

    /**
     * Печатает сообщения на экран телефона, нужен для того чтобы получать данные из потока в котором работает FotoBot
     */
    Handler.Callback hc = new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            final FotoBot fb = (FotoBot) getApplicationContext();
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyWakelockTag");

            wakeLock.acquire();

            DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String reportDate = dateformat.format(today);

            String message = (String) msg.obj; //Extract the string from the Message
            log = reportDate + ": " + message + "\n" + log;

            tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Log_Font_Size);
            tvInfo.setTypeface(Typeface.MONOSPACE);
            tvInfo.setTextColor(Color.rgb(150, 150, 150));

            tvInfo.setText(log);

            //final FotoBot fb = (FotoBot) getApplicationContext();
            Log.d(LOG_TAG, "Handler.Callback(): fb.getstatus()" + fb.getstatus());
            n = msg.what;
            if (msg.what == STATUS_STOPPED) btnStart.setText("Play");

            if (fb.getstatus() == 3) {

               // mCamera.stopPreview();
             //   mCamera.release();
               // mCamera = null;

                btnStart = (Button) findViewById(R.id.play);
                btnStop = (Button) findViewById(R.id.stop);
                //     Log.d(LOG_TAG, "Handler.Callback() if: fb.getstatus()" + fb.getstatus());
                btnStart.setText("Пуск");
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnConfig.setEnabled(true);
                btnStart.postInvalidate();
                btnStop.postInvalidate();
            }

            return false;
        }
    };

    final int STATUS_STARTED = 111;
    final int STATUS_WORKING = 222;
    final int STATUS_STOPPED = 333;
    boolean STOP_FOTOBOT = false;


    Button btnStart;
    Button btnStop;
    Button btnConfig;
    Handler h = null;
    TextView tvInfo;
    TextView text;
    Intent intent;
    String str1 = "Fotobot str to file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");


        super.onCreate(savedInstanceState);
        wakeLock.acquire();

        //    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FotoBot fb = (FotoBot) getApplicationContext();

        btnStart = (Button) findViewById(R.id.play);
        btnConfig = (Button) findViewById(R.id.config);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        text = (TextView) findViewById(R.id.textView);

        //get the Image View at the main.xml file
        iv_image = (ImageView) findViewById(R.id.imageView);

        //get the Surface View at the main.xml file
        sv = (SurfaceView) findViewById(R.id.surfaceView);

        //Get a surface
        sHolder = sv.getHolder();

        //add the callback interface methods defined below as the Surface View callbacks
        sHolder.addCallback(this);

        //tells Android that this surface will have its data constantly replaced
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        fb.sHolder = sHolder;


        intent = new Intent(MainActivity.this, Status.class);
        log = "\n\n\n\n\nФотобот приветствует Вас!";

        tvInfo.setText(log);

        h = new Handler(hc);

        /**
         * получили указатель на обработчик сообщений сразу же говорим FotoBot'у об этом
         */
        fb.h = h;

    }

    protected void onDestroy() {
        super.onDestroy();
  //      releaseCamera();
        Log.d(LOG_TAG, "MainActivity: onDestroy");
    }

    protected void onPause() {
        super.onPause();
//        releaseCamera();
        Log.d(LOG_TAG, "MainActivity: onPause");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "MainActivity: onRestart");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "MainActivity: onRestoreInstanceState");
    }

    protected void onResume(SurfaceHolder holder) {

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");


        wakeLock.acquire();

        Log.d(LOG_TAG, "MainActivity: onResume");
// Camera

        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(holder);

        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }


        final FotoBot fb = (FotoBot) getApplicationContext();
        Log.d(LOG_TAG, "MainActivity: onResume");
        Log.d(LOG_TAG, "MainActivity: fb.getstatus()" + fb.getstatus());
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        super.onResume();

        h = new Handler(hc);
        btnStart = (Button) findViewById(R.id.play);
        btnStop = (Button) findViewById(R.id.stop);

        if (fb.getstatus() == 1) {
            btnStart.setText("Пуск");
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
        }

        if (fb.getstatus() == 2) {
            btnStart.setText("Пуск");
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
        }

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "MainActivity: onSaveInstanceState");
    }

    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "MainActivity: onStart");
    }

    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "MainActivity: onStop");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user clicks the Status button
     */
    public void showStatus(View view) {
        Intent intent = new Intent(this, Status.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Settings button
     */
    public void showSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    /**
     * Запускаем Fotobot
     *
     * @param v
     */
    public void startFotobot(View v) {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");


        wakeLock.acquire();

        final FotoBot fb = (FotoBot) getApplicationContext();


        // h = new Handler(hc);


        switch (v.getId()) {
            case R.id.play:
                btnStart.setEnabled(false);
                btnStart.setText("Пуск");
                btnStop = (Button) findViewById(R.id.stop);
                btnStop.setEnabled(true);
                btnConfig.setEnabled(false);

                Thread t = new Thread(new Runnable() {
                    public void run() {

                        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                                "MyWakelockTag");

                        wakeLock.acquire();

                        fb.SendMessage("Фотобот начинает свою работу");

                        if (fb.Network_Connection_Method.contains("В начале работы")) {
                            fb.MakeInternetConnection(getApplicationContext(), h);
                        }

                        for (int i = 1; i <= 1000000000; i++) {

                            fb.batteryLevel();

                            if (fb.getstatus() == 3) {
                                //if (mCamera != null) {
                                    mCamera.stopPreview();
                                    mCamera.setPreviewCallback(null);
                                    mCamera.release();
                                    mCamera = null;
                                  //  mUnexpectedTerminationHelper.fini();
                                //}
                                fb.SendMessage(h, "Фотобот остановлен");
                                return;
                            }

                          /*  mCamera = Camera.open();

                            try {
                                mCamera.setPreviewDisplay(fb.sHolder);

                            } catch (IOException exception) {
                                mCamera.release();
                                mCamera = null;
                            }

                            mCamera.startPreview();
*/
                            fb.LoadData();

                            if (fb.Network_Connection_Method.contains("На каждом шаге")) {
                                fb.MakeInternetConnection(getApplicationContext(), h);
                            }



                            AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);


                            if (fb.Use_Flash) {
                                mCamera.stopPreview();
                                Camera.Parameters parameters = mCamera.getParameters();
                                try {
                                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                                } catch (Exception e) {
                                    fb.SendMessage(h, "Camera.Parameters.FLASH_MODE_ON error");
                                }
                                mCamera.setParameters(parameters);
                                mCamera.startPreview();

                                fb.fbpause(h, fb.process_delay);
                            }

                          // Camera.Parameters params;

                            if (fb.Photo_Post_Processing_Method.contains("Software")) {

                                Camera.Parameters params = mCamera.getParameters();

                                String string = fb.Image_Size;
                                String[] parts = string.split("x");
                                String width = parts[0];
                                String height = parts[1];

                                params.setPictureSize(Integer.parseInt(width), Integer.parseInt(height));
                                mCamera.setParameters(params);

                                fb.SendMessage("Разрешение фото: " + Integer.parseInt(width) + " " + Integer.parseInt(height));
                            }

                            mCamera.takePicture(null, null, mCall);
                            fb.SendMessage("Фото сделано");

                            fb.fbpause(h, fb.process_delay);

                            if (fb.Use_Flash) {
                                mCamera.stopPreview();

                                parameters = mCamera.getParameters();

                                fb.fbpause(h, fb.process_delay);

                                try {
                                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                                } catch (Exception e) {
                                    fb.SendMessage(h, "Camera.Parameters.FLASH_MODE_OFF error");
                                }

                                try {
                                    mCamera.setParameters(parameters);
                                } catch (Exception e) {
                                    fb.SendMessage(h, "setParameters error");
                                }

                            }

                            mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                            mgr.setStreamMute(AudioManager.STREAM_SYSTEM, false);


                            fb.SendMail(h, "fotobot.jpg");

                            if (fb.Network_Connection_Method.contains("На каждом шаге")) {
                                fb.CloseInternetConnection(getApplicationContext(), h);
                            }

                            fb.SendMessage("\n");
/*
                            mCamera.stopPreview();
                            mCamera.release();
                            mCamera = null;
*/
                            fb.fbpause(h, fb.Photo_Frequency);


                        }

                     /*   if (fb.getstatus() == 3) {
                            mCamera.stopPreview();
                            mCamera.release();
                            mCamera = null;
                            return;
                        } */
                    }

                }
                );

                t.start();
                fb.setstatus(2);
                Log.d(LOG_TAG, "startFotobot: fb.getstatus()" + fb.getstatus());
                break;

            default:
                break;
        }
    }

    public void stopFotobot(View v) {
        h = new Handler(hc);
        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.CloseInternetConnection(getApplicationContext(), h);

        Log.d(LOG_TAG, "stopFotobot: fb.getstatus()" + fb.getstatus());
        fb.setstatus(3);
        Log.d(LOG_TAG, "stopFotobot: STOP_FOTOBOT" + STOP_FOTOBOT);

        btnStart.setText("Пуск");
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnConfig.setEnabled(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

// Camera

        //get camera parameters
        parameters = mCamera.getParameters();

        //set camera parameters
        mCamera.setParameters(parameters);
        mCamera.startPreview();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.holder = holder;
        // The Surface has been created, acquire the camera and tell it where
        // to draw the preview.
        mUnexpectedTerminationHelper.init();


        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(holder);

        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }


        Camera.Parameters params = mCamera.getParameters();
        fb.camera_resolutions = params.getSupportedPictureSizes();
//        mCamera.release();
//        mCamera = null;

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }


    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
            mUnexpectedTerminationHelper.fini();
        }
    }


}
