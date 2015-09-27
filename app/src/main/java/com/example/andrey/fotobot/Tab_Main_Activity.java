package com.example.andrey.fotobot;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class Tab_Main_Activity extends Activity {

    final String LOG_NETWORK_ACTIVITY = "Logs";
    private CheckBox check_box_flash;
    private TextView text_view_jpeg_compression;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_main);
    }

    public void Apply(View v) {
        check_box_flash = (CheckBox) findViewById(R.id.checkBox_Flah);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (check_box_flash.isChecked()) {
            editor.putBoolean("Use_Flash", true);
        } else {
            editor.putBoolean("Use_Flash", false);
        }

        String input = text_view_jpeg_compression.getText().toString();

        editor.putInt("JPEG_Compression", Integer.parseInt(text_view_jpeg_compression.getText().toString()));

// Save the changes in SharedPreferences
        editor.commit(); // commit changes

    }

}