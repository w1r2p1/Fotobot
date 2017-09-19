package com.droid.app.fotobot;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.BatteryManager;
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
import android.widget.Toast;

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
import java.util.List;
import java.util.concurrent.TimeUnit;

// commented to debug ffc
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    ActivityManager actvityManager;

    private int screenWidth, screenHeight;
    public static final int UNKNOW_CODE = 99;
    final String LOG_TAG = "Logs";
    final int STATUS_STOPPED = 333;

    private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
    private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
    private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;

    public boolean savedStreamMuted = false;

    int n;
    ScrollView LogWidget;
    RelativeLayout WorkSpace;
    boolean STOP_FOTOBOT = false;
    Button btnStart;
    Button btnStop;
    Handler h = null;
    TextView tvInfo;

    boolean preview_stopped = true;
    AudioManager mgr = null;

    int mainWindowColor = Color.rgb(0,0,0);
    int logWindowColor = Color.rgb(54,54,54);
    int helpWindowColor = Color.rgb(26, 54, 60);

    /*
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = new File("/storage/sdcard0/" + "img.jpg");

            if (pictureFile == null) {
                Log.d("TEST", "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("TEST", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("TEST", "Error accessing file: " + e.getMessage());
            }
        }
    };
*/
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

// string length

            if (fb.log.length() > fb.loglength) {
                fb.log = fb.log.substring(0, fb.loglength);
            }

            tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Log_Font_Size);
            tvInfo.setTypeface(Typeface.MONOSPACE);

            tvInfo.setTextColor(Color.rgb(140, 140, 140));
