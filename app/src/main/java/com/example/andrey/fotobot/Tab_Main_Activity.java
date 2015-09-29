package com.example.andrey.fotobot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Tab_Main_Activity extends Activity {

    final String LOG_NETWORK_ACTIVITY = "Logs";
    private CheckBox check_box_flash;
    private EditText edit_text_jpeg_compression;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// создание LinearLayout
        LinearLayout linLayout = new LinearLayout(this);
        // установим вертикальную ориентацию
        linLayout.setOrientation(LinearLayout.VERTICAL);
        // создаем LayoutParams
        LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // устанавливаем linLayout как корневой элемент экрана
        setContentView(linLayout, linLayoutParam);

        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(this);
        tv.setText("JPEG Compression");
        tv.setLayoutParams(lpView);
        linLayout.addView(tv);

        EditText et = new EditText(this);
        et.setLayoutParams(lpView);
        linLayout.addView(et);

        Button btn = new Button(this);
        btn.setText("Применить");
        linLayout.addView(btn, lpView);


        LinearLayout.LayoutParams leftMarginParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        leftMarginParams.gravity = Gravity.BOTTOM;
        leftMarginParams.leftMargin = 50;

/*

        Button btn1 = new Button(this);
        btn1.setText("Button1");
        linLayout.addView(btn1, leftMarginParams);


        LinearLayout.LayoutParams rightGravityParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rightGravityParams.gravity = Gravity.RIGHT;

        Button btn2 = new Button(this);
        btn2.setText("Button2");
        linLayout.addView(btn2, rightGravityParams);

*/




        //LinearLayout.LayoutParams ladderFLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0);
        //ladderFLParams.weight = 5f;
        //LinearLayout.LayoutParams dummyParams = new LinearLayout.LayoutParams(0,0);
        //dummyParams.weight = 1f;


        //setContentView(R.layout.tab_main);

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
    /** Called when the user clicks the Settings button */
    public void showMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}