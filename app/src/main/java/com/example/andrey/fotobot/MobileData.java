package com.example.andrey.fotobot;

import android.content.Context;
import android.net.ConnectivityManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Andrey on 22.08.2015.
 */

public class MobileData {
    public void setMobileDataEnabled(Context context, boolean enabled) {
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





/*
            final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class<?> conmanClass = Class.forName(conman.getClass().getName());
            final Method setMobileDataEnabledMethod = conmanClass.getMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(conman, enabled);
*/






        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}




/*      ConnectivityManager dataManager;
        dataManager  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Method dataMtd = null;
        try {
            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dataMtd.setAccessible(true);
        try {
            dataMtd.invoke(dataManager, false);
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
*/

  /*      ConnectivityManager dataManager;
                        dataManager  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        Method dataMtd = null;
                        try {
                            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
                        } catch (NoSuchMethodException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        dataMtd.setAccessible(true);
                        try {
                            dataMtd.invoke(dataManager, true);
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
*/