String status="";
            if (fb.success_message) {
                fb.log = reportDate + ": " + "<font color=aqua><b>" + message + "</b></font>" + "<br><br>" + fb.log;
                status = "<font color=green>Ok</font>";
            } else if (fb.error_message) {
                fb.log = reportDate + ": " + "<font color=red><b>" + message + "</b></font>" + "<br><br>" + fb.log;
                status = "<font color=red>Error</font>";
            } else {
                fb.log = reportDate + ": " + message + "[" + status + "]" + "<br><br>" + fb.log;
            }

            Log.d(LOG_TAG, reportDate + ": " + message);

            tvInfo.setText(Html.fromHtml(fb.log));

            fb.success_message = false;
            fb.debug_message = false;
            fb.error_message = false;

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

        actvityManager = (ActivityManager)
                this.getSystemService(ACTIVITY_SERVICE);

        final FotoBot fb = (FotoBot) getApplicationContext();

        fb.LoadSettings();

        if (fb.launched_first_time) {
            fb.set_default_storage();
            fb.launched_first_time = false;

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("Launched_First_Time", fb.launched_first_time);
            editor.commit();
        }

        PackageManager pm = getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
            fb.autofocus = true;
            Log.d(LOG_TAG, "Autofocus is available");
        } else {
            fb.autofocus = false;
            Log.d(LOG_TAG, "Autofocus is not available");
        }

        fb.work_dir_init();

        if (fb.show_start_tip) {
            String str = getResources().getString(R.string.Fotobot);
            fb.log = Html.fromHtml(str).toString();

            fb.show_start_tip = false;

            fb.SaveSettings();

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
//        WorkSpace.setBackgroundColor(Color.rgb(64, 98, 125));
        WorkSpace.setBackgroundColor(Color.rgb(0, 0, 128));
//        WorkSpace.setBackgroundColor(Color.rgb(0, 0, 0));
        WorkSpace.setMinimumHeight(screenHeight);
        WorkSpace.setMinimumWidth(screenWidth);

        LogWidget = (ScrollView) findViewById(R.id.scrollView);
        // LogWidget.setBackgroundColor(Color.rgb(34, 58, 95));
//        LogWidget.setBackgroundColor(Color.rgb(51, 51, 51));
        LogWidget.setBackgroundColor(mainWindowColor);
        LogWidget.setMinimumWidth(screenWidth);


        Button startButton;

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

        h = new Handler(hc);

        /**
         * получили указатель на обработчик сообщений сразу же говорим FotoBot'у об этом
         */
        fb.h = h;

        tvInfo.setText(Html.fromHtml(fb.log));


        if (fb.clean_log) {
            fb.log = "";
            fb.clean_log = false;
            tvInfo.setText(Html.fromHtml(fb.log));
            Log.d(LOG_TAG, "************************************************************************************* clean log");
        } else if (!fb.Tab_Foto_Activity_activated && !fb.Tab_Main_Activity_activated && !fb.Tab_Network_Activity_activated && !fb.Tab_Video_Activity_activated) {
            tvInfo.setText(Html.fromHtml(fb.log));
            fb.SendMessage(getString(R.string.update));
        }

        Button button = (Button) findViewById(R.id.log);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "создание отчета может потребовать 5 - 15 сек", Toast.LENGTH_LONG).show();

                final FotoBot fb = (FotoBot) getApplicationContext();

                File logfile = new File(fb.work_dir + "/logfile.txt");

                if (logcat2file()) {
                    //fb.SendMessage("Заполнили файл данными из logcat");
                } else {
                    fb.SendMessage("Проблема с доступом к logcat");
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
                Toast.makeText(MainActivity.this, "вывод системного журнала на экран завершен ", Toast.LENGTH_LONG).show();
            }


        });

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

        registerReceiver(onBatteryChanged,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }

    protected void onStop() {
        final FotoBot fb = (FotoBot) getApplicationContext();
        super.onStop();
        Log.d(LOG_TAG, "MainActivity: onStop");
        unregisterReceiver(onBatteryChanged);
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


                        fb.success_message = true;
                        fb.log = "";
                        fb.SendMessage(getResources().getString(R.string.start_message));

                        if (fb.sound_mute) {
                            mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                            if (Build.VERSION.SDK_INT >= 23) {
                                mgr.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                                savedStreamMuted = true;
                            } else {
                                mgr.setStreamMute(AudioManager.STREAM_MUSIC, true);
                                mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                                mgr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                            }
                        }

                        int i = 0; //Image counter

                        while (true) {

// заполняем список активных процессов
                            getActiveProcesses();

// не было ли команды на остановку Fotobot'а
                            if (fb.getstatus() == 3) {
                                if (stopFotobot()) {
                                    return;
                                } else {
                                    fb.SendMessage("Проблема с остановкой Fotobot'а");
                                }

                            }

// method1 соединяемся с сетью
                            if (fb.network && !(fb.Method1_activated)) {
                                if (fb.Network_Connection_Method.contains("Method 1")) {
                                    if (android.os.Build.VERSION.SDK_INT <= 21) {
                                        fb.MakeInternetConnection();
                                    }
                                    fb.Method1_activated = true;
                                }
                            }

// не было ли команды на остановку Fotobot'а
                            if (fb.getstatus() == 3) {
                                if (stopFotobot()) {
                                    return;
                                } else {
                                    fb.SendMessage("Проблема с остановкой Fotobot'а");
                                }

                            }

// method1 рарзрываем соединение с сетью
                            if (!(fb.network) && fb.Method1_activated) {
                                if (fb.Network_Connection_Method.contains("Method 1")) {
                                    if (android.os.Build.VERSION.SDK_INT <= 21) {
                                        fb.CloseInternetConnection();
                                    }
                                    fb.Method1_activated = false;
                                }
                            }

// не было ли команды на остановку Fotobot'а
                            if (fb.getstatus() == 3) {
                                if (stopFotobot()) {
                                    return;
                                } else {
                                    fb.SendMessage("Проблема с остановкой Fotobot'а");
                                }

                            }

// загружаем настройки из реестра
                            fb.LoadSettings();

// на каждом шаге удаляем логфайл, чтобы не забить память
                            deleteLogFile();

// счетчик кадров
                            fb.Image_Index = i + 1;

// на каждом шаге измеряем уровень заряда батареии
                            fb.batteryLevel();

// соединяемся с сетью если Android < 5
                            if (fb.network) {
                                if ((fb.Network_Connection_Method.contains("Method 2")) && (Build.VERSION.SDK_INT <= 21)) {
                                    fb.MakeInternetConnection();
                                }
                            }

// не было ли команды на остановку Fotobot'а
                            if (fb.getstatus() == 3) {
                                if (stopFotobot()) {
                                    return;
                                } else {
                                    fb.SendMessage("Проблема с остановкой Fotobot'а");
                                }

                            }

// делаем фото и видео камерой на задней панели телефона
                            if (fb.back_camera && fb.make_photo_bc) {
                                makePhoto("Bc");
                                fb.fbpause(h, 3);
                            }

// не было ли команды на остановку Fotobot'а
                            if (fb.getstatus() == 3) {
                                if (stopFotobot()) {
                                    return;
                                } else {
                                    fb.SendMessage("Проблема с остановкой Fotobot'а");
                                }

                            }

                            if (fb.front_camera && fb.make_photo_fc) {
                                makePhoto("Fc");
                                fb.fbpause(h, 3);
                            }

// не было ли команды на остановку Fotobot'а
                            if (fb.getstatus() == 3) {
                                if (stopFotobot()) {
                                    return;
                                } else {
                                    fb.SendMessage("Проблема с остановкой Fotobot'а");
                                }

                            }

                            if (fb.back_camera && fb.make_video_bc) {
                                makeVideo("Bc");
                                fb.fbpause(h, 3);
                            }

// не было ли команды на остановку Fotobot'а
                            if (fb.getstatus() == 3) {
                                if (stopFotobot()) {
                                    return;
                                } else {
                                    fb.SendMessage("Проблема с остановкой Fotobot'а");
                                }

                            }

                            if (fb.front_camera && fb.make_video_fc) {
                                makeVideo("Fc");
                            }

// не было ли команды на остановку Fotobot'а
                            if (fb.getstatus() == 3) {
                                if (stopFotobot()) {
                                    return;
                                } else {
                                    fb.SendMessage("Проблема с остановкой Fotobot'а");
                                }

                            }

// засекаем время для отправки письма
                            long start = System.currentTimeMillis();

// готовим лог к отправке
                            if (fb.attach_log && fb.network) {

                                if (logcat2file()) {

                                } else {
                                    fb.error_message = true;
                                    fb.SendMessage("Проблема с доступом к logcat");
                                }

                            }

// отправляем письмо с фото и видео
                            if (fb.network) {
                                fb.fbpause(h, 1);

                                if (fb.useMail) {
                                    fb.SendMail(h, fb.bc_Image_Name_Full_Path, fb.fc_Image_Name_Full_Path, fb.bc_Video_Name_Full_Path, fb.fc_Video_Name_Full_Path);
                                }

                                if (fb.useFTP) {
                                    if (fb.make_photo_bc && fb.bc_image_attach) {
                                        fb.FTPUpload(fb.bc_Image_Name_Full_Path);
                                    }

                                    if (fb.make_photo_fc && fb.fc_image_attach) {
                                        fb.FTPUpload(fb.fc_Image_Name_Full_Path);
                                    }

                                    if (fb.make_video_bc && fb.bc_video_attach) {
                                        fb.FTPUpload(fb.bc_Video_Name_Full_Path);
                                    }

                                    if (fb.make_video_fc && fb.fc_video_attach) {
                                        fb.FTPUpload(fb.fc_Video_Name_Full_Path);
                                    }

                                    if (fb.attach_log) {
                                        fb.FTPUpload(fb.work_dir + "/logfile.txt");
                                    }

                                }

                                long durationInMilliseconds = System.currentTimeMillis() - start;

                                fb.email_sending_time = durationInMilliseconds / 1000;

// если используется метод 2, то отсоединяемся от сети в конце шага
                                if ((fb.Network_Connection_Method.contains("Method 2") && (Build.VERSION.SDK_INT <= 21))) {
                                    fb.CloseInternetConnection();
                                }
                            }

                            fb.SendMessage("------------------------");
                            fb.SendMessage(getResources().getString(R.string.pause_between_photos) + " " + fb.Photo_Frequency + "sec");
                            fb.SendMessage("------------------------");
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


// если размер лога превышает 50 kb, то чистим его
                            cleanLogFile();

// удаляем фото с телефона
                          //  if (fb.delete_foto) {
                                deletePhoto();
                                deleteVideo();
                          //  }

                            fb.fbpause(h, fb.Photo_Frequency);
                            fb.sms_check_file = false;
                            fb.frame_delay = false;

                            i = i + 1;

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

        if (fb.sound_mute) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (savedStreamMuted) {
                    mgr.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
                    savedStreamMuted = false;
                }
            } else {
                mgr.setStreamMute(AudioManager.STREAM_MUSIC, false);
                mgr.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                mgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        final FotoBot fb = (FotoBot) getApplicationContext();

        Camera.Parameters params;


        fb.numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo ci = new Camera.CameraInfo();

        for (int i = 0; i < fb.numberOfCameras; i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                fb.fcId = i;
                fb.front_camera = true;
                mCamera = Camera.open(fb.fcId);

                fb.fc_Camera_Properties = mCamera.getParameters().flatten();

                params = mCamera.getParameters();
                fb.fc_camera_resolutions = params.getSupportedPictureSizes();

                getFcAvailableVideoProfiles();

                mCamera.release();
                mCamera = null;
            }
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                fb.bcId = i;
                mCamera = Camera.open(fb.bcId);

                fb.Camera_Properties = mCamera.getParameters().flatten();

                params = mCamera.getParameters();
                fb.camera_resolutions = params.getSupportedPictureSizes();

                getBcAvailableVideoProfiles();

                mCamera.release();
                mCamera = null;

            }
        }

        fb.holder = holder;

        mUnexpectedTerminationHelper.init();

        if (fb.automatic_mode && !fb.Tab_Foto_Activity_activated && !fb.Tab_Main_Activity_activated && !fb.Tab_Network_Activity_activated && !fb.Tab_Video_Activity_activated) {
            Button button = (Button)findViewById(R.id.play);
            fb.fbpause(h,1);
            button.performClick();
        }


