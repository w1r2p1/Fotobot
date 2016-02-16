package com.droid.app.fotobot;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * <h1>FotoBot</h1>
 * Умеет делать фото и отправлять на почту.
 * Это глобальный класс, объект данного класса будет виден во всех активити. Инициализируется через Manifest.
 * Все переменные с которыми работают методы других классов собраны здесь.
 */
public class FotoBot extends Application {

    Camera camera = null;

    private final static Logger fblogger = Logger.getLogger(FotoBot.class.getName());

    public String versionName = "";

    public String Camera_Name = "";

    final String LOG_TAG = "Logs";

    /**
     * Интервал фотографирования (в секундах)
     */
    public int Photo_Frequency;

    /**
     * Нужно ли использовать Wi-Fi для выхода в Internet
     */
    public boolean Use_WiFi;

    /**
     * Нужно ли использовать 3G (2G) для выхода в Internet
     */
    public boolean Use_Mobile_Data;

    /**
     * Делать фото со вспышкой
     */
    public boolean Use_Flash;


    public int battery_level;


    /**
     * Степень JPEG сжатия
     */
    public int JPEG_Compression = 90;

    /**
     * Коэффициент масштабирования фото (1/4,1/2,1)
     */
    public String Image_Scale = "1/4";

    /**
     * Номер изображения в текущей сессии
     */
    public int Image_Index = 0;

    /**
     * Размер изображения в пикселях
     */
    public String Image_Size = "1024x768";

    /**
     * Размер изображения в килобайтах
     */
    public int File_Size;

    /**
     * вспомогательная почта для отправки писем
     */
    public String EMail_Sender = "user@gmail.com";

    /**
     * пароль для вспомогательной почты
     */
    public String EMail_Sender_Password = "passwd";

    /**
     * кому отправлять письма с фотками
     */
    public String EMail_Recepient = "recipient@mail.ru";

    public String SMTP_Host = "smtp.gmail.com";
    public String SMTP_Port = "465";

    public String Network_Channel = "Both";

    /**
     * Соединятся с Internet один раз (Method1) или на каждом шаге (Method2)
     */
    public String Network_Connection_Method = "Method 1";

    /**
     * Метод обработки фото (Hardaware или Software)
     */
    public String Photo_Post_Processing_Method = "Hardware";

    public int GSM_Signal = 0;

    List<Camera.Size> camera_resolutions;

    public int process_delay = 3;

    public int status = 1;

    public SurfaceHolder holder;

    public String str = "";

    public Handler h;

    public SurfaceHolder sHolder = null;

    public boolean frame_delay = false;

    /**
     * Размер шрифта в настройках (sp)
     */
    public int Config_Font_Size = 20;

    /**
     * Размер шрифта в логе (sp)
     */
    public int Log_Font_Size = 14;

    public int Working_Area_Height = 240;

    public int menuheight;

    public String Image_Name;

    public String Image_Name_Full_Path;

    /**
     * Длина лога в главном окне
     */
    public int loglength = 1024;

    /**
     * Длина лога в файле
     */
    public int floglength = 1024;

    /**
     * Частота с которой Fotobot обращается к камере и стартует preview,
     * необходима для того, чтобы приложение не было выброшено из памяти
     */
    public int wake_up_interval = 60;

    /**
     * Camera properties
     */
    public String Camera_Properties;

    /**
     * Документация
     */
    public String Main_Help = "<h1><font color=white>Фотобот<br>Руководство пользователя</font></h1>" +
            "Это простое Android приложение предназначено для фотографирования и отправки фото по электронной почте " +
            "через заданные промежутки времени." +
            "Все что нужно сделать - это завести почтовый ящик для Fotobot с которого он будет присылать фото." +
            "Подробную инструкцию по установке и настройке " +
            "этого приложения вы найдете на <a href=http://v-and-f.ru/fotobot>http://v-and-f.ru/fotobot</a>";

    /**
     * Если  Show_Help = false, то выводится timestamp для логов,
     * если  Show_Help = true, ты выводится документация.
     */
    public Boolean Show_Help = false;

    /**
     * Память в телефоне
     */
    public String freeMemory;
    public String totalMemory;
    public String usedMemory;

