package com.example.andrey.fotobot;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Andrey on 22.08.2015.
 */

public class MobileData {
    final String LOG_TAG = "Logs";

    public void setMobileDataEnabled(Context context, boolean enabled) {

        if (Build.VERSION.SDK_INT <= 10) {
            Log.d(LOG_TAG, "Build.VERSION.SDK_INT" + Build.VERSION.SDK_INT);
            try {
                final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final Class conmanClass = Class.forName(conman.getClass().getName());
                final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                iConnectivityManagerField.setAccessible(true);
                final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
                final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                setMobileDataEnabledMethod.setAccessible(true);

                setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ( (Build.VERSION.SDK_INT > 10) && (Build.VERSION.SDK_INT <= 21)) {
            Log.d(LOG_TAG, "Build.VERSION.SDK_INT" + Build.VERSION.SDK_INT);
            ConnectivityManager dataManager;
            dataManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Method dataMtd = null;
            try {
                dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            dataMtd.setAccessible(true);
            try {
                dataMtd.invoke(dataManager, enabled);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            Log.d(LOG_TAG, "Build.VERSION.SDK_INT" + Build.VERSION.SDK_INT);
        }
    }

}