// adopted for ffc

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void releaseCamera() {

        final FotoBot fb = (FotoBot) getApplicationContext();

        if (mCamera != null) {
            if (!preview_stopped) {
                try {
                    mCamera.stopPreview();
                } catch (Exception e) {
                    fb.error_message = true;
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

//        LogWidget.setBackgroundColor(Color.rgb(51, 51, 51));
        LogWidget.setBackgroundColor(mainWindowColor);

        tvInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Log_Font_Size);
        tvInfo.setTypeface(Typeface.MONOSPACE);
        tvInfo.setTextColor(Color.rgb(190, 190, 190));

        tvInfo.setText(Html.fromHtml(fb.log));

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
        LogWidget.setBackgroundColor(Color.rgb(0,0,128));

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

        Toast.makeText(this, "generating report", Toast.LENGTH_LONG).show();

        File logfile = new File(fb.work_dir + "/logfile.txt");

        if (logcat2file()) {
            //fb.SendMessage("Заполнили файл данными из logcat");
        } else {
            fb.SendMessage("Проблема с доступом к logcat");
        }

        //       fb.fbpause(fb.h, 1);

        LogWidget = (ScrollView) findViewById(R.id.scrollView);
        LogWidget.setBackgroundColor(Color.rgb(0,0,128));

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
        //   fb.Show_Help = true;


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
        LogWidget.setBackgroundColor(helpWindowColor);

        tvInfo.setText(Html.fromHtml("<h1>Fotobot " + fb.versionName + "</h1>" + str));

        findViewById(R.id.play).setEnabled(false);
        findViewById(R.id.stop).setEnabled(false);

    }

    public void clearLog() {
        try {
            Process process = new ProcessBuilder()
                    .command("logcat", "-c")
                    .redirectErrorStream(true)
                    .start();
        } catch (IOException e) {
        }
    }

    static String getExternalStorage() {
        String exts = Environment.getExternalStorageDirectory().getPath();
        try {
            FileReader fr = new FileReader(new File("/proc/mounts"));
            BufferedReader br = new BufferedReader(fr);
            String sdCard = null;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("secure") || line.contains("asec")) continue;
                if (line.contains("fat")) {
                    String[] pars = line.split("\\s");
                    if (pars.length < 2) continue;
                    if (pars[1].equals(exts)) continue;
                    sdCard = pars[1];
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


    public void getBcAvailableVideoProfiles() {

        final FotoBot fb = (FotoBot) getApplicationContext();

        CamcorderProfile profile;

        if (!fb.bc_video_profile_initialized) {

            try {
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
                fb.bc_video_profile.add("QUALITY_HIGH");
            } catch (Exception e) {
            }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_2160P);
            fb.bc_video_profile.add("QUALITY_2160P");
        } catch (Exception e) {
        }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P);
            fb.bc_video_profile.add("QUALITY_1080P");
        } catch (Exception e) {
        }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
            fb.bc_video_profile.add("QUALITY_720P");
        } catch (Exception e) {
        }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
            fb.bc_video_profile.add("QUALITY_480P");
        } catch (Exception e) {
        }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_CIF);
            fb.bc_video_profile.add("QUALITY_CIF");
        } catch (Exception e) {
        }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA);
            fb.bc_video_profile.add("QUALITY_QVGA");
        } catch (Exception e) {
        }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_QCIF);
            fb.bc_video_profile.add("QUALITY_QCIF");
        } catch (Exception e) {
        }

            try {
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
                fb.bc_video_profile.add("QUALITY_LOW");
            } catch (Exception e) {
            }
        fb.bc_video_profile_initialized = true;
        }
    }

    public void getFcAvailableVideoProfiles() {

        final FotoBot fb = (FotoBot) getApplicationContext();

        CamcorderProfile profile;

        if (!fb.fc_video_profile_initialized) {

            try {
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
                fb.fc_video_profile.add("QUALITY_HIGH");
            } catch (Exception e) {
            }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_2160P);
            fb.fc_video_profile.add("QUALITY_2160P");
        } catch (Exception e) {
        }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P);
            fb.fc_video_profile.add("QUALITY_1080P");
        } catch (Exception e) {
        }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
            fb.fc_video_profile.add("QUALITY_720P");
        } catch (Exception e) {
        }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
            fb.fc_video_profile.add("QUALITY_480P");
        } catch (Exception e) {
        }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_CIF);
            fb.fc_video_profile.add("QUALITY_CIF");
        } catch (Exception e) {
        }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA);
            fb.fc_video_profile.add("QUALITY_QVGA");
        } catch (Exception e) {
        }

        try {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_QCIF);
            fb.fc_video_profile.add("QUALITY_QCIF");
        } catch (Exception e) {
        }

            try {
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
                fb.fc_video_profile.add("QUALITY_LOW");
            } catch (Exception e) {
            }
        fb.fc_video_profile_initialized = true;
        }
    }

    public void getActiveProcesses() {
        final FotoBot fb = (FotoBot) getApplicationContext();

        List<ActivityManager.RunningAppProcessInfo> procInfos = actvityManager.getRunningAppProcesses();

        for (int idx = 0; idx < procInfos.size(); idx++) {
            fb.Top = fb.Top + procInfos.get(idx).processName + "\n";
        }
    }

    public void deleteLogFile() {
        final FotoBot fb = (FotoBot) getApplicationContext();

        File log_file = null;

        log_file = new File((fb.work_dir + "/logfile.txt"));

        if (log_file.isFile()) {
            log_file.delete();
            Log.d(LOG_TAG, "logcat file has been deleted");
        }
    }

    public boolean stopFotobot() {
        final FotoBot fb = (FotoBot) getApplicationContext();

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }

        fb.thread_stopped = true;
        fb.debug_message = true;
        fb.SendMessage(h, getResources().getString(R.string.stop_message));
        return true;
    }

    public void makePhoto(String cameraType) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        String width;
        String height;

        int cameraId = -1;

        String str="";

        if (cameraType.equals("Bc")) {
            cameraId = fb.bcId;
            str = "back_cam";
        } else {
            cameraId = fb.fcId;
            str = "front_cam";
        }

        fb.SendMessage(str + ": " + getResources().getString(R.string.starting_to_make_photo) + " " + fb.Image_Index);

        buildImageName(cameraType);

        fb.LoadSettings();

