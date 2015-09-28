package com.example.andrey.fotobot;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class Tab_Scheduler_Activity  extends Activity {

    final String LOG_NETWORK_ACTIVITY = "Logs";
    private EditText edit_text_photo_frequency;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_scheduler);
    }

    public void Apply(View v) {

        edit_text_photo_frequency = (EditText) findViewById(R.id.editText_photo_frequency);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String input = edit_text_photo_frequency.getText().toString();

        editor.putInt("Photo_Frequency", Integer.parseInt(edit_text_photo_frequency.getText().toString()));

// Save the changes in SharedPreferences
        editor.commit(); // commit changes

    }

}
