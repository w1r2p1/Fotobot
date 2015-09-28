package com.example.andrey.fotobot;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class Tab_Main_Activity extends Activity {

    final String LOG_NETWORK_ACTIVITY = "Logs";
    private CheckBox check_box_flash;
    private EditText edit_text_jpeg_compression;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_main);
    }

    public void Apply(View v) {
        check_box_flash = (CheckBox) findViewById(R.id.checkBox_Flash);
        edit_text_jpeg_compression = (EditText) findViewById(R.id.editText_JPEG_Compression);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (check_box_flash.isChecked()) {
            editor.putBoolean("Use_Flash", true);
        } else {
            editor.putBoolean("Use_Flash", false);
        }

        String input = edit_text_jpeg_compression.getText().toString();

        editor.putInt("JPEG_Compression", Integer.parseInt(edit_text_jpeg_compression.getText().toString()));

// Save the changes in SharedPreferences
        editor.commit(); // commit changes

    }

}