// set camera resolution
        if (cameraType.equals("Bc")) {
            String[] parts = fb.Image_Size.split("x");
            width = parts[0];
            height = parts[1];
        } else {
            String[] parts = fb.fc_Image_Size.split("x");
            width = parts[0];
            height = parts[1];
        }

// Step 1
        if (mCamera == null) {
            try {
                mCamera = Camera.open(cameraId);
            } catch (Exception e) {
                fb.error_message = true;
                fb.SendMessage("Проблема с доступом к " + cameraType + " камере\n\n\n" + e.toString());
            }
        }

// Step 2
        Camera.Parameters parameters = mCamera.getParameters();

        if (fb.Use_Flash && cameraType.equals("Bc")) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        }

        if (fb.Photo_Post_Processing_Method.contains("Software")) {
            parameters.setPictureSize(Integer.parseInt(width), Integer.parseInt(height));
        }

        try {
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            fb.error_message = true;
            fb.SendMessage("Проблема с установкой параметров для " + cameraType + " камеры\n\n\n" + e.toString());
        }

// Step 3

        if (preview_stopped) {
            try {
                mCamera.setPreviewDisplay(fb.holder);
                mCamera.startPreview();

                if (fb.autofocus && fb.use_autofocus && cameraType.equals("Bc")) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        public void onAutoFocus(boolean success, Camera camera) {

                        }
                    });

                    fb.fbpause(h, fb.time_for_focusing);

                }

                preview_stopped = false;
            } catch (Exception e) {
                fb.error_message = true;
                fb.SendMessage("Проблема запуска preview для " + cameraType + " камеры\n\n\n" + e.toString());

            }
        }

