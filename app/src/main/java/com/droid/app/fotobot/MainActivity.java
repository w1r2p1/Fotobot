package com.droid.app.fotobot;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
// commented to debug ffc
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

  //  public class MainActivity extends AppCompatActivity {
    private int screenWidth, screenHeight;
    public static final int UNKNOW_CODE = 99;
    final String LOG_TAG = "Logs";
 //   final String HANDLER = "MainActivity";
    final int STATUS_STOPPED = 333;

    private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
    private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
    private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;

    int n;
    ScrollView LogWidget;
    RelativeLayout WorkSpace;
    boolean STOP_FOTOBOT = false;
    Button btnStart;
    Button btnStop;
    Handler h = null;
    TextView tvInfo;
// adopted for ffc
//    boolean preview_stopped = false;
    boolean preview_stopped = true;
    Camera.PictureCallback mPicture = new Camera.PictureCallback(){
        @Override
        public void onPictureTaken(byte[] data, Camera camera){
            File pictureFile = new File("/storage/sdcard0/"+"img.jpg");

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

    /**
     * Печатает сообщения на экран телефона, нужен для того чтобы получать данные из потока в котором работает FotoBot
     */
    Handler.Callback hc = new Handler.Callback() {
        public boolean handleMessage(Message msg) {

            final FotoBot fb = (FotoBot) getApplicationContext();

// Wakelock 1
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyWakelockTag");

            wakeLock.acquire();


            DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String reportDate = dateformat.format(today);

            String message = (String) msg.obj; //Extract the string from the Message
            fb.log = reportDate + ": " + message + "\n\n" + fb.log;

// string length

            if (fb.log.length() > fb.loglength) {
                fb.log = fb.log.substring(0, fb.loglength);
            }

            tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Log_Font_Size);
            tvInfo.setTypeface(Typeface.MONOSPACE);
            tvInfo.setTextColor(Color.rgb(190, 190, 190));

            Log.d(LOG_TAG, reportDate + ": " + message);

            tvInfo.setText(fb.log);

            n = msg.what;
            if (msg.what == STATUS_STOPPED) btnStart.setText("Play");

            if (fb.getstatus() == 3 && fb.thread_stopped) {

                findViewById(R.id.play).setEnabled(true);
                findViewById(R.id.stop).setEnabled(false);
                findViewById(R.id.config).setEnabled(true);

                findViewById(R.id.help).setEnabled(true);

                findViewById(R.id.log).setEnabled(true);

                findViewById(R.id.mainw).setEnabled(true);

                fb.thread_stopped = false;

            }

            //  wakeLock.release();

            return false;

        }
    };

    TextView text;
    Intent intent;
    TelephonyManager tel;
    MyPhoneStateListener myPhoneStateListener;

    private SurfaceView sv;
    private Bitmap bmp;

    /**
     * постобработка фото
     */
    Camera.PictureCallback mCall = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            final FotoBot fb = (FotoBot) getApplicationContext();

            fb.LoadSettings();

// Wakelock 2
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyWakelockTag");

            wakeLock.acquire();


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;

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

            options.inPreferredConfig = Bitmap.Config.RGB_565;

            bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            FileOutputStream fOut = null;
            File file = new File(fb.work_dir + "/" + fb.Image_Name);

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

            bmp.compress(Bitmap.CompressFormat.JPEG, fb.JPEG_Compression, fOut);

            fb.fbpause(h, 1);

            getUsedMemorySize();

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

            try {
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            bmp.recycle();

            bmp = null;

            //   wakeLock.release();

        }

    };

    //Camera variables
    //a surface holder
    private SurfaceHolder sHolder;
    //a variable to control the camera
    private Camera mCamera;
    //the camera parameters
    private Camera.Parameters parameters;
    private UnexpectedTerminationHelper mUnexpectedTerminationHelper = new UnexpectedTerminationHelper();

    /**
     * Печатает в консоль общее число памяти, доступная память, занятую память
     *
     * @return используемая память
     */
    public long getUsedMemorySize() {

        final FotoBot fb = (FotoBot) getApplicationContext();
        long freeMemory = 0L;
        long totalMemory = 0L;
        long usedMemory = -1L;

        try {

            Runtime info = Runtime.getRuntime();
            freeMemory = info.freeMemory();
            totalMemory = info.totalMemory();
            usedMemory = totalMemory - freeMemory;

            fb.freeMemory = String.format("%.2f", (float) freeMemory / 1000000) + "MB";
            fb.totalMemory = String.format("%.2f", (float) totalMemory / 1000000) + "MB";
            fb.usedMemory = String.format("%.2f", (float) usedMemory / 1000000) + "MB";

        } catch (Exception e) {

            e.printStackTrace();

        }

        return usedMemory;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.d(LOG_TAG, "MainActivity: onCreate");

        final FotoBot fb = (FotoBot) getApplicationContext();

        fb.LoadSettings();
















//        fb.log = Html.fromHtml(getResources().getString(R.string.Fotobot)) + "\n\n" +
//                 "---------------------" + "\n\n" +
//                getResources().getString(R.string.fast_start) + "\n\n" +
//                fb.work_dir + "\n\n" +
//                getResources().getString(R.string.update);

        fb.work_dir_init();

            if (savedInstanceState == null)   // приложение запущено впервые
        {
            Log.d(LOG_TAG, "MainActivity: onCreate started first time");

            if (fb.clean_log) {
                fb.log = "";
                fb.clean_log = false;
            }
        } else // приложение восстановлено из памяти
        {
            Log.d(LOG_TAG, "MainActivity: onCreate restored");
            if (fb.clean_log) {
                fb.log = "";
                fb.clean_log = false;
            }
        }

        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);

        int statusBarHeight;

        switch (displayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_HIGH:
                statusBarHeight = HIGH_DPI_STATUS_BAR_HEIGHT;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
                break;
            case DisplayMetrics.DENSITY_LOW:
                statusBarHeight = LOW_DPI_STATUS_BAR_HEIGHT;
                break;
            default:
                statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
        }

        screenHeight = screenHeight - ((int) pxFromDp(getApplicationContext(), statusBarHeight));

        fb.Working_Area_Height = screenHeight;

        myPhoneStateListener = new MyPhoneStateListener();
        tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tel.listen(myPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        setContentView(R.layout.activity_main);

        tvInfo = (TextView) findViewById(R.id.tvInfo);

        text = (TextView) findViewById(R.id.textView);

        WorkSpace = (RelativeLayout) findViewById(R.id.workspace);
        WorkSpace.setBackgroundColor(Color.rgb(64, 98, 125));
        WorkSpace.setMinimumHeight(screenHeight);
        WorkSpace.setMinimumWidth(screenWidth);

        LogWidget = (ScrollView) findViewById(R.id.scrollView);
       // LogWidget.setBackgroundColor(Color.rgb(34, 58, 95));
        LogWidget.setBackgroundColor(Color.rgb(51, 51, 51));
        LogWidget.setMinimumWidth(screenWidth);

        final Button btnHelp = (Button) findViewById(R.id.help);
        btnHelp.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                }
                return false;
            }

        });

        final Button btnLog = (Button) findViewById(R.id.log);

        btnLog.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                }
                return false;
            }

        });

        final Button btnMainw = (Button) findViewById(R.id.mainw);

        btnMainw.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {

                }
                return false;
            }

        });

        sv = (SurfaceView) findViewById(R.id.surfaceView);
        sHolder = sv.getHolder();
        sHolder.addCallback(this);
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        intent = new Intent(MainActivity.this, Status.class);

        tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Log_Font_Size);
        tvInfo.setTypeface(Typeface.MONOSPACE);
        tvInfo.setTextColor(Color.rgb(190, 190, 190));

        tvInfo.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
