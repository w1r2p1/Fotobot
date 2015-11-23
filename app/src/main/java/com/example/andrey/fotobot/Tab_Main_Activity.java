package com.example.andrey.fotobot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

//public class Tab_Main_Activity extends AppCompatActivity {
public class Tab_Main_Activity extends Activity {

    final String LOG_NETWORK_ACTIVITY = "Logs";
    private CheckBox check_box_flash;
    private EditText edit_text_jpeg_compression;
    private int screenWidth, screenHeight;
    private int padding = 15;
    CheckBox checkBox_Flash;
    EditText Photo_Frequency;
    EditText Config_Font_Size;
    EditText Log_Font_Size;
    EditText process_delay;
    final String LOG_TAG = "Logs";

    protected void onCreate(Bundle savedInstanceState) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        super.onCreate(savedInstanceState);
        fb.LoadData();
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

// Main Container (Vertical LinearLayout)
        LinearLayout FullFrame = new LinearLayout(this);
  //      FullFrame.setBackgroundColor(Color.parseColor("#3f4b4d"));
        FullFrame.setOrientation(LinearLayout.VERTICAL);
        FullFrame.setPadding(5, padding, 0, 0);
     //   setContentView(FullFrame);

// Интервал между фото (Horizontal LinearLayout)
        LinearLayout linLayout1 = new LinearLayout(this);
        linLayout1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Интервал между фото (пояснение)
        LinearLayout linLayout1_notes = new LinearLayout(this);
        linLayout1_notes.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView_notes = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et_notes = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Интервал между фото (divider)
        LinearLayout linLayout1_divider = new LinearLayout(this);
        linLayout1_divider.setOrientation(LinearLayout.HORIZONTAL);
        //linLayout1_divider.getLayoutParams().height = 15;
        linLayout1_divider.setPadding(5,15,5,15);
        LinearLayout.LayoutParams lpView_divider = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et_divider = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Flash Container
        LinearLayout linLayout_Flash = new LinearLayout(this);
        linLayout1.setOrientation(LinearLayout.HORIZONTAL);
//        linLayout_Flash.setBackgroundColor(Color.parseColor("#00ff00"));
        LinearLayout.LayoutParams lpView_Flash = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et_Flash = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Flash Container
        LinearLayout linLayout_process_delay = new LinearLayout(this);
        linLayout_process_delay.setOrientation(LinearLayout.HORIZONTAL);
//        linLayout_Flash.setBackgroundColor(Color.parseColor("#00ff00"));
        LinearLayout.LayoutParams lpView_process_delay = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_process_delay_m = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


// Config_Font_Size Container
        LinearLayout linLayout_config_font_size = new LinearLayout(this);
        linLayout_config_font_size.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView_config_font_size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_config_font_size_m = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);




// Log_Font_Size Container
        LinearLayout linLayout_log_font_size = new LinearLayout(this);
        linLayout_config_font_size.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView_log_font_size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_log_font_size_m = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);







// Second Container (Horizontal LinearLayout)
        LinearLayout linLayout2 = new LinearLayout(this);
        linLayout2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams lpViewbutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

//        linLayout2.setBackgroundColor(Color.parseColor("#0000ff"));
        linLayout2.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        linLayout2.setLayoutParams(lpView2);

// Интервал между фото
        TextView tv = new TextView(this);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv.setTextColor(Color.BLACK);
        tv.setText("Интервал между фото(сек)");
        tv.setWidth((screenWidth - padding) / 100 * 80);
        tv.setLayoutParams(lpView);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
    //    tv.setTextSize(14);
      //  tv.setTextColor(Color.WHITE);
        linLayout1.addView(tv);

// Интервал между фото (notes)
        TextView tv_notes = new TextView(this);
        tv_notes.setTypeface(null, Typeface.ITALIC);
        tv_notes.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_notes.setTextColor(Color.BLACK);
        tv_notes.setText("Временной интервал между фото в секундах, рекомендуется > 300 секунд, но можно и чаще.");
        tv_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_notes.setLayoutParams(lpView);
        //tv_notes.setTypeface(Typeface.DEFAULT_BOLD);
        //    tv.setTextSize(14);
        tv_notes.setTextColor(Color.GRAY);
        linLayout1_notes.addView(tv_notes);

// Интервал между фото (divider)
        View line = new View(this);
        line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line.setBackgroundColor(Color.rgb(210, 210, 210));
        line.getLayoutParams().height = 3;
        linLayout1_divider.addView(line);



// EditText
        Photo_Frequency = new EditText(this);
        Photo_Frequency.setLayoutParams(lpView_et);
        Photo_Frequency.setText(Integer.toString(fb.Photo_Frequency));
        ViewGroup.LayoutParams lp = Photo_Frequency.getLayoutParams();
        lp.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 80);
        Photo_Frequency.setLayoutParams(lp);
        Photo_Frequency.setGravity(Gravity.RIGHT);
        Photo_Frequency.setTextColor(Color.BLACK);
        Photo_Frequency.setBackgroundResource(android.R.drawable.edit_text);
     //   editText_JPEG_Compression.setBackgroundColor(Color.parseColor("#92adb1"));
        linLayout1.addView(Photo_Frequency);






        // process delay
        TextView tv_process_delay = new TextView(this);
        tv_process_delay.setTypeface(Typeface.DEFAULT_BOLD);
        tv_process_delay.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_process_delay.setTextColor(Color.BLACK);
        tv_process_delay.setText("Интервал между процессами(сек)");
        tv_process_delay.setWidth((screenWidth - padding) / 100 * 80);
        tv_process_delay.setLayoutParams(lpView);
        tv_process_delay.setTypeface(Typeface.DEFAULT_BOLD);
       // tv_process_delay.setTextSize(14);
        //  tv.setTextColor(Color.WHITE);
        linLayout_process_delay.addView(tv_process_delay);