// Step 4
        try {
            mCamera.takePicture(null, null, mCall);
            fb.success_message = true;
            fb.SendMessage(getResources().getString(R.string.photo_has_been_taken));
        } catch (Exception e) {
            fb.error_message = true;
            fb.SendMessage("Проблема с takePicture для " + cameraType + " камеры");
        }

        fb.fbpause(h, 3);

// Step 5
        if (!preview_stopped) {
            mCamera.stopPreview();
            preview_stopped = true;
        }

        if (fb.Use_Flash && cameraType.equals("Bc")) {
            parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

            try {
                mCamera.setParameters(parameters);
            } catch (Exception e) {
                fb.error_message = true;
                fb.SendMessage("Проблема выключения вспышки для " + cameraType + " камеры\n\n\n" + e.toString());
            }
        }

        if (fb.autofocus && fb.use_autofocus && cameraType.equals("Bc")) {

            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);

            try {
                mCamera.setParameters(parameters);
            } catch (Exception e) {
                fb.error_message = true;
                fb.SendMessage("Проблема с установкой фиксированного фокуса для " + cameraType + " камеры\n\n\n" + e.toString());
            }
        }

        if (mCamera != null) {
            try {
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                fb.SendMessage("ERROR: mCamera.release() " + e.toString());
            }
        }
    }


    public void makeVideo(String cameraType) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        Camera.Parameters save_camera_parameters = null;
        int cameraId = -1;

        String str = "";

        if (cameraType.equals("Bc")) {
            cameraId = fb.bcId;
            str = "back_cam";
        } else {
            cameraId = fb.fcId;
            str = "front_cam";
        }

        fb.SendMessage(str + ": " + getResources().getString(R.string.starting_to_make_video) + " " + fb.Image_Index);

        buildVideoName(cameraType);

        fb.LoadSettings();

