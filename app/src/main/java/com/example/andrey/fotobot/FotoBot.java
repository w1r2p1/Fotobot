package com.example.andrey.fotobot;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * <h1>FotoBot</h1>
 * Умеет делать фото порасписанию и отправлять на почту
 */
public class FotoBot extends Application {

//Global variables
    public int Update;                      /** Интервал фотографирования (в секундах) */
    public boolean Use_WiFi;                /** Нужно ли использовать Wi-Fi для выхода в Internet */
    public boolean Use_Mobile_Data;         /** Нужно ли использовать 3G (2G) для выхода в Internet */
    public boolean Use_Flash;               /** Делать фото со вспышкой */
    public int JPEG_Compression;             /** Степень JPEG сжатия */
    public int Photo_Frequency;
    public String EMail_Sender;              /** вспомогательная почта для отправки писем */
    public String EMail_Sender_Password;     /** пароль для вспомогательной почты */
    public String EMail_Recepient;           /** кому отправлять письма с фотками */;
    public String Network_Channel;
    public String Network_Connection_Method; /** Соединятся с Internet один раз или на каждом шаге*/
//End Global variables

    public int status = 1;

    public SurfaceHolder holder;
    public String str = "";

    public int getstatus() {

        return status;
    }

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
     * В конструкторе проводим инициализацию объекта посредством считывания всей свойств из SharedPreferences.
     */
    public void FotoBot() {
        LoadData();
    }

    public void Init() {

    }

    public void WriteData() {

    }

    /*
     * isOnline - Check if there is a NetworkConnection
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

    public boolean getData(Handler h) {
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(3000); //choose your own timeframe
            urlc.setReadTimeout(4000); //choose your own timeframe
            urlc.connect();
            //  networkcode2 = urlc.getResponseCode();
            SendMessage(h, "удалось скачать файл из Internet");
            return (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            SendMessage(h, "не удалось скачать файл из Internet");
            return (false);  //connectivity exists, but no internet.
        }
    }

    public boolean MakeInternetConnection(Context context, Handler h) {
        fbpause(h, 1);

        WiFi wf;
        wf = new WiFi();

        boolean wf_connect_attempt = false;

        MobileData md;
        md = new MobileData();

        if (!(isOnline(h) && getData(h))) {
            SendMessage(h, "Для начала включим Wi-Fi");
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

    public void CloseInternetConnection(Context context, Handler h) {
        MobileData md;
        md = new MobileData();
        md.setMobileDataEnabled(getApplicationContext(), false);

        WiFi wf;
        wf = new WiFi();
        wf.setWiFiEnabled(getApplicationContext(), false);
    }

    public void fbpause(Handler h, int delay) {
        String message;

        for (int i = 1; i <= delay; i++) {
            Message msg = Message.obtain(); // Creates an new Message instance
            message = ". . . . . " + Integer.toString(i);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            msg.obj = message; // Put the string into Message, into "obj" field.
            msg.setTarget(h); // Set the Handler
            //   msg.sendToTarget(); //Send the message
        }
    }

    /**
     * Печатаем сообщение на экран
     * @param h handler, который ловит сообщения
     * @param str текст сообщения
     */
    public void SendMessage(Handler h, String str) {
        Message msg = Message.obtain(); // Creates an new Message instance
        msg.obj = str; // Put the string into Message, into "obj" field.
        msg.setTarget(h); // Set the Handler
        msg.sendToTarget(); //Send the message
    }

    public void SendMail(Handler h, String str) {
        Mail m = new Mail("fotobotmail@gmail.com", "fotobotmailpasswd");

        String[] toArr = {"digibolt@mail.ru"};
        m.setTo(toArr);
        m.setFrom("voran.mail@gmail.com");
        m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
        m.setBody("Email body.");
        SendMessage(h, "SendMail" + str);
        str = getApplicationContext().getFilesDir().toString() + "/" + str;
        //  str = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/fotobot.jpg";;

        SendMessage(h, "SendMail with file path:" + str);

        File attach_file;
        attach_file = new File(str);
        boolean fileExists = attach_file.isFile();

        if (fileExists) {

            SendMessage(h, "SendMail: файл с фото есть: " + attach_file.length());
        } else {
            SendMessage(h, "SendMail: файла с фото нет");
        }

        try {

            m.addAttachment(str);

            try {
                TimeUnit.SECONDS.sleep(9);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (m.send()) {
                //  Toast.makeText(MailApp.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                SendMessage(h, "Email was sent successfully.");
            } else {
                // Toast.makeText(MailApp.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                SendMessage(h, "Email was not sent.");
            }
        } catch (Exception e) {
            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            SendMessage(h, "Could not send email");
            Log.e("MailApp", "Could not send email", e);
        }
    }

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

        Use_WiFi = pref.getBoolean("Use_WiFi", true);         // getting boolean

        Use_Mobile_Data = pref.getBoolean("Use_Mobile_Data", true);         // getting boolean

        Use_Flash = pref.getBoolean("Use_Flash", true);

        JPEG_Compression = pref.getInt("JPEG_Compression", 50);

        Photo_Frequency = pref.getInt("Photo_Frequency", 60);


    }
    /**
     * Инициализируем глобальные переменные значениями из SharedPreferences и выводим отладочную информацию на экран
     */
    public void LoadData(Handler h) {
        /******* Create SharedPreferences *******/

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Use_WiFi = pref.getBoolean("Use_WiFi", true);         // getting boolean
        SendMessage(h, "LoadData: Use_WiFi " + Use_WiFi);
        Use_Mobile_Data = pref.getBoolean("Use_Mobile_Data", true);         // getting boolean
        SendMessage(h, "LoadData: Use_Mobile_Data " + Use_Mobile_Data);
        Use_Flash = pref.getBoolean("Use_Flash", true);
        SendMessage(h, "LoadData: Use_Flash " + Use_Flash);
        JPEG_Compression = pref.getInt("JPEG_Compression", 50);
        SendMessage(h, "LoadData: JPEG_Compression " + JPEG_Compression);
        Photo_Frequency = pref.getInt("Photo_Frequency", 60);
        SendMessage(h, "LoadData: Photo_Frequency " + Photo_Frequency);

    }
}