//        tvInfo.setText(fb.log);

        h = new Handler(hc);

        /**
         * получили указатель на обработчик сообщений сразу же говорим FotoBot'у об этом
         */
        fb.h = h;



        if ( fb.launched_first_time ) {
            fb.set_default_storage();
            fb.launched_first_time = false;
        }

        fb.log = Html.fromHtml(getResources().getString(R.string.Fotobot)) + "\n\n" +
                "---------------------" + "\n\n" +
                getResources().getString(R.string.fast_start) + "\n\n" +
                fb.work_dir + "\n\n" +
                getResources().getString(R.string.update);

        tvInfo.setText(fb.log);


    }

    protected void onDestroy() {
        final FotoBot fb = (FotoBot) getApplicationContext();
        super.onDestroy();
        Log.d(LOG_TAG, "MainActivity: onDestroy");
    }

    protected void onPause() {
        final FotoBot fb = (FotoBot) getApplicationContext();
        super.onPause();
        Log.d(LOG_TAG, "MainActivity: onPause");
    }

    protected void onRestart() {
        final FotoBot fb = (FotoBot) getApplicationContext();
        super.onRestart();
        Log.d(LOG_TAG, "MainActivity: onRestart");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "MainActivity: onRestoreInstanceState");
    }

    protected void onResume(SurfaceHolder holder) {

// Wakelock 4
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");

        wakeLock.acquire();

        Log.d(LOG_TAG, "MainActivity: onResume");
/* commented to debug ffc
        mCamera = Camera.open(1);
        try {
            mCamera.setPreviewDisplay(holder);

        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
*/
        final FotoBot fb = (FotoBot) getApplicationContext();
        Log.d(LOG_TAG, "MainActivity: onResume");
        Log.d(LOG_TAG, "MainActivity: fb.getstatus()" + fb.getstatus());
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        super.onResume();

        h = new Handler(hc);
        btnStart = (Button) findViewById(R.id.play);
        btnStop = (Button) findViewById(R.id.stop);

        if (fb.getstatus() == 1) {
            btnStart.setText(getResources().getString(R.string.start_button));
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);

            Button btnHelp = (Button) findViewById(R.id.help);
            btnHelp.setEnabled(true);

            Button btnLog = (Button) findViewById(R.id.log);
            btnLog.setEnabled(true);

            Button btnMainw = (Button) findViewById(R.id.mainw);
            btnMainw.setEnabled(true);

        }

        if (fb.getstatus() == 2) {
            btnStart.setText(getResources().getString(R.string.start_button));
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);

            Button btnHelp = (Button) findViewById(R.id.help);
            btnHelp.setEnabled(false);

            Button btnLog = (Button) findViewById(R.id.log);
            btnLog.setEnabled(false);

            Button btnMainw = (Button) findViewById(R.id.mainw);
            btnMainw.setEnabled(false);

        }

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
        Log.d(LOG_TAG, "MainActivity: onSaveInstanceState");
    }

    protected void onStart() {

        super.onStart();
        Log.d(LOG_TAG, "MainActivity: onStart");

        final FotoBot fb = (FotoBot) getApplicationContext();

        try {
            fb.versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    protected void onStop() {
        final FotoBot fb = (FotoBot) getApplicationContext();
        super.onStop();
        Log.d(LOG_TAG, "MainActivity: onStop");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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

// Wakelock 5
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");

        wakeLock.acquire();

        final FotoBot fb = (FotoBot) getApplicationContext();



        fb.LoadSettings();

        fb.work_dir_init();

        switch (v.getId()) {
            case R.id.play:
                findViewById(R.id.play).setEnabled(false);
                findViewById(R.id.stop).setEnabled(true);
                findViewById(R.id.config).setEnabled(false);

                findViewById(R.id.help).setEnabled(false);

                findViewById(R.id.mainw).setEnabled(false);

                findViewById(R.id.log).setEnabled(false);

                Thread t = new Thread(new Runnable() {
                    public void run() {

// Wakelock 6
                        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                                "MyWakelockTag");

                        wakeLock.acquire();

                        AudioManager mgr = null;

                        fb.SendMessage(getResources().getString(R.string.start_message));

/* comment to debug ffc
// Добавлено в Andorid 5. Без этого не работает. Не понятно, как раньше работало.
                        if (mCamera == null) {
                            mCamera = Camera.open(1);
                        }

                        fb.Camera_Properties = mCamera.getParameters().flatten();
*/
                        for (int i = 1; i <= 1000000000; i++) {

// method1 соединяемся с сетью
                            if ( fb.network && !(fb.Method1_activated) ) {
                                if (fb.Network_Connection_Method.contains("Method 1")) {
                                    if (android.os.Build.VERSION.SDK_INT <= 21) {

                                        fb.MakeInternetConnection();
                                    }
                                    fb.Method1_activated = true;
                                }
                            }

// method1 рарзрываем соединение с сетью
                            if ( !(fb.network) && fb.Method1_activated ) {
                                if (fb.Network_Connection_Method.contains("Method 1")) {
                                    if (android.os.Build.VERSION.SDK_INT <= 21) {

                                        fb.CloseInternetConnection();
                                    }
                                    fb.Method1_activated = false;
                                }
                            }

// загружаем настройки из реестра
                            fb.LoadSettings();

// на каждом шаге удаляем логфайл, чтобы не забить память
                            File log_file = null;

                            log_file = new File((fb.work_dir + "/logfile.txt"));

                            if (log_file.isFile()) {

                                log_file.delete();
                                Log.d(LOG_TAG, "logcat file has been deleted");
                            }

// подготавливаем камеру для фото
                            fb.Image_Index = i;



                            fb.batteryLevel();

                            if (fb.network) {
                                if ((fb.Network_Connection_Method.contains("Method 2")) && (Build.VERSION.SDK_INT <= 21)) {
                                    fb.MakeInternetConnection();
                                }
                            }

                            if ( fb.back_camera && fb.Use_Bc) {
                                fb.SendMessage(getResources().getString(R.string.Back_Camera) + " " + getResources().getString(R.string.starting_to_make_photo) + " " + fb.Image_Index);
                                if (fb.getstatus() == 3) {
                                    if (mCamera != null) {
                                        mCamera.stopPreview();
                                        mCamera.setPreviewCallback(null);
                                        mCamera.release();
                                        mCamera = null;
                                    }
                                    fb.thread_stopped = true;
                                    fb.SendMessage(h, getResources().getString(R.string.stop_message));
                                    return;
                                }

                                DateFormat df = new SimpleDateFormat("MM-dd-yy_HH-mm-ss-SSS");
                                fb.Image_Name = df.format(new Date()) + ".jpg";
                                fb.Image_Name_Full_Path = fb.work_dir + "/" + fb.Image_Name;

                                fb.bc_Image_Name = fb.Image_Name;
                                fb.bc_Image_Name_Full_Path = fb.Image_Name_Full_Path;

                                fb.LoadSettings();



                                mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);


                                // Camera.Parameters params;
                                String string = fb.Image_Size;
                                String[] parts = string.split("x");
                                String width = parts[0];
                                String height = parts[1];

// start and set camera parameters

                                if (mCamera == null) {

                                    //   fb.SendMessage("Camera is not initialized.");

                                    try {
                                        mCamera = Camera.open(fb.bcId);
                                        //      fb.SendMessage("Camera has been initialized for parameters setting.");
                                    } catch (Exception e) {
                                        fb.SendMessage("Problem with camera initialization in main cycle.");

                                    }
                                }

                                if (!preview_stopped) {
                                    try {
                                        mCamera.stopPreview();
                                    } catch (Exception e) {
                                        fb.SendMessage("Preview couldn't be stopped in the main cycle.");

                                    }
                                    preview_stopped = true;
                                }

                                Camera.Parameters parameters = mCamera.getParameters();

                                if (fb.Use_Flash) {
                                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                                }

                                if (fb.Photo_Post_Processing_Method.contains("Software")) {
                                    parameters.setPictureSize(Integer.parseInt(width), Integer.parseInt(height));
                                }

                                try {
                                    mCamera.setParameters(parameters);
                                } catch (Exception e) {
                                    fb.SendMessage("Camera parameters have not been changed in the main cycle.");

                                    e.printStackTrace();
                                }

                                fb.fbpause(h, 3);

                                if (preview_stopped) {

                                    try {
                                        mCamera.setPreviewDisplay(fb.holder);
                                        mCamera.startPreview();
                                        preview_stopped = false;
                                    } catch (Exception e) {
                                        fb.SendMessage("Problem with preview starting after camera initialization in the main cycle.");

                                    }
                                }

                                fb.fbpause(h, 3);


                                try {
                                    mCamera.takePicture(null, null, mCall);
                                    fb.SendMessage(getResources().getString(R.string.photo_has_been_taken));
                                } catch (Exception e) {
                                    fb.SendMessage("Problem with picture taking.");

                                }


// *************************************** FFC *****************************************
/*                            if (mCamera != null) {
                                mCamera.stopPreview();
                                mCamera.setPreviewCallback(null);
                                mCamera.release();
                                mCamera = null;
                            }

                            Camera camera;
                            camera = Camera.open(1);
                            String camera_Properties = camera.getParameters().flatten();
                            try {
                                camera.setPreviewDisplay(fb.holder);
                            } catch (Exception e) {
                                Log.d(LOG_TAG,"setPreviewDisplay failed for ffc");
                            }

                            try {
                                camera.startPreview();
                            } catch (Exception e) {
                                Log.d(LOG_TAG,"startPreview failed for ffc");
                            }

                            camera.takePicture(null,null,mPicture);

                            if (camera != null) {
                             //   camera.stopPreview();
                             //   camera.setPreviewCallback(null);
                                camera.release();
                                camera = null;
                            }
*/
// *************************************************************************************


                                fb.fbpause(h, 3);

                                fb.fbpause(h, fb.process_delay);

                                if (fb.Use_Flash) {
                                    try {
                                        mCamera.stopPreview();
                                        preview_stopped = true;
                                        fb.fbpause(h, 1);
                                    } catch (Exception e) {
                                        fb.SendMessage("FLASH OFF: problem with stopping of preview.");

                                    }

                                    parameters = mCamera.getParameters();

                                    fb.fbpause(h, fb.process_delay);

                                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

                                    try {
                                        mCamera.setParameters(parameters);
                                    } catch (Exception e) {
                                        fb.SendMessage("setParameters error");

                                    }

                                }

                                //releaseCamera();
                                mCamera.release();
                                mCamera = null;

                            }












                            fb.fbpause(h,5);





















                            if ( fb.front_camera && fb.Use_Fc) {

                                if (fb.getstatus() == 3) {
                                    if (mCamera != null) {
                                        mCamera.stopPreview();
                                        mCamera.setPreviewCallback(null);
                                        mCamera.release();
                                        mCamera = null;
                                    }
                                    fb.thread_stopped = true;
                                    fb.SendMessage(h, getResources().getString(R.string.stop_message));
                                    return;
                                }

                                fb.SendMessage(getResources().getString(R.string.Front_Camera) + " " + getResources().getString(R.string.starting_to_make_photo) + " " + fb.Image_Index);

                         //       fb.batteryLevel();

                                String str1 = null;
                                String str2 = null;


                                DateFormat df = new SimpleDateFormat("MM-dd-yy_HH-mm-ss-SSS");

                                str1 = fb.Image_Name;
                                str2 = fb.Image_Name_Full_Path;

                                fb.Image_Name = "fc_" + df.format(new Date()) + ".jpg";
                                fb.Image_Name_Full_Path = fb.work_dir + "/" + fb.Image_Name;

                                fb.fc_Image_Name = fb.Image_Name;
                                fb.fc_Image_Name_Full_Path = fb.Image_Name_Full_Path;



                            /*    fb.LoadSettings();

                                if (fb.network) {
                                    if ((fb.Network_Connection_Method.contains("Method 2")) && (Build.VERSION.SDK_INT <= 21)) {
                                        fb.MakeInternetConnection();
                                    }
                                }
*/
                                mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                                mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);


                                // Camera.Parameters params;
                                String string = fb.fc_Image_Size;
                                String[] fc_parts = string.split("x");
                                String fc_width = fc_parts[0];
                                String fc_height = fc_parts[1];

// start and set camera parameters

                                if (mCamera == null) {

                                    //   fb.SendMessage("Camera is not initialized.");

                                    try {
                                        mCamera = Camera.open(fb.fcId);
//                                        fb.SendMessage("Front camera has been initialized for parameters setting.");
                                    } catch (Exception e) {
                                        fb.SendMessage("Problem with camera initialization in main cycle.");

                                    }
                                }

                                if (!preview_stopped) {
                                    try {
                                        mCamera.stopPreview();
                                    } catch (Exception e) {
                                        fb.SendMessage("Preview couldn't be stopped in the main cycle.");

                                    }
                                    preview_stopped = true;
                                }

                                Camera.Parameters parameters = mCamera.getParameters();


                                if (fb.Photo_Post_Processing_Method.contains("Software")) {
                                    parameters.setPictureSize(Integer.parseInt(fc_width), Integer.parseInt(fc_height));
                                }

                                try {
                                    mCamera.setParameters(parameters);
                                } catch (Exception e) {
                                    fb.SendMessage("Camera parameters have not been changed in the main cycle.");

                                    e.printStackTrace();
                                }

                                fb.fbpause(h, 3);

                                if (preview_stopped) {

                                    try {
                                        mCamera.setPreviewDisplay(fb.holder);
                                        mCamera.startPreview();
                                        preview_stopped = false;
                                    } catch (Exception e) {
                                        fb.SendMessage("Problem with preview starting after camera initialization in the main cycle.");

                                    }
                                }

                                fb.fbpause(h, 3);


                                try {
                                    mCamera.takePicture(null, null, mCall);
                                    fb.SendMessage(getResources().getString(R.string.photo_has_been_taken));
                                } catch (Exception e) {
                                    fb.SendMessage("Problem with picture taking.");

                                }



                                fb.fbpause(h, fb.process_delay);


                                //releaseCamera();
                                mCamera.release();
                                mCamera = null;

// эти строки нельзя передвигать выше, иначе callback не поспевает
                                fb.Image_Name = str1;
                                fb.Image_Name_Full_Path = str2;

                            }


























































                            mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                            mgr.setStreamMute(AudioManager.STREAM_SYSTEM, false);

                            long start = System.currentTimeMillis();

                            if ( fb.attach_log && fb.network) {

                                if (logcat2file()) {

                                } else {
                                    fb.SendMessage("Проблема с доступом к logcat");
                                }

                            }

                            if ( fb.network) {
                                fb.fbpause(h, 1);

                                fb.SendMail(h, fb.bc_Image_Name_Full_Path, fb.fc_Image_Name_Full_Path);

                                long durationInMilliseconds = System.currentTimeMillis() - start;

                                fb.email_sending_time = durationInMilliseconds / 1000;

                                if ((fb.Network_Connection_Method.contains("Method 2") && (Build.VERSION.SDK_INT <= 21))) {
                                    fb.CloseInternetConnection();
                                }
                            }

                            fb.SendMessage("-----------------------------");

                            fb.SendMessage(getResources().getString(R.string.pause_between_photos) + " " + fb.Photo_Frequency + "sec");

                            fb.SendMessage("-----------------------------");
                            fb.SendMessage("");

                            if (mCamera != null) {

                                mCamera.stopPreview();
                                preview_stopped = true;
                                mCamera.setPreviewCallback(null);
                                mCamera.release();
                                mCamera = null;

                            }

                            fb.camera = mCamera;
                            fb.frame_delay = true;

                            fb.sms_check_file = true;

                            File logcat_file;
                            logcat_file = new File(fb.work_dir + "/logfile.txt");

                            boolean fileExists = logcat_file.isFile();
                            // если размер лога превышает 50 kb, то чистим его
                            if (fileExists) {
                                if ( logcat_file.length() / 1000 > fb.log_size ) {
                                    clearLog();
                                }
                            } else {
                                // fb.SendMessage(h, "logfile.txt doesn't exist.");
                            }

                            if ( fb.delete_foto ) {

                                if ( fb.Use_Bc) {
                                    File imgfile = new File(fb.Image_Name_Full_Path);

                                    if (imgfile.delete()) {
                                        fb.SendMessage("Файл " + fb.Image_Name + " был удален");
                                    } else {
                                        fb.SendMessage("Проблема с удалением фото: " + fb.Image_Name);
//                                    fb.SendMessage("Попробуйте поменять разрешение для фото в настройках");

                                    }
                                }

                                if ( fb.Use_Fc ) {
                                    File fc_imgfile = new File(fb.fc_Image_Name_Full_Path);

                                    if (fc_imgfile.delete()) {
                                        fb.SendMessage("Файл " + fb.fc_Image_Name + " был удален");
                                    } else {
                                        fb.SendMessage("Проблема с удалением фото: " + fb.fc_Image_Name);
//                                    fb.SendMessage("Попробуйте поменять разрешение для фото в настройках");

                                    }
                                }

                            }

                            fb.fbpause(h, fb.Photo_Frequency);
                            fb.sms_check_file = false;
                            fb.frame_delay = false;

                        }

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

        Log.d(LOG_TAG, "stopFotobot: fb.getstatus()" + fb.getstatus());
        fb.setstatus(3);
        Log.d(LOG_TAG, "stopFotobot: STOP_FOTOBOT" + STOP_FOTOBOT);

        fb.SendMessage(getResources().getString(R.string.request_for_stopping));

    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

/* commented to debug ffc
// Camera

        //get camera parameters
        parameters = mCamera.getParameters();

        //set camera parameters
        mCamera.setParameters(parameters);
        mCamera.startPreview();
*/
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        final FotoBot fb = (FotoBot) getApplicationContext();

        Camera.Parameters params;



        fb.numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for(int i = 0; i < fb.numberOfCameras; i++){
            Camera.getCameraInfo(i,ci);
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                fb.fcId = i;
                fb.front_camera = true;
                mCamera = Camera.open(fb.fcId);

       /*
       try {
            mCamera.setPreviewDisplay(holder);

        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
        */

                params = mCamera.getParameters();
                fb.fc_camera_resolutions = params.getSupportedPictureSizes();

                mCamera.release();
                mCamera = null;
            }
            if(ci.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                fb.bcId = i;
                mCamera = Camera.open(fb.bcId);
                params = mCamera.getParameters();
                fb.camera_resolutions = params.getSupportedPictureSizes();
                mCamera.release();
                mCamera = null;

            }
        }








        fb.holder = holder;
        // The Surface has been created, acquire the camera and tell it where
        // to draw the preview.
        mUnexpectedTerminationHelper.init();

// adopted for ffc











    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
// adopted for ffc
//        releaseCamera();
    }

    private void releaseCamera() {

        final FotoBot fb = (FotoBot) getApplicationContext();

        if (mCamera != null) {
            if ( !preview_stopped) {
                try {
                    mCamera.stopPreview();
                } catch (Exception e) {
                    fb.SendMessage("Preview couldn't be stopped in the main cycle.");

                }

                mCamera.setPreviewCallback(null);
                preview_stopped = true;
            }
            mCamera.release();
            mCamera = null;
            mUnexpectedTerminationHelper.fini();
        }

    }

    private int calculateSignalStrengthInPercent(int signalStrength) {
        return (int) ((float) signalStrength);
    }

    private class UnexpectedTerminationHelper {

        private Thread mThread;
        private Thread.UncaughtExceptionHandler mOldUncaughtExceptionHandler = null;
        private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) { // gets called on the same (main) thread
/* commented to debug ffc
                releaseCamera();
*/
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

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            final FotoBot fb = (FotoBot) getApplicationContext();
            super.onSignalStrengthsChanged(signalStrength);

            if (null != signalStrength && signalStrength.getGsmSignalStrength() != UNKNOW_CODE) {
                fb.GSM_Signal = calculateSignalStrengthInPercent(signalStrength.getGsmSignalStrength());

            }
        }
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    /**
     * FotoBots main window
     *
     * @param v
     */
    public void mainw(View v) {
        final FotoBot fb = (FotoBot) getApplicationContext();

        LogWidget = (ScrollView) findViewById(R.id.scrollView);
        //LogWidget.setBackgroundColor(Color.rgb(34, 58, 95));
        LogWidget.setBackgroundColor(Color.rgb(51, 51, 51));

        tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Log_Font_Size);
        tvInfo.setTypeface(Typeface.MONOSPACE);
        tvInfo.setTextColor(Color.rgb(190, 190, 190));

        tvInfo.setText(fb.log);

        findViewById(R.id.play).setEnabled(true);
        findViewById(R.id.stop).setEnabled(false);

    }

    /**
     * FotoBots log window
     *
     * @param v
     */
    public void log(View v) {

        final FotoBot fb = (FotoBot) getApplicationContext();

        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(fb.work_dir + "/fblog.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder strBuilder = new StringBuilder();

        String line;
        try {
            while ((line = fileReader.readLine()) != null) {
                strBuilder.insert(0, line);
                strBuilder.insert(0, "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String contentsOfFile = strBuilder.toString();

        LogWidget = (ScrollView) findViewById(R.id.scrollView);
        LogWidget.setBackgroundColor(Color.rgb(54, 54, 54));

        tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Log_Font_Size);
        tvInfo.setTypeface(Typeface.MONOSPACE);
        tvInfo.setTextColor(Color.rgb(190, 190, 190));

        tvInfo.setText(contentsOfFile);

        Log.d(LOG_TAG, "reverse: " + contentsOfFile);

    }

    private boolean logcat2file() {

        final FotoBot fb = (FotoBot) getApplicationContext();

        String cmd = null;

        try {

            if (Build.VERSION.SDK_INT <= 12) {
                cmd = "logcat -v long -d -f " + fb.work_dir + "/logfile.txt" + " Logs:* FotoBot:* *:S";
            } else {
                cmd = "logcat -v long -d -f " + fb.work_dir + "/logfile.txt";
            }
            Runtime.getRuntime().exec(cmd);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    private String file2string() {

        final FotoBot fb = (FotoBot) getApplicationContext();

        BufferedReader fileReader = null;

        try {
            fileReader = new BufferedReader(new FileReader(fb.work_dir + "/logfile.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder strBuilder = new StringBuilder();

        int line_counter = 0;

        String line;

        try {
            while ((line = fileReader.readLine()) != null && line_counter < fb.log_line_number) {
                strBuilder.insert(0, line);
                strBuilder.insert(0, "\n");
                line_counter = line_counter + 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String contentsOfFile = strBuilder.toString();

        return contentsOfFile;
    }

    public void logcat(View v) {

        final FotoBot fb = (FotoBot) getApplicationContext();

    //    fb.SendMessage("getExternalStorage(): " + getExternalStorage());
    //    fb.SendMessage("Environment.getExternalStorageDirectory(): " + Environment.getExternalStorageDirectory());
    //    fb.SendMessage("Environment.getExternalStoragePublicDirectory(): " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

/*        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }

        Camera camera;
        camera = Camera.open(1);
        String camera_Properties = camera.getParameters().flatten();
        try {
            camera.setPreviewDisplay(fb.holder);
        } catch (Exception e) {
          Log.d(LOG_TAG,"setPreviewDisplay failed for ffc");
        }

        try {
            camera.startPreview();
        } catch (Exception e) {
            Log.d(LOG_TAG,"startPreview failed for ffc");
        }

        camera.takePicture(null,null,mPicture);

        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
*/
        /*FrontFaceCamera ffc = new FrontFaceCamera(getApplicationContext(), fb.holder);
        ffc.getCameraInstance();
        String ffc_Properties = ffc.getCameraParameters();
        Log.d(LOG_TAG, "ffc: " + ffc_Properties);

        ffc.takePicture();

        ffc.releaseCamera();
*/


        File logfile = new File(fb.work_dir + "/logfile.txt");

        if (logcat2file()) {
            //fb.SendMessage("Заполнили файл данными из logcat");
        } else {
            fb.SendMessage("Проблема с доступом к logcat");
        }

        fb.fbpause(fb.h, 1);

        LogWidget = (ScrollView) findViewById(R.id.scrollView);
        LogWidget.setBackgroundColor(Color.rgb(54, 54, 54));

        tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Log_Font_Size);
        tvInfo.setTypeface(Typeface.MONOSPACE);
        tvInfo.setTextColor(Color.rgb(190, 190, 190));

        tvInfo.setText(file2string());

        findViewById(R.id.play).setEnabled(false);
        findViewById(R.id.stop).setEnabled(false);

        try {
            logfile.delete();
            //  fb.SendMessage("Logfile from catlog has been deleted");
        } catch (Exception e) {
            fb.SendMessage("Problem with deleting of Logfile from catlog");
        }

    }


    /**
     * FotoBots help window
     *
     * @param v
     */
    public void help(View v) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.Show_Help = true;


        InputStream is = getResources().openRawResource(R.raw.file);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;
        String str = "";

        try {
            while ((readLine = br.readLine()) != null) {
                str = str + readLine;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        LogWidget = (ScrollView) findViewById(R.id.scrollView);
        LogWidget.setBackgroundColor(Color.rgb(26, 54, 60));

        tvInfo.setText(Html.fromHtml("<h1>Fotobot " + fb.versionName + "</h1>" + str));

        findViewById(R.id.play).setEnabled(false);
        findViewById(R.id.stop).setEnabled(false);

    }
    public void clearLog(){
        try {
            Process process = new ProcessBuilder()
                    .command("logcat", "-c")
                    .redirectErrorStream(true)
                    .start();
        } catch (IOException e) {
        }
    }

    static String getExternalStorage(){
        String exts =  Environment.getExternalStorageDirectory().getPath();
        try {
            FileReader fr = new FileReader(new File("/proc/mounts"));
            BufferedReader br = new BufferedReader(fr);
            String sdCard=null;
            String line;
            while((line = br.readLine())!=null){
                if(line.contains("secure") || line.contains("asec")) continue;
                if(line.contains("fat")){
                    String[] pars = line.split("\\s");
                    if(pars.length<2) continue;
                    if(pars[1].equals(exts)) continue;
                    sdCard =pars[1];
                    break;
                }
            }
            fr.close();
            br.close();
            return sdCard;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


}