    /**
     * Батарейка
     */
    int battery_health;
    int battery_icon_small;
    int battery_charge;
    int battery_plugged;
    boolean battery_present;
    int battery_scale;
    int battery_status;
    String battery_technology;
    float battery_temperature;
    int battery_voltage;

    /**
     * Логфайл
     */
    public String logpath = "";
    public String logfile = "Fotobot.txt";

    Logger logger;

    FileHandler fh = null;

    boolean init_logger = false;

    /**
     * Строка на экран
     */
    public String log = "";

    public boolean clean_log = false;

    /**
     * Длительность отправки предыдущего письма
     */
    public long email_sending_time;

    /**
     * Возвращает текущее состояние FotoBot'а, сейчас не пользуюсь этим
     *
     * @return
     */
    public int getstatus() {

        return status;
    }

    /**
     * Устанавливает статус
     *
     * @param fb_status
     */
    public void setstatus(int fb_status) {

        status = fb_status;
    }

    public String getstr() {

        return str;
    }

    public void setstr(String fb_str) {

        str = fb_str;
    }

    /**
     * В конструкторе проводим инициализацию объекта посредством считывания всех свойств из SharedPreferences.
     */
    public void FotoBot() {

        LoadData();
    }

    public void Init() {

    }

    public void WriteData() {

    }