// Step 1
        if (mCamera == null) {
            try {
                mCamera = Camera.open(cameraId);
               // fb.SendMessage(cameraType + " камера успешно открыта");
            } catch (Exception e) {
                fb.error_message = true;
                fb.SendMessage("Проблема с доступом к " + cameraType + " камере\n\n\n" + e.toString());
            }
        }

// DEBUG
// стартуем preview и сразу же останавливаем
/*        fb.fbpause(h,1);
        if (preview_stopped) {
            fb.SendMessage("на данный момент preview не запущено, поэтому запускаем preview");
            try {
                mCamera.setPreviewDisplay(fb.holder);
                mCamera.startPreview();
                preview_stopped = false;
                fb.SendMessage("preview запущено");
            } catch (Exception e) {
                fb.error_message = true;
                fb.SendMessage("Проблема запуска preview для " + cameraType + " камеры\n\n\n" + e.toString());
            }
        }

        fb.fbpause(h,1);
        if (!preview_stopped) {
            fb.SendMessage("preview уже запущено, пробуем его остановить");
            try {
                mCamera.stopPreview();
                preview_stopped = true;
                fb.SendMessage("preview остановлено");
            } catch (Exception e){
                fb.SendMessage("Проблема с остановкой preview " + e.toString());
            }
        }*/
