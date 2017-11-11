/*
Copyright (C) 2017 Andrey Voronin

This file is part of Fotobot.

    Fotobot is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Fotobot is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Fotobot.  If not, see <http://www.gnu.org/licenses/>.
*/

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
