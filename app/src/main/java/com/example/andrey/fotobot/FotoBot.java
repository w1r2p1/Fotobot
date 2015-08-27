package com.example.andrey.fotobot;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 8/19/2015.
 */
public class FotoBot extends Application {
    public int Update;
    public int status=1;
    public String str="";

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
    public void FotoBot () {
        Update = 300;
    }

    public void Init (){

    }

    public void WriteData () {

    }

    /*
     * isOnline - Check if there is a NetworkConnection
     * @return boolean
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getData() {
        try
        {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(3000); //choose your own timeframe
            urlc.setReadTimeout(4000); //choose your own timeframe
            urlc.connect();
          //  networkcode2 = urlc.getResponseCode();
            return (urlc.getResponseCode() == 200);
        } catch (IOException e)
        {
            return (false);  //connectivity exists, but no internet.
        }
    }

    public boolean MakeInternetConnection(Context context, Handler h) {

        fbpause(h, 5);

        if (isOnline()) {
            String message = "Internet есть";
            Message msg = Message.obtain(); // Creates an new Message instance
            msg.obj = message; // Put the string into Message, into "obj" field.
            msg.setTarget(h); // Set the Handler
            msg.sendToTarget(); //Send the message

            fbpause(h, 3);

            if (getData()) {
                message = "удалось скачать пробный файл,\n связь с Internet работает";
                msg = Message.obtain(); // Creates an new Message instance
                msg.obj = message; // Put the string into Message, into "obj" field.
                msg.setTarget(h); // Set the Handler
                msg.sendToTarget(); //Send the message
            } else {
                message = "не удалось скачать пробный файл";
                msg = Message.obtain(); // Creates an new Message instance
                msg.obj = message; // Put the string into Message, into "obj" field.
                msg.setTarget(h); // Set the Handler
                msg.sendToTarget(); //Send the message
            }
        } else {
            String message = "Internet'а нет, попробуем подключиться";
            Message msg = Message.obtain(); // Creates an new Message instance
            msg.obj = message; // Put the string into Message, into "obj" field.
            msg.setTarget(h); // Set the Handler
            msg.sendToTarget(); //Send the message

        //    WiFi wf;
        //    wf = new WiFi();
        //    wf.setWiFiEnabled(getApplicationContext(), true);

            MobileData md;
            md = new MobileData();
            md.setMobileDataEnabled(getApplicationContext(), true);

            fbpause(h, 9);

            if (isOnline()) {
                message = "Internet появился";
                msg = Message.obtain(); // Creates an new Message instance
                msg.obj = message; // Put the string into Message, into "obj" field.
                msg.setTarget(h); // Set the Handler
                msg.sendToTarget(); //Send the message

                fbpause(h, 3);
                if (getData()) {
                    message = "удалось скачать пробный файл,\n связь с Internet работает";
                    msg = Message.obtain(); // Creates an new Message instance
                    msg.obj = message; // Put the string into Message, into "obj" field.
                    msg.setTarget(h); // Set the Handler
                    msg.sendToTarget(); //Send the message
                } else {
                    message = "не удалось скачать пробный файл";
                    msg = Message.obtain(); // Creates an new Message instance
                    msg.obj = message; // Put the string into Message, into "obj" field.
                    msg.setTarget(h); // Set the Handler
                    msg.sendToTarget(); //Send the message
                }
            }



        }




        return true;
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
}
