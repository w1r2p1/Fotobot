package com.droid.app.fotobot;

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
