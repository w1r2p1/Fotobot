package com.example.andrey.fotobot;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Created by admin on 8/26/2015.
 */
public class WiFi {
    final String LOG_TAG = "Logs";

    public void setWiFiEnabled(Context context, boolean enabled) {
        try {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}







/*
ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

//mobile
State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

//wifi
State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
//  and then use it like that:

if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING)
        {
        Toast.makeText(Wifi_Gprs.this,"Mobile is Enabled :) ....",Toast.LENGTH_LONG).show();
        }
        else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING)
        {
        Toast.makeText(Wifi_Gprs.this,"Wifi is Enabled  :) ....",Toast.LENGTH_LONG).show();
        }
        else
        {
        Toast.makeText(Wifi_Gprs.this,"No Wifi or Gprs Enabled :( ....",Toast.LENGTH_LONG).show();
        }
        */