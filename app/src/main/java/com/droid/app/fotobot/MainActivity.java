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
import android.widget.LinearLayout;
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
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static class MyCustomFormatter extends Formatter {

        @Override

        public String format(LogRecord record) {

            StringBuffer sb = new StringBuffer();

            sb.append(record.getMessage());
            sb.append("\n");

            return sb.toString();

        }

    }

    private int screenWidth, screenHeight;
    public static final int UNKNOW_CODE = 99;
    final String LOG_TAG = "Logs";
    final int STATUS_STOPPED = 333;

    private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
    private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
    private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;

    int n;
    FotoBot fb;
    ScrollView LogWidget;
    LinearLayout Buttons1, Buttons2;
    RelativeLayout WorkSpace;
    boolean STOP_FOTOBOT = false;
    Button btnStart;
    Button btnStop;
    Button btnConfig;
    Handler h = null;
    TextView tvInfo;
    boolean preview_stopped = false;

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
            fb.log = reportDate + ": " + message + "\n" + fb.log;

// string length

            if (fb.log.length() > fb.loglength) {
                fb.log = fb.log.substring(0, fb.loglength);
            }

            tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Log_Font_Size);
            tvInfo.setTypeface(Typeface.MONOSPACE);
            tvInfo.setTextColor(Color.rgb(190, 190, 190));

            fb.logger.fine(reportDate + ": " + message);

            if (fb.Show_Help) {
                tvInfo.setText(Html.fromHtml((getResources().getString(R.string.main_help))));
                fb.Show_Help = false;
                return false;
            }

            tvInfo.setText(fb.log);

            // Log.d(LOG_TAG, "Handler.Callback(): fb.getstatus()" + fb.getstatus());
            n = msg.what;
            if (msg.what == STATUS_STOPPED) btnStart.setText("Play");

            if (fb.getstatus() == 3) {

                btnStart = (Button) findViewById(R.id.play);
                btnStop = (Button) findViewById(R.id.stop);
                btnStart.setText(getResources().getString(R.string.start_button));
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnConfig.setEnabled(true);

                Button btnHelp = (Button) findViewById(R.id.help);
                btnHelp.setBackgroundColor(Color.rgb(90, 90, 90));
                btnHelp.setEnabled(true);

                Button btnLog = (Button) findViewById(R.id.log);
                btnLog.setBackgroundColor(Color.rgb(90, 90, 90));
                btnLog.setEnabled(true);

                Button btnMainw = (Button) findViewById(R.id.mainw);
                btnMainw.setBackgroundColor(Color.rgb(90, 90, 90));
                btnMainw.setEnabled(true);

                btnStart.postInvalidate();
                btnStop.postInvalidate();
            }

            return false;

        }
    };

    TextView text;
    Intent intent;
    //   String str1 = "Fotobot str to file";
    TelephonyManager tel;
    MyPhoneStateListener myPhoneStateListener;
    //   private ImageView iv_image;
    private SurfaceView sv;
    private Bitmap bmp;

    /**
     * постобработка фото
     */
    Camera.PictureCallback mCall = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            final FotoBot fb = (FotoBot) getApplicationContext();

            fb.LoadData();

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
            //  String fdir = getFilesDir().toString();
            File file = new File(getFilesDir(), fb.Image_Name);

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

            File attach_file;
            attach_file = new File(fb.Image_Name_Full_Path);

            boolean fileExists = attach_file.isFile();

            if (fileExists) {
                fb.SendMessage(h, attach_file.length() / 1000 + "Kb");
            } else {
                fb.SendMessage(h, "Image doesn't exist.");
            }

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
        //  final String LOG_USED_MEMORY = "UsedMem";
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

        //   Log.d(LOG_TAG, "\n\n\n\n\nMainActivity: onCreate\n\n\n\n\n");

        final FotoBot fb = (FotoBot) getApplicationContext();

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

        Log.d(LOG_TAG, "screenHeight: " + screenHeight);
        Log.d(LOG_TAG, "statusBarHeight: " + statusBarHeight);

        screenHeight = screenHeight - ((int) pxFromDp(getApplicationContext(), statusBarHeight));
        Log.d(LOG_TAG, "pxFromDp: " + (int) pxFromDp(getApplicationContext(), statusBarHeight));
        Log.d(LOG_TAG, "screenHeight: " + screenHeight);

        fb.Working_Area_Height = screenHeight;

