package com.example.andrey.fotobot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Status extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        TextView text = (TextView)findViewById(R.id.textView);
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
    }

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
}