// END DEBUG

        MediaRecorder mMediaRecorder = new MediaRecorder();

        mCamera.unlock();

        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override

            public void onInfo(MediaRecorder mediaRecorder, int what, int extra) {
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                   // fb.SendMessage("MEDIA_RECORDER_INFO_MAX_DURATION_REACHED");
                   //     mediaRecorder.stop();
                }
            }
        });

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

       if (cameraType.equals("Bc")) {
          // try {
               if (fb.bc_current_video_profile.contains("QUALITY_HIGH")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
               }
               if (fb.bc_current_video_profile.contains("QUALITY_2160P")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_2160P));
               }
               if (fb.bc_current_video_profile.contains("QUALITY_1080P")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));
               }
               if (fb.bc_current_video_profile.contains("QUALITY_720P")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
               }
               if (fb.bc_current_video_profile.contains("QUALITY_480P")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
               }
               if (fb.bc_current_video_profile.contains("QUALITY_CIF")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_CIF));
               }
               if (fb.bc_current_video_profile.contains("QUALITY_QCIF")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QCIF));
               }
               if (fb.bc_current_video_profile.contains("QUALITY_QVGA")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA));
               }
               if (fb.bc_current_video_profile.contains("QUALITY_LOW")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
               }
         //  } catch (Exception e){
         //      fb.error_message = true;
        //       fb.SendMessage(str + " не поддерживает профиль " + fb.fc_current_video_profile + ", пожалуйста выберите другой видеопрофиль для этой камеры.");
        ///   }


        } else {
         //  try {
               if (fb.fc_current_video_profile.contains("QUALITY_HIGH")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
               }
               if (fb.fc_current_video_profile.contains("QUALITY_2160P")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_2160P));
               }
               if (fb.fc_current_video_profile.contains("QUALITY_1080P")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));
               }
               if (fb.fc_current_video_profile.contains("QUALITY_720P")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
               }
               if (fb.fc_current_video_profile.contains("QUALITY_480P")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
               }
               if (fb.fc_current_video_profile.contains("QUALITY_CIF")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_CIF));
               }
               if (fb.fc_current_video_profile.contains("QUALITY_QCIF")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QCIF));
               }
               if (fb.fc_current_video_profile.contains("QUALITY_QVGA")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA));
               }
               if (fb.fc_current_video_profile.contains("QUALITY_LOW")) {
                   mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
               }
           //} catch (Exception e){
             //  fb.error_message = true;
             //  fb.SendMessage(str + " не поддерживает профиль " + fb.fc_current_video_profile + ", пожалуйста выберите другой видеопрофиль для этой камеры.");
          // }
        }

      //  mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));

        if (cameraType.equals("Bc")) {
            mMediaRecorder.setOutputFile(fb.bc_Video_Name_Full_Path);
        } else {
           // fb.SendMessage("fc_Video_Name_Full_Path: " + fb.fc_Video_Name_Full_Path);
            mMediaRecorder.setOutputFile(fb.fc_Video_Name_Full_Path);
        }

        mMediaRecorder.setPreviewDisplay(fb.holder.getSurface());

        mMediaRecorder.setMaxDuration(fb.video_recording_time * 1000);

//DEBUG
       // mMediaRecorder.setVideoSize(352, 288);
     //   mMediaRecorder.setVideoFrameRate(30);
