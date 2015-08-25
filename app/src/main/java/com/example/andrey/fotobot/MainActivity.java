package com.example.andrey.fotobot;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    int n;
    Handler hndl;
    FotoBot fb;
    String log;


    Handler.Callback hc = new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            String message = (String) msg.obj; //Extract the string from the Message
            log=message + "\n" + log ;
            tvInfo.setText(log);

            final FotoBot fb = (FotoBot) getApplicationContext();
            Log.d(LOG_TAG, "Handler.Callback(): fb.getstatus()" + fb.getstatus());
            n = msg.what;
            if (msg.what == STATUS_STOPPED) btnStart.setText("Play");
     //       log="Закачан файл: " + msg.what + "\n" + log ;
     //       tvInfo.setText(log);



            if (fb.getstatus() == 3) {
                btnStart = (Button) findViewById(R.id.play);
                btnStop = (Button) findViewById(R.id.stop);
                Log.d(LOG_TAG, "Handler.Callback() if: fb.getstatus()" + fb.getstatus());
                btnStart.setText("Play");
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnConfig.setEnabled(true);
                btnStart.postInvalidate();
                btnStop.postInvalidate();
            }


            //    Log.d(LOG_TAG, "what = " + msg.what);
            return false;
        }
    };


    final int STATUS_STARTED = 111;
    final int STATUS_WORKING = 222;
    final int STATUS_STOPPED = 333;
    boolean STOP_FOTOBOT = false;

    final String LOG_TAG = "Logs";
    Button btnStart;
    Button btnStop;
    Button btnConfig;
    Handler h;
    TextView tvInfo;
    TextView text;
    Intent intent;
    String str1 = "Fotobot str to file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FotoBot fb = (FotoBot) getApplicationContext();

        btnStart = (Button) findViewById(R.id.play);
        btnConfig = (Button) findViewById(R.id.config);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        text = (TextView) findViewById(R.id.textView);

        intent = new Intent(MainActivity.this, Status.class);
        log="Фотобот приветствует Вас!";
        tvInfo.setTextSize(20);
        tvInfo.setTypeface(Typeface.SANS_SERIF);
        tvInfo.setTextColor(Color.BLUE);
        tvInfo.setText(log);


    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MainActivity: onDestroy");
    }

    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "MainActivity: onPause");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "MainActivity: onRestart");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "MainActivity: onRestoreInstanceState");
    }

    protected void onResume() {
        final FotoBot fb = (FotoBot) getApplicationContext();
        Log.d(LOG_TAG, "MainActivity: onResume");
        Log.d(LOG_TAG, "MainActivity: fb.getstatus()" + fb.getstatus());
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        super.onResume();

        h = new Handler(hc);
        btnStart = (Button) findViewById(R.id.play);
        btnStop = (Button) findViewById(R.id.stop);


        //tvInfo.setText("Закачано файлов: " + "returned from Status");
  /*      h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                // обновляем TextView

                if (msg.what == STATUS_STOPPED) btnStart.setText("Стартовать Фотобот");
                tvInfo.setText("Закачан файл: " + msg.what);

                if (msg.what == 10) {
                    btnStart.setEnabled(true);
                    tvInfo.setText("Закачано файлов: " + msg.what);

                }



            }
        }; */

        if (fb.getstatus() == 1) {
            btnStart.setText("Play");
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
        }

        if (fb.getstatus() == 2) {
            btnStart.setText("Play");
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
        }


    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "MainActivity: onSaveInstanceState");
    }

    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "MainActivity: onStart");
    }

    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "MainActivity: onStop");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user clicks the Status button
     */
    public void showStatus(View view) {
        Intent intent = new Intent(this, Status.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Settings button
     */
    public void showSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void startFotobot(View v) {
        final FotoBot fb = (FotoBot) getApplicationContext();


        h = new Handler(hc);






        switch (v.getId()) {
            case R.id.play:
                btnStart.setEnabled(false);
                btnStart.setText("Play");
                btnStop = (Button) findViewById(R.id.stop);
                btnStop.setEnabled(true);
                btnConfig.setEnabled(false);
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        MobileData md;
                        md = new MobileData();
                        md.setMobileDataEnabled(getApplicationContext(), true);

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

                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if ( fb.isOnline() )
                        {
                            String message = "мобильные данные включились";
                            Message msg = Message.obtain(); // Creates an new Message instance
                            msg.obj = message; // Put the string into Message, into "obj" field.
                            msg.setTarget(h); // Set the Handler
                            msg.sendToTarget(); //Send the message

                            if ( fb.getData() ) {
                                message = "удалось скачать пробный файл";
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
                            String message = "не удалось включить мобильные данные";
                            Message msg = Message.obtain(); // Creates an new Message instance
                            msg.obj = message; // Put the string into Message, into "obj" field.
                            msg.setTarget(h); // Set the Handler
                            msg.sendToTarget(); //Send the message
                        }








                        for (int i = 1; i <= 1; i++) {

                            if ( fb.getstatus() == 3 ) {

                                h.sendEmptyMessage(4444);

                                Log.d(LOG_TAG, "startFotobot: fb.getstatus()" + fb.getstatus());
                                return;
                            }


                            downloadFile();



                        //    h.sendEmptyMessage(i);

                            String message = Integer.toString(i);
                            Message msg = Message.obtain(); // Creates an new Message instance
                            msg.obj = message; // Put the string into Message, into "obj" field.
                            msg.setTarget(h); // Set the Handler
                            msg.sendToTarget(); //Send the message


                            try {
                                FileOutputStream fileout = openFileOutput("mytextfile.txt", MODE_PRIVATE | MODE_APPEND);
                                OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                                outputWriter.write("Закачан файл:" + Integer.toString(i) + "\n");
                                outputWriter.close();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            // пишем лог
                            //   Log.d(LOG_TAG, "i = " + i);
                        }
                      //  h.sendEmptyMessage(STATUS_STOPPED);
                    }

                }
                );
                t.start();
                fb.setstatus(2);
                Log.d(LOG_TAG, "startFotobot: fb.getstatus()" + fb.getstatus());
                break;
            // case R.id.btnTest:
            //     Log.d(LOG_TAG, "test");
            //     break;
            default:
                break;
        }
    }

    public void stopFotobot(View v) {

        MobileData md;
        md = new MobileData();
        md.setMobileDataEnabled(getApplicationContext(), false);


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
        final FotoBot fb = (FotoBot) getApplicationContext();
        Log.d(LOG_TAG, "stopFotobot: fb.getstatus()" + fb.getstatus());
        fb.setstatus(3);
        Log.d(LOG_TAG, "stopFotobot: STOP_FOTOBOT" + STOP_FOTOBOT);

        btnStart.setText("Play");
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnStart.postInvalidate();
        btnStop.postInvalidate();

/*        switch (v.getId()) {
            case R.id.button2_1:
                break;

            default:
                break;
        }
        */
    }

    void downloadFile() {
        // пауза - 1 секунда
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