// EditText
        process_delay = new EditText(this);
        process_delay.setLayoutParams(lpView_et);
        process_delay.setText(Integer.toString(fb.process_delay));
        ViewGroup.LayoutParams lp_process_delay = process_delay.getLayoutParams();
        lp_process_delay.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 80);
        process_delay.setLayoutParams(lp);
        process_delay.setGravity(Gravity.RIGHT);
        //   editText_JPEG_Compression.setBackgroundColor(Color.parseColor("#92adb1"));
        linLayout_process_delay.addView(process_delay);









// Config_Font_Size
        TextView cfs = new TextView(this);
        cfs.setTypeface(Typeface.DEFAULT_BOLD);
        cfs.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        cfs.setTextColor(Color.BLACK);
        cfs.setText("Размер шрифта в настройках");
        cfs.setWidth((screenWidth - padding) / 100 * 80);
        cfs.setLayoutParams(lpView);
        cfs.setTypeface(Typeface.DEFAULT_BOLD);
        //    tv.setTextSize(14);
        //  tv.setTextColor(Color.WHITE);
        linLayout_config_font_size.addView(cfs);

// Config_Font_Size
        Config_Font_Size = new EditText(this);
        Config_Font_Size.setLayoutParams(lpView_et);
        Config_Font_Size.setText(Integer.toString(fb.Config_Font_Size));
        ViewGroup.LayoutParams lp_cfs = Config_Font_Size.getLayoutParams();
        lp_cfs.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 80);
        Config_Font_Size.setLayoutParams(lp_cfs);
        Config_Font_Size.setGravity(Gravity.RIGHT);
        //   editText_JPEG_Compression.setBackgroundColor(Color.parseColor("#92adb1"));
        linLayout_config_font_size.addView(Config_Font_Size);



// Log_Font_Size
        TextView lfs = new TextView(this);
        lfs.setTypeface(Typeface.DEFAULT_BOLD);
        lfs.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        lfs.setTextColor(Color.BLACK);
        lfs.setText("Размер шрифта в логах");
        lfs.setWidth((screenWidth - padding) / 100 * 80);
        lfs.setLayoutParams(lpView);
        lfs.setTypeface(Typeface.DEFAULT_BOLD);
        //    tv.setTextSize(14);
        //  tv.setTextColor(Color.WHITE);
        linLayout_log_font_size.addView(lfs);

// Log_Font_Size
        Log_Font_Size = new EditText(this);
        Log_Font_Size.setLayoutParams(lpView_et);
        Log_Font_Size.setText(Integer.toString(fb.Log_Font_Size));
        ViewGroup.LayoutParams lp_lfs = Log_Font_Size.getLayoutParams();
        lp_lfs.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 80);
        Log_Font_Size.setLayoutParams(lp_lfs);
        Log_Font_Size.setGravity(Gravity.RIGHT);
        //   editText_JPEG_Compression.setBackgroundColor(Color.parseColor("#92adb1"));
        linLayout_log_font_size.addView(Log_Font_Size);






















// Apply Button
        Button btn = new Button(this);
        btn.setText("Применить");
        btn.setGravity(Gravity.CENTER);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putInt("Photo_Frequency", Integer.parseInt(Photo_Frequency.getText().toString()));
                editor.putInt("process_delay", Integer.parseInt(process_delay.getText().toString()));
                editor.putInt("Config_Font_Size", Integer.parseInt(Config_Font_Size.getText().toString()));
                editor.putInt("Log_Font_Size", Integer.parseInt(Log_Font_Size.getText().toString()));

// Save the changes in SharedPreferences
                editor.commit(); // commit changes
            }
        });


// GoTo Main Page Button
        Button btn_mp = new Button(this);
        btn_mp.setText("На главную");
        btn_mp.setGravity(Gravity.CENTER);

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
        FullFrame.addView(linLayout1_notes);
        FullFrame.addView(linLayout1_divider);
        FullFrame.addView(linLayout_process_delay);
        FullFrame.addView(linLayout_Flash);
        FullFrame.addView(linLayout_config_font_size);
        FullFrame.addView(linLayout_log_font_size);
        FullFrame.addView(linLayout2);

        ScrollView m_Scroll = new ScrollView(this);
        m_Scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        m_Scroll.addView( FullFrame, new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT) );

//  addContentView(m_Scroll, new LayoutParams(LayoutParams.FILL_PARENT,
//  LayoutParams.WRAP_CONTENT));

        setContentView(m_Scroll);

    }
    protected void onPause() {
        super.onPause();
        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.LoadData();
        Photo_Frequency.setText(Integer.toString(fb.Photo_Frequency));
        process_delay.setText(Integer.toString(fb.process_delay));
    }

    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "Tab1: onRestart");
    }
    protected void onResume(SurfaceHolder holder) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.LoadData();
        Photo_Frequency.setText(Integer.toString(fb.Photo_Frequency));
        //  spinner1.setSelection(spinnerArrayAdapter1.getPosition(fb.Image_Scale));
        Log.d(LOG_TAG, "Tab1: onResume");
    }


    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "Tab1: onRestoreInstanceState");
    }
}