// http://stackoverflow.com/questions/20264268/how-to-get-height-and-width-of-navigation-bar-programmatically

     //   this.registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

// Wakelock 3
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");

        super.onCreate(savedInstanceState);
        wakeLock.acquire();

        myPhoneStateListener = new MyPhoneStateListener();
        tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tel.listen(myPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.play);
        btnConfig = (Button) findViewById(R.id.config);

        tvInfo = (TextView) findViewById(R.id.tvInfo);

        text = (TextView) findViewById(R.id.textView);

        WorkSpace = (RelativeLayout) findViewById(R.id.workspace);
        WorkSpace.setBackgroundColor(Color.rgb(64, 98, 125));
        WorkSpace.setMinimumHeight(screenHeight);
        WorkSpace.setMinimumWidth(screenWidth);

        Buttons1 = (LinearLayout) findViewById(R.id.buttons1);
        Buttons1.setBackgroundColor(Color.rgb(192, 192, 192));
        Buttons1.setMinimumWidth(screenWidth);

        LogWidget = (ScrollView) findViewById(R.id.scrollView);
        LogWidget.setBackgroundColor(Color.rgb(34, 58, 95));
        LogWidget.setMinimumWidth(screenWidth);

        Buttons2 = (LinearLayout) findViewById(R.id.buttons2);
        Buttons2.setBackgroundColor(Color.rgb(192, 192, 192));
        Buttons2.setMinimumWidth(screenWidth);

        final Button btnHelp = (Button) findViewById(R.id.help);
        btnHelp.setBackgroundColor(Color.rgb(90, 90, 90));
        btnHelp.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnHelp.setBackgroundColor(Color.rgb(90, 90, 90));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnHelp.setBackgroundColor(Color.rgb(128, 128, 128));
                }
                return false;
            }

        });

        final Button btnLog = (Button) findViewById(R.id.log);
        btnLog.setBackgroundColor(Color.rgb(90, 90, 90));
        btnLog.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnLog.setBackgroundColor(Color.rgb(90, 90, 90));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnLog.setBackgroundColor(Color.rgb(128, 128, 128));
                }
                return false;
            }

        });

        final Button btnMainw = (Button) findViewById(R.id.mainw);
        btnMainw.setBackgroundColor(Color.rgb(90, 90, 90));
        btnMainw.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnMainw.setBackgroundColor(Color.rgb(90, 90, 90));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnMainw.setBackgroundColor(Color.rgb(128, 128, 128));
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

        tvInfo.setText(fb.log);

        h = new Handler(hc);

        /**
         * получили указатель на обработчик сообщений сразу же говорим FotoBot'у об этом
         */
        fb.h = h;

    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MainActivity: onDestroy");
    }

    protected void onPause() {
        super.onPause();
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

// Wakelock 4
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");

        wakeLock.acquire();

        Log.d(LOG_TAG, "MainActivity: onResume");

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
            btnStart.setText(getResources().getString(R.string.start_button));
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);

            Button btnHelp = (Button) findViewById(R.id.help);
            btnHelp.setBackgroundColor(Color.rgb(90, 90, 90));
            btnHelp.setEnabled(true);

            Button btnLog = (Button) findViewById(R.id.log);
            btnLog.setBackgroundColor(Color.rgb(90, 90, 90));
            btnLog.setEnabled(true);

            Button btnMainw = (Button) findViewById(R.id.mainw);
            btnMainw.setBackgroundColor(Color.rgb(90, 90, 90));
            btnMainw.setEnabled(true);

        }

        if (fb.getstatus() == 2) {
            btnStart.setText(getResources().getString(R.string.start_button));
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);

            Button btnHelp = (Button) findViewById(R.id.help);
            btnHelp.setBackgroundColor(Color.rgb(90, 90, 90));
            btnHelp.setEnabled(false);

            Button btnLog = (Button) findViewById(R.id.log);
            btnLog.setBackgroundColor(Color.rgb(90, 90, 90));
            btnLog.setEnabled(false);

            Button btnMainw = (Button) findViewById(R.id.mainw);
            btnMainw.setBackgroundColor(Color.rgb(90, 90, 90));
            btnMainw.setEnabled(false);

        }

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "MainActivity: onSaveInstanceState");
    }

    protected void onStart() {

        super.onStart();
        //  Log.d(LOG_TAG, "\n\n\n\n\nMainActivity: onStart\n\n\n\n\n");

        final FotoBot fb = (FotoBot) getApplicationContext();

        try {
            fb.versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (!fb.init_logger) {

            Log.d(LOG_TAG, "fb.init_logger");

            fb.logger = Logger.getLogger(FotoBot.class.getName());

            fb.logpath = getFilesDir().toString() + "/";

            File file = new File(fb.logpath + "fblog.txt");

            if (!file.exists()) {
                Log.d(LOG_TAG, "fblog.txt doesn't exist");
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(LOG_TAG, "fblog.txt created");
            }

            try {
                fb.fh = new FileHandler(fb.logpath + "fblog.txt", fb.floglength, 1, true);
                Log.d(LOG_TAG, "handler created");

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "handler is not created");
            }

            fb.fh.setFormatter(new MyCustomFormatter());
            Log.d(LOG_TAG, "formatter created");

            fb.logger.addHandler(fb.fh);
            Log.d(LOG_TAG, "handler added");
            fb.logger.setLevel(Level.FINE);

            fb.logger.finest("Logger has been initialised.");
            Log.d(LOG_TAG, "Logger has been initialised.");
            fb.init_logger = true;
        }

    }

    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "MainActivity: onStop");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

        if (!fb.init_logger) {

            Log.d(LOG_TAG, "fb.init_logger");
            fb.logger = Logger.getLogger(FotoBot.class.getName());
            fb.logpath = getFilesDir().toString() + "/";

            try {
                fb.fh = new FileHandler(fb.logpath + "fblog.txt", fb.floglength, 1, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            fb.fh.setFormatter(new MyCustomFormatter());
            fb.logger.addHandler(fb.fh);
            fb.logger.setLevel(Level.FINE);

            fb.logger.finest("Logger has been initialised.");

            fb.init_logger = true;
        }

        switch (v.getId()) {
            case R.id.play:
                btnStart.setEnabled(false);
                btnStart.setText(getResources().getString(R.string.start_button));
                btnStop = (Button) findViewById(R.id.stop);
                btnStop.setEnabled(true);
                btnConfig.setEnabled(false);

                Button btnHelp = (Button) findViewById(R.id.help);
                btnHelp.setBackgroundColor(Color.rgb(165, 165, 165));
                btnHelp.setEnabled(false);

                Button btnMainw = (Button) findViewById(R.id.mainw);
                btnMainw.setBackgroundColor(Color.rgb(165, 165, 165));
                btnMainw.setEnabled(false);

                Button btnLog = (Button) findViewById(R.id.log);
                btnLog.setBackgroundColor(Color.rgb(165, 165, 165));
                btnLog.setEnabled(false);

                Thread t = new Thread(new Runnable() {
                    public void run() {

// Wakelock 6
                        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                                "MyWakelockTag");

                        wakeLock.acquire();


                        fb.SendMessage(getResources().getString(R.string.start_message));

// Добавлено в Andorid 5. Без этого не работает. Не понятно, как раньше работало.
                        if (mCamera == null) {
                            mCamera = Camera.open();
                        }

                        fb.Camera_Properties = mCamera.getParameters().flatten();

                        Log.d(LOG_TAG, fb.Camera_Properties);

                        if (fb.Network_Connection_Method.contains("Method 1") && (Build.VERSION.SDK_INT <= 21)) {
                            fb.MakeInternetConnection(getApplicationContext(), h);
                        }

                        for (int i = 1; i <= 1000000000; i++) {

                            fb.Image_Index = i;
                            fb.SendMessage("n: " + fb.Image_Index);

                            if (preview_stopped) {

                                try {
                                    mCamera.setPreviewDisplay(fb.holder);
                                    mCamera.startPreview();
                                } catch (Exception e) {
                                    // fb.SendMessage("Problem with preview starting 1.");
                                }

                            }
// https://sohabr.net/habr/post/215693/
                            fb.batteryLevel();

                            if (fb.getstatus() == 3) {
                                if (mCamera != null) {
                                    mCamera.stopPreview();
                                    mCamera.setPreviewCallback(null);
                                    mCamera.release();
                                    mCamera = null;
                                }
                                fb.SendMessage(h, getResources().getString(R.string.stop_message));
                                return;
                            }

                            DateFormat df = new SimpleDateFormat("MM-dd-yy_HH-mm-ss-SSS");
                            fb.Image_Name = df.format(new Date()) + ".jpg";
                            fb.Image_Name_Full_Path = getFilesDir().toString() + "/" + fb.Image_Name;

                            fb.LoadData();

                            if ((fb.Network_Connection_Method.contains("Method 2")) && (Build.VERSION.SDK_INT <= 21)) {
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
                            String string = fb.Image_Size;
                            String[] parts = string.split("x");
                            String width = parts[0];
                            String height = parts[1];

                            if (mCamera == null) {
                                //     fb.SendMessage("Camera is not initialized.");
                                try {
                                    mCamera = Camera.open();
                                } catch (Exception e) {
                                      fb.SendMessage("Problem with camera initialisation.");
                                }
                            }
                            if (preview_stopped) {
                                //    fb.SendMessage("Preview is not started.");
                                try {
                                    mCamera.setPreviewDisplay(fb.holder);
                                    mCamera.startPreview();
                                } catch (Exception e) {
                                    fb.SendMessage("Problem with preview starting 2.");
                                }
                            }

                            if (fb.Photo_Post_Processing_Method.contains("Software")) {

                                Camera.Parameters params = mCamera.getParameters();
                                params.setPictureSize(Integer.parseInt(width), Integer.parseInt(height));
                                mCamera.setParameters(params);

                            }


                            mCamera.takePicture(null, null, mCall);

                            fb.SendMessage(getResources().getString(R.string.photo_has_been_taken));

                            fb.fbpause(h, 3);

                            if (fb.Photo_Post_Processing_Method.contains("Software")) {
                                fb.SendMessage(Integer.parseInt(width) + "x" + Integer.parseInt(height));
                            } else {
                                fb.SendMessage(fb.Image_Scale);
                            }

                            fb.SendMessage(getResources().getString(R.string.free_memory) + ": " + fb.freeMemory);

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

                            long start = System.currentTimeMillis();

                            fb.SendMail(h, fb.Image_Name_Full_Path);

                            long durationInMilliseconds = System.currentTimeMillis() - start;

                            fb.email_sending_time = durationInMilliseconds / 1000;

                            if ((fb.Network_Connection_Method.contains("Method 2") && (Build.VERSION.SDK_INT <= 21))) {
                                fb.CloseInternetConnection(getApplicationContext(), h);
                            }

                            fb.SendMessage("-----------------------------");

                            fb.SendMessage(getResources().getString(R.string.pause_between_photos) + " " + fb.Photo_Frequency + "sec");

                            fb.SendMessage("-----------------------------");

                            fb.fbpause(h, fb.Photo_Frequency);

                            File imgfile = new File(fb.Image_Name_Full_Path);

                            if (imgfile.delete()) {
                                //     fb.SendMessage(fb.Image_Name + " was deleted");
                            } else {
                                //     fb.SendMessage(fb.Image_Name + " wasn't deleted");
                            }

                            if (mCamera != null) {

                                mCamera.stopPreview();
                                mCamera.setPreviewCallback(null);
                                mCamera.release();
                                mCamera = null;

                            }

                            preview_stopped = true;

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

        //     if (Build.VERSION.SDK_INT < 21 ) {
        //         fb.CloseInternetConnection(getApplicationContext(), h);
        //     }

        Log.d(LOG_TAG, "stopFotobot: fb.getstatus()" + fb.getstatus());
        fb.setstatus(3);
        Log.d(LOG_TAG, "stopFotobot: STOP_FOTOBOT" + STOP_FOTOBOT);

        btnStart.setText(getResources().getString(R.string.start_button));
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnConfig.setEnabled(true);

        Button btnHelp = (Button) findViewById(R.id.help);
        btnHelp.setBackgroundColor(Color.rgb(90, 90, 90));
        btnHelp.setEnabled(true);

        Button btnLog = (Button) findViewById(R.id.log);
        btnLog.setBackgroundColor(Color.rgb(90, 90, 90));
        btnLog.setEnabled(true);

        Button btnMainw = (Button) findViewById(R.id.log);
        btnMainw.setBackgroundColor(Color.rgb(90, 90, 90));
        btnMainw.setEnabled(true);

        if (fb.init_logger) {
            fb.fh.flush();
            fb.fh.close();
            fb.init_logger = false;
        }
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

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
        final FotoBot fb = (FotoBot) getApplicationContext();
        if (fb.init_logger) {
            fb.fh.flush();
            fb.fh.close();
            fb.init_logger = false;
        }
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

    private int calculateSignalStrengthInPercent(int signalStrength) {
        //return (int) ((float) signalStrength / MAX_SIGNAL_DBM_VALUE * 100);
        return (int) ((float) signalStrength);
    }

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

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            final FotoBot fb = (FotoBot) getApplicationContext();
            super.onSignalStrengthsChanged(signalStrength);

            if (null != signalStrength && signalStrength.getGsmSignalStrength() != UNKNOW_CODE) {
                int signalStrengthPercent = calculateSignalStrengthInPercent(signalStrength.getGsmSignalStrength());
                fb.GSM_Signal = calculateSignalStrengthInPercent(signalStrength.getGsmSignalStrength());

            }
        }
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
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
        LogWidget.setBackgroundColor(Color.rgb(34, 58, 95));

        tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Log_Font_Size);
        tvInfo.setTypeface(Typeface.MONOSPACE);
        tvInfo.setTextColor(Color.rgb(190, 190, 190));

        tvInfo.setText(fb.log);

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
            fileReader = new BufferedReader(new FileReader(getFilesDir().toString() + "/fblog.txt"));
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

        String contentsOfFile = "Fotobot's log" + strBuilder.toString();

        LogWidget = (ScrollView) findViewById(R.id.scrollView);
        LogWidget.setBackgroundColor(Color.rgb(54, 54, 54));

        tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Log_Font_Size);
        tvInfo.setTypeface(Typeface.MONOSPACE);
        tvInfo.setTextColor(Color.rgb(190, 190, 190));

        tvInfo.setText(contentsOfFile);

        Log.d(LOG_TAG, "reverse: " + contentsOfFile);

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

    }

   /* private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int  health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            int  icon_small= intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0);
            int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            int  plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
            boolean  present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
            int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
            int  status= intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
            String  technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            int  temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
            int  voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);

        }
    }; */

}
