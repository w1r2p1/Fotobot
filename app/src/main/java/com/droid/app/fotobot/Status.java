package com.droid.app.fotobot;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Status extends AppCompatActivity {
Handler h;
    final String LOG_TAG = "Logs";
    TextView text;
    static final int READ_BLOCK_SIZE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // Get the message from the intent
        Intent intent = getIntent();


        text = (TextView)findViewById(R.id.textView);

        //reading text from file
        try {
            FileInputStream fileIn=openFileInput("mytextfile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
         //   Toast.makeText(getBaseContext(), s,Toast.LENGTH_SHORT).show();
            text.setText(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



     //   String str1 = getIntent().getExtras().getString("var1");
     //   text.setText(str1);

/*       h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                // обновляем TextView
                text.setText("Закачано файлов: " + msg.what);
              //  if (msg.what == 10) btnStart.setEnabled(true);
            }
        };
*/



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
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

    /** Called when the user clicks the Settings button */
    public void showMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
     //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void cleanLogs(View view) {
        try {
            FileOutputStream fileout=openFileOutput("mytextfile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write("");
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        text.setText("");

//        Intent intent = new Intent(this, Status.class);
 //       finish();
 //       startActivity(intent);

    }

    public void reloadLogs(View view) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        String str = fb.getstr();
        Log.d(LOG_TAG, "Status:" + str );
        text = (TextView)findViewById(R.id.textView);

        //reading text from file
        try {
            FileInputStream fileIn=openFileInput("mytextfile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            //   Toast.makeText(getBaseContext(), s,Toast.LENGTH_SHORT).show();
            text.setText(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    //    Intent intent = new Intent(this, Status.class);
     //   finish();
     //   startActivity(intent);

    }

}
