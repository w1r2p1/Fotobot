package com.example.andrey.fotobot;

import android.content.Intent;
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

    Handler hndl;

    Handler.Callback hc = new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            Log.d(LOG_TAG, "what = " + msg.what);
            return false;
        }
    };



    final int STATUS_STARTED = 111;
    final int STATUS_WORKING = 222;
    final int STATUS_STOPPED = 333;

    final String LOG_TAG = "Logs";
    Button btnStart;
    Handler h;
    TextView tvInfo;
    TextView text;
    Intent intent;
    String str1 = "Fotobot str to file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button) findViewById(R.id.button2);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        text = (TextView)findViewById(R.id.textView);

        intent = new Intent(MainActivity.this, Status.class);
        tvInfo.setText("Before handler starts");

        h = new Handler(hc);


    }
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    protected void onResume() {
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        super.onResume();
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
        Log.d(LOG_TAG, "onResume ");
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
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

    /** Called when the user clicks the Status button */
    public void showStatus(View view) {
        Intent intent = new Intent(this, Status.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Settings button */
    public void showSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void startFotobot(View v) {
        switch (v.getId()) {
            case R.id.button2:
                btnStart.setEnabled(false);
                btnStart.setText("Фотобот работает");

                Thread t = new Thread(new Runnable() {
                    public void run() {
                        for (int i = 1; i <= 10; i++) {
                            // долгий процесс
                            downloadFile();

//                          public final boolean sendEmptyMessage (int what)
//                          Added in API level 1
//                          Sends a Message containing only the what value.

                            h.sendEmptyMessage(i);




                                try {
                                    FileOutputStream fileout=openFileOutput("mytextfile.txt", MODE_PRIVATE | MODE_APPEND);
                                    OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                                    outputWriter.write("Закачан файл:" + Integer.toString(i) + "\n");
                                    outputWriter.close();



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }




                            // пишем лог
                            Log.d(LOG_TAG, "i = " + i);
                        }
                        h.sendEmptyMessage(STATUS_STOPPED);
                    }
                });
                t.start();

                break;
           // case R.id.btnTest:
           //     Log.d(LOG_TAG, "test");
           //     break;
            default:
                break;
        }

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
