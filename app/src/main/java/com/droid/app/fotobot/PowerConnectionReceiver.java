package com.droid.app.fotobot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by voran on 7/25/17.
 */

public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        String pinfo="";

        pinfo = "isCarging: " + isCharging +
                "usbCharge: " + usbCharge+
                "acCharge: " + acCharge;
        powerinfo2file(context, pinfo);
    }

    public void powerinfo2file(Context context, String powerinfo_message) {

        File file = new File(context.getFilesDir().toString() + "/powerinfo.txt");

        FileOutputStream fos = null;

        try {

            fos = new FileOutputStream(file, true);

            // Writes bytes from the specified byte array to this file output stream
            fos.write(powerinfo_message.getBytes());

        }
        catch (FileNotFoundException e) {
            Log.e("powerinfo", "File not found" + e);
        }
        catch (IOException ioe) {
            Log.e("powerinfo", "Exception while writing file " + ioe);
        }
        finally {
            // close the streams using close method
            try {
                if (fos != null) {
                    fos.close();
                }
            }
            catch (IOException ioe) {
                Log.e("powerinfo", "Error while closing stream: " + ioe);
            }

        }
    }

}

