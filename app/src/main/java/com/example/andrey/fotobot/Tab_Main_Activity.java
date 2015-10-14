package com.example.andrey.fotobot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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

//public class Tab_Main_Activity extends AppCompatActivity {
public class Tab_Main_Activity extends Activity {

    final String LOG_NETWORK_ACTIVITY = "Logs";
    private CheckBox check_box_flash;
    private EditText edit_text_jpeg_compression;
    private int screenWidth, screenHeight;
    private int padding = 15;
    CheckBox checkBox_Flash;
    EditText editText_JPEG_Compression;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

// Main Container (Vertical LinearLayout)
        LinearLayout FullFrame = new LinearLayout(this);
  //      FullFrame.setBackgroundColor(Color.parseColor("#3f4b4d"));
        FullFrame.setOrientation(LinearLayout.VERTICAL);
        FullFrame.setPadding(5, padding, 0, 0);
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
        linLayout2.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        linLayout2.setLayoutParams(lpView2);

// TextView
        TextView tv = new TextView(this);
        tv.setText("Интервал между фото(сек)");
        tv.setWidth((screenWidth - padding) / 100 * 90);
        tv.setLayoutParams(lpView);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(14);
        tv.setTextColor(Color.WHITE);
        linLayout1.addView(tv);

// Flash TextView
        TextView tv_Flash = new TextView(this);
        tv_Flash.setText("Использовать вспышку");
        tv_Flash.setWidth((screenWidth - padding) / 100 * 99);
        tv_Flash.setLayoutParams(lpView_Flash);
        tv_Flash.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Flash.setTextSize(14);
        tv_Flash.setTextColor(Color.WHITE);
      //  linLayout_Flash.addView(tv_Flash);

// CheckBox
        checkBox_Flash = new CheckBox(this);
      //  linLayout_Flash.addView(checkBox_Flash);

// EditText
        editText_JPEG_Compression = new EditText(this);
        editText_JPEG_Compression.setLayoutParams(lpView_et);
        editText_JPEG_Compression.setText("90");
        ViewGroup.LayoutParams lp = editText_JPEG_Compression.getLayoutParams();
        lp.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 90);
        editText_JPEG_Compression.setLayoutParams(lp);
        editText_JPEG_Compression.setGravity(Gravity.RIGHT);
     //   editText_JPEG_Compression.setBackgroundColor(Color.parseColor("#92adb1"));
        linLayout1.addView(editText_JPEG_Compression);

// Apply Button
        Button btn = new Button(this);
        btn.setText("Применить");
        btn.setGravity(Gravity.BOTTOM);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                if (checkBox_Flash.isChecked()) {
                    editor.putBoolean("Use_Flash", true);
                } else {
                    editor.putBoolean("Use_Flash", false);
                }

                String input = editText_JPEG_Compression.getText().toString();

                editor.putInt("Update", Integer.parseInt(editText_JPEG_Compression.getText().toString()));

// Save the changes in SharedPreferences
                editor.commit(); // commit changes
            }
        });

// GoTo Main Page Button
        Button btn_mp = new Button(this);
        btn_mp.setText("На главную");
        btn_mp.setGravity(Gravity.BOTTOM);

        btn_mp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        linLayout2.addView(btn, lpViewbutton);
        linLayout2.addView(btn_mp, lpViewbutton);

        FullFrame.addView(linLayout1);
        FullFrame.addView(linLayout_Flash);
        FullFrame.addView(linLayout2);

    }

}