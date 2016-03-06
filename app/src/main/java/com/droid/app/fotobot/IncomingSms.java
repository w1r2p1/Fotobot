package com.droid.app.fotobot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class IncomingSms extends BroadcastReceiver {

//    final FotoBot fb = (FotoBot) getApplicationContext();

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

       // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    sms2file(context, message);


                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

public void sms2file(Context context, String sms_message_body) {

    File file = new File(context.getFilesDir().toString() + "/sms.txt");

    FileOutputStream fos = null;

    try {

        fos = new FileOutputStream(file, true);

        // Writes bytes from the specified byte array to this file output stream
        fos.write(sms_message_body.getBytes());

    }
    catch (FileNotFoundException e) {
        Log.e("SmsReceiver", "File not found" + e);
    }
    catch (IOException ioe) {
        Log.e("SmsReceiver", "Exception while writing file " + ioe);
    }
    finally {
        // close the streams using close method
        try {
            if (fos != null) {
                fos.close();
            }
        }
        catch (IOException ioe) {
            Log.e("SmsReceiver", "Error while closing stream: " + ioe);
        }

    }
}


}