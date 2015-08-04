package com.example.andrey.fotobot;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Status extends AppCompatActivity {
Handler h;
    TextView text;
    static final int READ_BLOCK_SIZE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // Get the message from the intent
        Intent intent = getIntent();


        text = (TextView)findViewById(R.id.textView);
        text.setText("Преимущество использования ядра Linux, \n" +
                "как основы платформы Android состоит в\n" +
                "том, что ядро системы позволяет верхним\n" +
                "уровням\n" +
                "программного стека оставаться неизменным\n" +
                "несмотря на изменения в используемом оборудовании. \n" +
                "Конечно, хорошая практика программирования\n" +
                "требует, чтобы пользовательские приложения корректно \n" +
                "завершали свою работу в случае вызова\n" +
                "ресурса, являющегося недоступным, например, камеры, не \n" +
                "присутствующей в специфической модели\n" +
                "смартофона. Поскольку новое вспомогательное \n" +
                "оборудование для мобильных устройств постоянно\n" +
                "появляется на рынке, драйверы для них должны быть \n" +
                "быть написаны на уровне ядра Linux для\n" +
                "обеспечения поддержку оборудования также, \n" +
                "как на настольных Linux-системах.\n" +
                "Поскольку новое вспомогательное \n" +
                "оборудование для мобильных устройств постоянно\n" +
                "появляется на рынке, драйверы для них должны быть \n" +
                "быть написаны на уровне ядра Linux для\n" +
                "обеспечения поддержку оборудования также, \n" +
                "как на настольных Linux-системах.");


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
        startActivity(intent);
    }

}
