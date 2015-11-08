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
 * Умеет делать фото и отправлять на почту.
 * Это глобальный класс, объект данного класса будет виден во всех активити. Инициализируется через Manifest.
 */
public class FotoBot extends Application {
    /**
     * Интервал фотографирования (в секундах)
     */
    public int Photo_Frequency;
  //  public int Update;
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
    /**
     * Степень JPEG сжатия
     */
    public int JPEG_Compression = 90;

    /**
     * Коэффициент масштабирования фото (1/4,1/2,1)
     */
    public String Image_Scale = "1/4";



    /**
     * вспомогательная почта для отправки писем
     */
    public String EMail_Sender = "user@gmail.com";

    /**
     * пароль для вспомогательной почты
     */
    public String EMail_Sender_Password = "passwd";

    /** кому отправлять письма с фотками */
    public String EMail_Recepient = "user@mail.ru";

    public String Network_Channel;

    /**
     * Соединятся с Internet один раз или на каждом шаге
     */
    public String Network_Connection_Method;

    public int process_delay = 3;

    public int status = 1;

    public SurfaceHolder holder;
    public String str = "";

    public Handler h;

    /**
     * Возвращает текущее состояние FotoBot'а, сейчас не пользуюсь этим
     * @return
     */
    public int getstatus() {

        return status;
    }

    /**
     * Устанавливает статус
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
            //  networkcode2 = urlc.getResponseCode();
            SendMessage(h, "удалось скачать файл из Internet");
            return (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            SendMessage(h, "не удалось скачать файл из Internet");
            return (false);  //connectivity exists, but no internet.
        }
    }

    /**
     * FotoBot умеет самостоятельно подключаться к Internet для отправки фото на почту
     * @param context
     * @param h
     * @return
     */
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

    /** FotoBot может самостоятельно отключаться от Internet
     *
     * @param context
     * @param h
     */
    public void CloseInternetConnection(Context context, Handler h) {
        MobileData md;
        md = new MobileData();
        md.setMobileDataEnabled(getApplicationContext(), false);

        WiFi wf;
        wf = new WiFi();
        wf.setWiFiEnabled(getApplicationContext(), false);
    }

    /**
     * Делаем паузу и печатаем на экран точки, чтобы было понятно, что программа не зависла
     * @param h
     * @param delay
     */
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
     * @param h
     * @param str
     */
    public void SendMail(Handler h, String str) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        Mail m = new Mail(fb.EMail_Sender, fb.EMail_Sender_Password);

        String[] toArr = {fb.EMail_Recepient};
        m.setTo(toArr);
        m.setFrom(fb.EMail_Sender);
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
                TimeUnit.SECONDS.sleep(3);
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

    /**
     * На внешнюю карту можно записывать файлы?
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

        Network_Connection_Method = pref.getString("Network_Connection_Method", "На каждом шаге");

        Use_WiFi = pref.getBoolean("Use_WiFi", true);         // getting boolean

        Use_Mobile_Data = pref.getBoolean("Use_Mobile_Data", true);         // getting boolean

        Use_Flash = pref.getBoolean("Use_Flash", false);

        JPEG_Compression = pref.getInt("JPEG_Compression", 50);

        Photo_Frequency = pref.getInt("Photo_Frequency", 60);

        process_delay = pref.getInt("process_delay", 60);

        Image_Scale = pref.getString("Image_Scale","1/4");

        EMail_Sender = pref.getString("EMail_Sender", "user@gmail.com");

        EMail_Sender_Password = pref.getString("EMail_Sender_Password", "passwd");

        EMail_Recepient = pref.getString("EMail_Recepient", "user@mail.ru");
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