//END DEBUG

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {

            if (mMediaRecorder != null) {
                mMediaRecorder.reset();   // clear recorder configuration
                mMediaRecorder.release(); // release the recorder object
                mMediaRecorder = null;
            }

            //fb.error_message = true;
            //fb.SendMessage("Mediarecorder prepare problem\n\n\n" + e.toString());

        } catch (IOException e) {

            if (mMediaRecorder != null) {
                mMediaRecorder.reset();   // clear recorder configuration
                mMediaRecorder.release(); // release the recorder object
                mMediaRecorder = null;
            }

            fb.SendMessage("Mediarecorder prepare problem\n\n\n" + e.toString());
            fb.SendMessage(e.toString());

        }

        try {
            //  mCamera.unlock();                 здесь скорей всего надо проверять на версии Android
            mMediaRecorder.start();
            if (cameraType.equals("Bc")) {
                fb.SendMessage(getResources().getString(R.string.str_profile) + " " +  fb.bc_current_video_profile + " " + getResources().getString(R.string.str_supported) + " " + str + " " + getResources().getString(R.string.str_camera));
            } else {
                fb.SendMessage(getResources().getString(R.string.str_profile) + " " + fb.fc_current_video_profile + " " + getResources().getString(R.string.str_supported) + " " + str + " " + getResources().getString(R.string.str_camera));
            }
            try {
                TimeUnit.SECONDS.sleep(fb.video_recording_time  + 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            fb.error_message = true;
            fb.SendMessage(str + getResources().getString(R.string.str_is_not_supported) + " " + fb.fc_current_video_profile + " " + getResources().getString(R.string.str_please_select));
        }

        try {
            mMediaRecorder.stop();
            fb.success_message = true;
            fb.SendMessage(getResources().getString(R.string.video_recorded));
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
        } catch (Exception e) {
            fb.error_message = true;
            fb.SendMessage(getResources().getString(R.string.str_stop_video) + "\n" + e.toString());
        }

  /*      if (fb.autofocus && fb.use_autofocus) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        }

        try {
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            fb.error_message = true;
            fb.SendMessage("Проблема с восстановлением параметров для " + cameraType + " камеры\n\n\n" + e.toString());
        }*/

        if (mCamera != null) {
            try {
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                fb.SendMessage("ERROR: mCamera.release() " + e.toString());
            }
        }

    }


    public void cleanLogFile() {
        final FotoBot fb = (FotoBot) getApplicationContext();

        File logcat_file;
        logcat_file = new File(fb.work_dir + "/logfile.txt");

        boolean fileExists = logcat_file.isFile();

        if (fileExists) {
            if (logcat_file.length() / 1000 > fb.log_size) {
                clearLog();
            }
        } else {
            // fb.SendMessage(h, "logfile.txt doesn't exist.");
        }
    }


    public void deletePhoto() {
        final FotoBot fb = (FotoBot) getApplicationContext();

        if (fb.make_photo_bc && fb.bc_image_delete) {
            File imgfile = new File(fb.bc_Image_Name_Full_Path);

            if (imgfile.delete()) {
                fb.SendMessage(getResources().getString(R.string.str_file) + " " + fb.bc_Image_Name + " " + getResources().getString(R.string.str_was_deleted));
            } else {
                fb.error_message = true;
                fb.SendMessage(getResources().getString(R.string.str_problem_with_deleting) + " " + fb.bc_Image_Name);
            }
        }
        if (fb.make_photo_fc && fb.fc_image_delete) {
            File fc_imgfile = new File(fb.fc_Image_Name_Full_Path);

            if (fc_imgfile.delete()) {
                fb.SendMessage(getResources().getString(R.string.str_file) + " " + fb.fc_Image_Name + " " + getResources().getString(R.string.str_was_deleted));
            } else {
                fb.error_message = true;
                fb.SendMessage(getResources().getString(R.string.str_problem_with_deleting) + " " + fb.fc_Image_Name);
            }
        }
    }

    public void deleteVideo() {
        final FotoBot fb = (FotoBot) getApplicationContext();

        if (fb.make_video_bc && fb.bc_video_delete) {
            File videofile = new File(fb.bc_Video_Name_Full_Path);

            if (videofile.delete()) {
                fb.SendMessage(getResources().getString(R.string.str_file) + " " + fb.bc_Video_Name_Full_Path + " " + getResources().getString(R.string.str_was_deleted));
            } else {
                fb.error_message = true;
                fb.SendMessage(getResources().getString(R.string.str_problem_with_deleting) + " " + fb.bc_Video_Name_Full_Path);
            }
        }
        if (fb.make_video_fc && fb.fc_video_delete) {
            File fc_videofile = new File(fb.fc_Video_Name_Full_Path);

            if (fc_videofile.delete()) {
                fb.SendMessage(getResources().getString(R.string.str_file) + " " + fb.fc_Video_Name_Full_Path + " " + getResources().getString(R.string.str_was_deleted));
            } else {
                fb.error_message = true;
                fb.SendMessage(getResources().getString(R.string.str_problem_with_deleting) + " " + fb.fc_Video_Name_Full_Path);
            }
        }
    }


    public void buildImageName(String cameraType) {
        final FotoBot fb = (FotoBot) getApplicationContext();

        DateFormat df = new SimpleDateFormat("MM-dd-yy_HH-mm-ss-SSS");

        if (cameraType.equals("Bc")) {
            fb.bc_Image_Name = df.format(new Date()) + ".jpg";
            fb.bc_Image_Name_Full_Path = fb.work_dir + "/" + fb.bc_Image_Name;
            fb.Image_Name = fb.bc_Image_Name;
        } else if (cameraType.equals("Fc")) {
            fb.fc_Image_Name = "fc_" + df.format(new Date()) + ".jpg";
            fb.fc_Image_Name_Full_Path = fb.work_dir + "/" + fb.fc_Image_Name;
            fb.Image_Name = fb.fc_Image_Name;
        }

    }

    public void buildVideoName(String cameraType) {
        final FotoBot fb = (FotoBot) getApplicationContext();

        DateFormat df = new SimpleDateFormat("MM-dd-yy_HH-mm-ss-SSS");

        if (cameraType.equals("Bc")) {
            fb.bc_Video_Name = df.format(new Date()) + ".mp4";
            fb.bc_Video_Name_Full_Path = fb.work_dir + "/" + fb.bc_Video_Name;
        } else if (cameraType.equals("Fc")) {
            fb.fc_Video_Name = "fc_" + df.format(new Date()) + ".mp4";
            fb.fc_Video_Name_Full_Path = fb.work_dir + "/" + fb.fc_Video_Name;
        }

    }

    BroadcastReceiver onBatteryChanged=new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final FotoBot fb = (FotoBot) getApplicationContext();



            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);

            // Are we charging / charged?
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            fb.isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
        }

    };

}
