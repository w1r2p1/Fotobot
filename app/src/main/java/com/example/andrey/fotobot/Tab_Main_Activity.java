package com.example.andrey.fotobot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Tab_Main_Activity extends Activity {

    final String LOG_NETWORK_ACTIVITY = "Logs";
    private CheckBox check_box_flash;
    private EditText edit_text_jpeg_compression;
    private int screenWidth, screenHeight;
    CheckBox checkBox_Flash;
    EditText editText_JPEG_Compression;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

// Main Container (Vertical LinearLayout)
        LinearLayout FullFrame = new LinearLayout(this);
        FullFrame.setOrientation(LinearLayout.VERTICAL);
        setContentView(FullFrame);

// First Container (Horizontal LinearLayout)
        LinearLayout linLayout1 = new LinearLayout(this);
        linLayout1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Flash Container
        LinearLayout linLayout_Flash = new LinearLayout(this);
        linLayout1.setOrientation(LinearLayout.HORIZONTAL);
//        linLayout_Flash.setBackgroundColor(Color.parseColor("#00ff00"));
        LinearLayout.LayoutParams lpView_Flash = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et_Flash = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


// Second Container (Horizontal LinearLayout)
        LinearLayout linLayout2 = new LinearLayout(this);
        linLayout2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams lpViewbutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

//        linLayout2.setBackgroundColor(Color.parseColor("#0000ff"));
        linLayout2.setGravity(Gravity.BOTTOM|Gravity.CENTER);
        linLayout2.setLayoutParams(lpView2);

// TextView
        TextView tv = new TextView(this);
        tv.setText("JPEG Compression");
        tv.setWidth(screenWidth/100*90);
        tv.setLayoutParams(lpView);
        linLayout1.addView(tv);

// Flash TextView
        TextView tv_Flash = new TextView(this);
        tv_Flash.setText("Use Flash");
        tv_Flash.setWidth(screenWidth/100*98);
        tv_Flash.setLayoutParams(lpView_Flash);
        linLayout_Flash.addView(tv_Flash);

// CheckBox
        checkBox_Flash = new CheckBox(this);
        linLayout_Flash.addView(checkBox_Flash);

// EditText
        editText_JPEG_Compression = new EditText(this);
        editText_JPEG_Compression.setLayoutParams(lpView_et);
        editText_JPEG_Compression.setText("90");
        ViewGroup.LayoutParams lp = editText_JPEG_Compression.getLayoutParams();
        lp.width = screenWidth-screenWidth/100*90;
        editText_JPEG_Compression.setLayoutParams(lp);
        editText_JPEG_Compression.setGravity(Gravity.RIGHT);
        linLayout1.addView(editText_JPEG_Compression);

// Button
        Button btn = new Button(this);
        btn.setText("Применить");
        btn.setGravity(Gravity.BOTTOM);






        linLayout2.addView(btn, lpViewbutton);


        FullFrame.addView(linLayout1);
        FullFrame.addView(linLayout_Flash);
        FullFrame.addView(linLayout2);

//        LinearLayout.LayoutParams leftMarginParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//       leftMarginParams.gravity = Gravity.BOTTOM;
      //  leftMarginParams.leftMargin = 50;

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
    //    check_box_flash = (CheckBox) findViewById(R.id.checkBox_Flash);
    //    edit_text_jpeg_compression = (EditText) findViewById(R.id.editText_JPEG_Compression);

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