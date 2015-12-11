package com.example.andrey.fotobot;

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

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * <h1>FotoBot</h1>
 * Умеет делать фото и отправлять на почту.
 * Это глобальный класс, объект данного класса будет виден во всех активити. Инициализируется через Manifest.
 */
public class FotoBot extends Application {

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
     * Размер изображения в пикселях
     */
    public String Image_Size = "320x240";


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
    public String EMail_Recepient = "user@mail.ru";

    public String Network_Channel = "Both";

    /**
     * Соединятся с Internet один раз (Method1) или на каждом шаге (Method2)
     */
    public String Network_Connection_Method = "В начале работы";

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

    /**
     * Размер шрифта в настройках (sp)
     */
    public int Config_Font_Size = 20;

    /**
     * Размер шрифта в логе (sp)
     */
    public int Log_Font_Size = 14;

    public String Image_Name;

    public String Image_Name_Full_Path;

    /**
     * Camera properties
     */
    public String Camera_Properties;

    /**
     * Документация
     */
    public String Main_Help = "<h1><font color=blue>Фотобот приветствует Вас!</font></h1>" +
            "";

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
    public boolean isOnline(Handler h) {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            SendMessage(h, "есть соединение с Internet");
            return true;
        } else {
            SendMessage(h, "нет соединения с Internet");
            return false;
        }

    }

    /**
     * Для проверки соединения выкачивает страницу из Internet
     *
     * @param h
     * @return
     */
    public boolean getData(Handler h) {

        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(3000); //choose your own timeframe
            urlc.setReadTimeout(4000); //choose your own timeframe
            urlc.connect();
            SendMessage(h, "удалось скачать файл из Internet");
            return (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            SendMessage(h, "не удалось скачать файл из Internet");
            return (false);  //connectivity exists, but no internet.
        }

    }

    /**
     * FotoBot умеет самостоятельно подключаться к Internet для отправки фото на почту
     *
     * @param context
     * @param h
     * @return
     */
    public boolean MakeInternetConnection(Context context, Handler h) {

        WiFi wf;

        wf = new WiFi();

        LoadData();

        boolean wf_connect_attempt = false;

        MobileData md;
        md = new MobileData();

        if (Network_Channel.contains("Wi-Fi")) {
            SendMessage(h, "Метод подключения Wi-Fi");
            if (!(isOnline(h) && getData(h))) {
                SendMessage(h, "Включаем Wi-Fi");
                wf.setWiFiEnabled(getApplicationContext(), true);
                fbpause(h, 5);
                SendMessage(h, "Wi-Fi включается долго,\nнужно подождать");
                fbpause(h, 5);

                if ((isOnline(h) && getData(h))) {
                    SendMessage(h, "Ура! Связь с Internet появилась!");
                    return true;
                }

            }

        }

        if (Network_Channel.contains("Mobile Data")) {
            SendMessage(h, "Метод подключения MobileData");
            if (!(isOnline(h) && getData(h))) {
                SendMessage(h, "включаем Mobile Data");
                wf.setWiFiEnabled(getApplicationContext(), false);
                fbpause(h, 5);
                md.setMobileDataEnabled(getApplicationContext(), true);
                fbpause(h, 5);
            }

            if ((isOnline(h) && getData(h))) {
                SendMessage(h, "Ура! Связь с Internet появилась!");
                return true;
            } else {
                return false;
            }

        }

        if (Network_Channel.contains("Both")) {
            SendMessage(h, "Метод подключения Both");
            if (!(isOnline(h) && getData(h))) {
                SendMessage(h, "Включаем Wi-Fi");
                wf.setWiFiEnabled(getApplicationContext(), true);
                fbpause(h, 5);
                SendMessage(h, "Wi-Fi включается долго,\nнужно подождать");
                fbpause(h, 5);

                if ((isOnline(h) && getData(h))) {
                    SendMessage(h, "Ура! Связь с Internet появилась!");
                    return true;
                } else {
                    wf_connect_attempt = true;
                }

            }

            if (!(isOnline(h) && getData(h)) && wf_connect_attempt) {
                SendMessage(h, "По Wi-Fi нет связи,\nвключаем Mobile Data");
                wf.setWiFiEnabled(getApplicationContext(), false);
                fbpause(h, 5);
                md.setMobileDataEnabled(getApplicationContext(), true);
                fbpause(h, 5);
            }

            if ((isOnline(h) && getData(h))) {
                SendMessage(h, "Ура! Связь с Internet появилась!");
                return true;
            } else {
                return false;
            }

        }
        return false;
    }

    /**
     * FotoBot может самостоятельно отключаться от Internet
     *
     * @param context
     * @param h
     */
    public void CloseInternetConnection(Context context, Handler h) {

        MobileData md;
        md = new MobileData();
//Sony Xperia error
        md.setMobileDataEnabled(getApplicationContext(), false);

        WiFi wf;
        wf = new WiFi();
        wf.setWiFiEnabled(getApplicationContext(), false);

        SendMessage("Соединение с Internet завершено.");
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

        SendMessage("Аттачим" + str);

        Mail m = new Mail(fb.EMail_Sender, fb.EMail_Sender_Password);

        String[] toArr = {fb.EMail_Recepient};

        String s = "Debug-infos:";
        s += "\n OS Version: "      + System.getProperty("os.version")      + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\n OS API Level: "    + android.os.Build.VERSION.SDK_INT;
        s += "\n Device: "          + android.os.Build.DEVICE;
        s += "\n Model (and Product): " + android.os.Build.MODEL            + " ("+ android.os.Build.PRODUCT + ")";

        s += "\n RELEASE: "         + android.os.Build.VERSION.RELEASE;
        s += "\n BRAND: "           + android.os.Build.BRAND;
        s += "\n DISPLAY: "         + android.os.Build.DISPLAY;
        s += "\n CPU_ABI: "         + android.os.Build.CPU_ABI;
        s += "\n CPU_ABI2: "        + android.os.Build.CPU_ABI2;
        s += "\n UNKNOWN: "         + android.os.Build.UNKNOWN;
        s += "\n HARDWARE: "        + android.os.Build.HARDWARE;
        s += "\n Build ID: "        + android.os.Build.ID;
        s += "\n MANUFACTURER: "    + android.os.Build.MANUFACTURER;
        s += "\n SERIAL: "          + android.os.Build.SERIAL;
        s += "\n USER: "            + android.os.Build.USER;
        s += "\n HOST: "            + android.os.Build.HOST;



        m.setTo(toArr);
        m.setFrom(fb.EMail_Sender);
        m.setSubject("Fotobot");
        m.setBody("Уровень зарядки аккумулятора: " + fb.battery_level + "%" + "\n" +
                "Сила GSM сигнала: " + fb.GSM_Signal + "ASU    " + (2.0 * fb.GSM_Signal - 113) + "dBm" + "\n" +
                "Android: " + Build.VERSION.SDK_INT + "\n" +
                "Camera: " + fb.Camera_Properties + "\n" +
                s + "\n");

       // str = getApplicationContext().getFilesDir().toString() + "/" + str;

        File attach_file;
        attach_file = new File(str);
        boolean fileExists = attach_file.isFile();

        if (fileExists) {
            SendMessage(h, fb.Image_Name + ": " + attach_file.length() + " байт");
        } else {
            SendMessage(h, "SendMail: файла с фото нет");
        }

        try {
            m.addAttachment(str);
            fbpause(h, process_delay);

            if (m.send()) {
                SendMessage(h, "Фото выслано на почту");
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

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
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

        Network_Connection_Method = pref.getString("Network_Connection_Method", "В начале работы");

        Use_WiFi = pref.getBoolean("Use_WiFi", true);         // getting boolean

        Use_Mobile_Data = pref.getBoolean("Use_Mobile_Data", true);         // getting boolean

        Use_Flash = pref.getBoolean("Use_Flash", false);

        JPEG_Compression = pref.getInt("JPEG_Compression", 50);

        Photo_Frequency = pref.getInt("Photo_Frequency", 15);

        process_delay = pref.getInt("process_delay", 5);

        Image_Scale = pref.getString("Image_Scale", "1");

        Image_Size = pref.getString("Image_Size", "320x240");

        EMail_Sender = pref.getString("EMail_Sender", "fotobotmail@gmail.com");

        EMail_Sender_Password = pref.getString("EMail_Sender_Password", "fotobotmailpasswd");

        EMail_Recepient = pref.getString("EMail_Recepient", "voran@inbox.ru");

        Log_Font_Size = pref.getInt("Log_Font_Size", 12);

        Config_Font_Size = pref.getInt("Config_Font_Size", 14);

        Photo_Post_Processing_Method = pref.getString("Photo_Post_Processing_Method", "Hardware");

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
