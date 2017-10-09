package com.droid.app.fotobot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    public static String sms_file="";

    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        Log.i("Receiver", "Broadcast received: " + action);

        if(action.equals("workdir_intent")){
            sms_file = intent.getExtras().getString("workdir");
            Log.i("Receiver", "sms_file: " + sms_file);
        }


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
                    message = senderNum + "\n" + message;

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    sms2file(context, message);

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

public void sms2file(Context context, String sms_message_body) {

  //  final FotoBot fb = (FotoBot) context;

    File file = new File(sms_file + "/sms.txt");

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