    /**
     * isOnline - Check if there is a NetworkConnection
     *
     * @return boolean
     */
    public boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            SendMessage(getResources().getString(R.string.Internet_connection_is_already_created));
            return true;
        } else {
            SendMessage(getResources().getString(R.string.no_Internet_connection));
            return false;
        }

    }

    /**
     * Для проверки соединения выкачивает страницу из Internet
     */

    public boolean getData() {

        BufferedReader rd = null;
        StringBuilder sb = null;
        String line = null;

        InputStream is = null;

        HttpURLConnection urlc = null;
        URL serverAddress = null;

        try {
            serverAddress = new URL("http://www.javatalks.ru"); // http:// required
            //set up out communications stuff
            urlc = null;

            //Set up the initial connection
            urlc = (HttpURLConnection) serverAddress.openConnection();
            urlc.setRequestMethod("GET");
            urlc.setDoOutput(true);
            urlc.setReadTimeout(60000);

            urlc.connect();
//read the result from the server
            rd = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            sb = new StringBuilder();

            while ((line = rd.readLine()) != null) {
                sb.append(line + '\n');
            }

            SendMessage("WEB page downloaded.");

            urlc.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String str = sw.toString();
            SendMessage(str);
            return false;
        } catch (ProtocolException e) {
            e.printStackTrace();
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            SendMessage(sw.toString().toUpperCase());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            SendMessage(sw.toString().toUpperCase());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            SendMessage(sw.toString().toUpperCase());
        } finally {
            //close the connection, set all objects to null
            //    urlc.disconnect();
            rd = null;
            sb = null;
            urlc = null;
        }

        return true;
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    /**
     * FotoBot умеет самостоятельно подключаться к Internet для отправки фото на почту
     *
     * @param context
     * @param h
     * @return
     */

    private boolean enable_WiFi() {

        try {

            WiFi wf = new WiFi();
            SendMessage(h, getResources().getString(R.string.turning_on_wifi));
            wf.setWiFiEnabled(getApplicationContext(), true);
            fbpause(h, 5);
            SendMessage(h, getResources().getString(R.string.turning_on_wifi_message));
            fbpause(h, 5);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return false;

    }

    private boolean enable_MobileData() {

        try {

            MobileData md = new MobileData();
            SendMessage(h, getResources().getString(R.string.turning_on_mobiledata));
            md.setMobileDataEnabled(getApplicationContext(), true);
            fbpause(h, 5);

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public boolean MakeInternetConnection() {

        int connect_attempt;

        for (connect_attempt = 0; connect_attempt < 3; connect_attempt++) {

            SendMessage("MakeInternetConnection attempt: " + (connect_attempt + 1));

            if (Network_Channel.contains("Wi-Fi")) {
                SendMessage(getResources().getString(R.string.connection_channel_wifi));
                enable_WiFi();
            }

        if (Network_Channel.contains("Mobile Data")) {
            SendMessage(getResources().getString(R.string.connection_channel_mobiledata));
            enable_MobileData();
        }

        if (Network_Channel.contains("Both")) {
            SendMessage(getResources().getString(R.string.connection_channel_wifimobiledata));

            if (enable_WiFi()) {
                SendMessage("Wi-Fi is up in both metod");
            } else if (enable_MobileData()) {
                SendMessage("MobileData is up in both metod");
            }

        }

        if (isOnline() && getData()) {
            SendMessage(getResources().getString(R.string.Internet_connection));
            return true;
        }

    }

        if ( connect_attempt == 2 ) {
            SendMessage("Exiting without connecting to Internet, photo will be taken in offline mode.");
        }

        return false;

    }

    /**
     * FotoBot может самостоятельно отключаться от Internet
     *
     * @param context
     * @param h
     */
    public void CloseInternetConnection() {

        try
        {
            MobileData md = new MobileData();
//Sony Xperia error
// http://stackoverflow.com/questions/29340150/android-l-5-x-turn-on-off-mobile-data-programmatically
            md.setMobileDataEnabled(getApplicationContext(), false);
        } catch (Exception e) {
            SendMessage("Couldn't turn off Mobile Data.");
        }

        try {
            WiFi wf = new WiFi();
            wf.setWiFiEnabled(getApplicationContext(), false);
            SendMessage(getResources().getString(R.string.Internet_connection_is_closed));
        } catch (Exception e) {
            SendMessage("Couldn't turn off WiFi.");
        }
    }

    /**
     * Делаем паузу и печатаем на экран точки, чтобы было понятно, что программа не зависла
     *
     * @param h
     * @param delay
     */
    public void fbpause(final Handler h, final int delay) {

        final String message;

        Thread thread = new Thread() {
            public void run() {
                for (int i = 1; i <= delay; i++) {
                    Message msg = Message.obtain(); // Creates an new Message instance
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (getstatus() == 3) {
                        return;
                    }

                    if (i % wake_up_interval == 0 && frame_delay) {

                        // SendMessage("wake up: " + i);
                        SendMessage(".");

                        if (camera == null) {
                            //SendMessage("fb.apuse: Camera is not initialized.");
                            SendMessage("..");
                            try {
                                camera = Camera.open();
                                //SendMessage("fb.pause: Camera has been initialized.");
                                SendMessage(".");
                            } catch (Exception e) {
                                //SendMessage("fb pause: Problem with camera initialisation.");
                                SendMessage("...");
                                e.printStackTrace();
                            }
                        }

                        try {
                            camera.setPreviewDisplay(holder);
                            camera.startPreview();
                            //SendMessage("fb.pause: startPreview");
                            SendMessage(".");
                        } catch (Exception e) {
                            e.printStackTrace();
                            //SendMessage("fb.pause: problem with starting of preview");
                            SendMessage("...");
                        }

                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (camera != null) {
                            try {
                                camera.stopPreview();
                                camera.release();
                                camera = null;
                            } catch (Exception e) {
                                SendMessage("...");
                            }

                        }

                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }

        };
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Печатаем сообщение на экран
     *
     * @param h   handler, который ловит сообщения
     * @param str текст сообщения
     */
    public void SendMessage(Handler h, String str) {

        Message msg = Message.obtain(); // Creates an new Message instance
        msg.obj = str; // Put the string into Message, into "obj" field.
        msg.setTarget(h); // Set the Handler
        msg.sendToTarget(); //Send the message

    }

    /**
     * Печатаем сообщение на экран
     *
     * @param str текст сообщения
     */
    public void SendMessage(String str) {

        Message msg = Message.obtain(); // Creates an new Message instance
        msg.obj = str; // Put the string into Message, into "obj" field.
        msg.setTarget(h); // Set the Handler
        msg.sendToTarget(); //Send the message

    }

    /**
     * Данный метод позволяет отправить письма с аттачем
     *
     * @param h
     * @param str
     */
    public void SendMail(Handler h, String str) {

        final FotoBot fb = (FotoBot) getApplicationContext();

        // SendMessage("Аттачим" + str);

        Mail m = new Mail(getApplicationContext(), fb.EMail_Sender, fb.EMail_Sender_Password, fb.SMTP_Host, fb.SMTP_Port);
        //Mail m = new Mail("fotobotmail@gmail.com", "fotobotmailpasswd", "smtp.gmail.com", "465");

        String[] toArr = {fb.EMail_Recepient};

        String s = "Debug-infos:";
        s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        s += "\n Device: " + android.os.Build.DEVICE;
        s += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";

        s += "\n RELEASE: " + android.os.Build.VERSION.RELEASE;
        s += "\n BRAND: " + android.os.Build.BRAND;
        s += "\n DISPLAY: " + android.os.Build.DISPLAY;
        s += "\n CPU_ABI: " + android.os.Build.CPU_ABI;
        s += "\n CPU_ABI2: " + android.os.Build.CPU_ABI2;
        s += "\n UNKNOWN: " + android.os.Build.UNKNOWN;
        s += "\n HARDWARE: " + android.os.Build.HARDWARE;
        s += "\n Build ID: " + android.os.Build.ID;
        s += "\n MANUFACTURER: " + android.os.Build.MANUFACTURER;
        s += "\n SERIAL: " + android.os.Build.SERIAL;
        s += "\n USER: " + android.os.Build.USER;
        s += "\n HOST: " + android.os.Build.HOST;

        m.setTo(toArr);
        m.setFrom(fb.EMail_Sender);
        m.setSubject("Fotobot v" + versionName + " " + fb.Camera_Name);
        m.setBody("Fotobot v" + versionName + "\n" +
                "---------------------------------------------\n" +
                "Camera Name" + ": " + fb.Camera_Name + "\n" +
                getResources().getString(R.string.battery_charge) + ": " + fb.battery_level + "%" + "\n" +
                getResources().getString(R.string.battery_temperature) + ": " + fb.battery_temperature + "C" + "\n" +
                getResources().getString(R.string.gsm) + ": " + fb.GSM_Signal + "ASU    " + (2.0 * fb.GSM_Signal - 113) + "dBm" + "\n" +
                "-50 -82 dbm   -   very good" + "\n" +
                "-83 -86 dbm   -   good" + "\n" +
                "-87 -91 dbm   -   normal" + "\n" +
                "-92 -95 dbm   -   bad" + "\n" +
                "-96 -100 dbm   -  almost no signal" + "\n" +
                "---------------------------------------------\n" +
                "Image Index:" + fb.Image_Index + "\n" +
                "---------------------------------------------\n" +
                getResources().getString(R.string.phone_memory) + ":" + "\n" +
                "totalMemory: " + fb.totalMemory + "\n" +
                "usedMemory: " + fb.usedMemory + "\n" +
                "freeMemory: " + fb.freeMemory + "\n" +
                "---------------------------------------------\n" +
                getResources().getString(R.string.email_sending_time) + ": " + fb.email_sending_time + "\n" +
                "---------------------------------------------\n" +
                getResources().getString(R.string.Fotobot_settings) + ":\n" +
                "Network_Channel: " + Network_Channel + "\n" +
                "Network_Connection_Method: " + Network_Connection_Method + "\n" +
                "Use_WiFi: " + Use_WiFi + "\n" +
                "Use_Mobile_Data: " + Use_Mobile_Data + "\n" +
                "Use_Flash: " + Use_Flash + "\n" +
                "JPEG_Compression: " + JPEG_Compression + "\n" +
                "Photo_Frequency: " + Photo_Frequency + "\n" +
                "process_delay: " + process_delay + "\n" +
                "Image_Scale: " + Image_Scale + "\n" +
                "Image_Size: " + Image_Size + "\n" +
                "EMail_Sender: " + EMail_Sender + "\n" +
                "EMail_Sender_Password: *********" + "\n" +
                "EMail_Recepient: " + EMail_Recepient + "\n" +
                "Log_Font_Size: " + Log_Font_Size + "\n" +
                "Config_Font_Size: " + Config_Font_Size + "\n" +
                "Photo_Post_Processing_Method: " + Photo_Post_Processing_Method + "\n" +
                "SMTP_Host: " + SMTP_Host + "\n" +
                "SMTP_Port: " + SMTP_Port + "\n" +
                "Log length: " + loglength + "\n" +
                "FLog length: " + floglength + "\n" +
                "wake_up_interval: " + wake_up_interval + "\n" +
                "---------------------------------------------\n" +
                getResources().getString(R.string.hardware_info) + ":\n" +
                "Android: " + Build.VERSION.SDK_INT + "\n" +
                s + "\n");

        File attach_file;
        attach_file = new File(str);
        boolean fileExists = attach_file.isFile();

        if (fileExists) {
            //   SendMessage(h, attach_file.length()/1000 + "Kb");
        } else {
            SendMessage(h, "Image doesn't exist.");
        }

        attach_file = new File((getApplicationContext().getFilesDir().toString() + "/fblog.txt"));
        fileExists = attach_file.isFile();

        if (fileExists) {
            //   SendMessage(h, "FLog: " + attach_file.length()/1000 + "Kb");
        } else {
            SendMessage(h, "Log doesn't exist.");
        }

        try {
            m.addAttachment(str);
            m.addAttachment(getApplicationContext().getFilesDir().toString() + "/fblog.txt");
            fbpause(h, process_delay);

            if (m.send()) {
                SendMessage(h, getResources().getString(R.string.foto_sent));
            } else {
                SendMessage(h, "Email was not sent.");
            }
        } catch (Exception e) {
            SendMessage(h, "Could not send email");
            Log.e("MailApp", "Could not send email", e);
        }

    }

    /**
     * На внешнюю карту можно записывать файлы?
     *
     * @return
     */
    public boolean isExternalStorageWritable() {

        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public File getFolder() {
        return getExternalStoragePublicDirectory(null);
    }

    /**
     * Инициализируем глобальные переменные значениями из SharedPreferences
     */
    public void LoadData() {
        /******* Create SharedPreferences *******/

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Network_Channel = pref.getString("Network_Channel", "Both");

        Network_Connection_Method = pref.getString("Network_Connection_Method", "Method 1");

        Use_WiFi = pref.getBoolean("Use_WiFi", true);         // getting boolean

        Use_Mobile_Data = pref.getBoolean("Use_Mobile_Data", true);         // getting boolean

        Use_Flash = pref.getBoolean("Use_Flash", false);

        JPEG_Compression = pref.getInt("JPEG_Compression", 50);

        Camera_Name = pref.getString("Camera_Name", "default");

        Photo_Frequency = pref.getInt("Photo_Frequency", 15);

        process_delay = pref.getInt("process_delay", 5);

        Image_Scale = pref.getString("Image_Scale", "1");

        Image_Size = pref.getString("Image_Size", "1024x768");

        EMail_Sender = pref.getString("EMail_Sender", "fotobot@gmail.com");

        EMail_Sender_Password = pref.getString("EMail_Sender_Password", "passwd");

        EMail_Recepient = pref.getString("EMail_Recepient", "recipient@mail.ru");

        Log_Font_Size = pref.getInt("Log_Font_Size", 12);

        Config_Font_Size = pref.getInt("Config_Font_Size", 14);

        Photo_Post_Processing_Method = pref.getString("Photo_Post_Processing_Method", "Software");

        SMTP_Host = pref.getString("SMTP_Host", "smtp.gmail.com");

        SMTP_Port = pref.getString("SMTP_Port", "465");

        loglength = pref.getInt("Log_Length", 1024);

        floglength = pref.getInt("FLog_Length", 1024);

        wake_up_interval = pref.getInt("Wake_Up_Interval", 60);
    }

    /**
     * Уровень заряда аккумулятора. Ничего не возвращает, а инициализирует глобальную переменную battery_level.
     */
    public void batteryLevel() {

        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                //context.unregisterReceiver(this);
                int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                battery_temperature = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)) / 10.0f;
                battery_level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    battery_level = (rawlevel * 100) / scale;
                }
            }
        };

        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);

    }

}

/*
Загрузить в сеть новую ветку
git push -u origin branch

Восстановление ветки из сетевого репозитария
git fetch --all
git reset --hard origin/master
git pull origin master

git fetch downloads the latest from remote without trying to merge or rebase anything.

Then the git reset resets the master branch to what you just fetched. The --hard option changes all the files in your working tree to match the files in origin/master

Все ветки на локальном сервере
git branch

Посмотреть все ветки на удаленном сервере
git remote show origin
git branch -r

История коммитов
git log
git log --pretty=format:"%h %s" --graph

*/