package com.droid.app.fotobot;

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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.logging.Logger;

public class Tab_Main_Activity extends Activity {

    final String LOG_NETWORK_ACTIVITY = "Logs";
    Button btn, btn_mp;
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

    Logger fblogger = Logger.getLogger(FotoBot.class.getName());

    protected void onCreate(Bundle savedInstanceState) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        super.onCreate(savedInstanceState);
        fb.LoadData();


        Logger fblogger = Logger.getLogger(FotoBot.class.getName());
        fblogger.fine("Tab_Main_Activity");



        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

// Main Container (Vertical LinearLayout)
// Главный контейнер внутри которого вся раскладка
        LinearLayout FullFrame = new LinearLayout(this);
        FullFrame.setOrientation(LinearLayout.VERTICAL);
        FullFrame.setPadding(5, 5, 5, 5);
        FullFrame.setBackgroundColor(Color.rgb(192,192,192));
        LinearLayout.LayoutParams lpFull_Frame = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        FullFrame.setLayoutParams(lpFull_Frame);
        FullFrame.setMinimumHeight(fb.Working_Area_Height - fb.menuheight);

        // Find the root view
      //  View baseView = FullFrame.getRootView();
      //  baseView.setBackgroundColor(Color.rgb(0,0,90));

// ------------------------------------------------------------------------------------------------

// 1. Интервал между фото (Horizontal LinearLayout контейнер)
       // LinearLayout linLayout1 = new LinearLayout(this);
        RelativeLayout linLayout1 = new RelativeLayout(this);
     //   linLayout1.setOrientation(LinearLayout.HORIZONTAL);
        //LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout1.setBackgroundColor(Color.rgb(192, 192, 192));


// 1.1 Интервал между фото (пояснение контейнер)
        LinearLayout linLayout1_notes = new LinearLayout(this);
        //RelativeLayout linLayout1_notes = new RelativeLayout(this);
        linLayout1_notes.setOrientation(LinearLayout.HORIZONTAL);
        linLayout1_notes.setBackgroundColor(Color.rgb(192,192,192));
        linLayout1_notes.setPadding(0,0,0,9);

// 1.2 Интервал между фото (divider контейнер)
        LinearLayout linLayout1_divider = new LinearLayout(this);
        linLayout1_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout1_divider.setPadding(1,5,5,5);
        linLayout1_divider.setBackgroundColor(Color.rgb(192,192,192));

// 1.3 Интервал между фото
        TextView tv = new TextView(this);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv.setTextColor(Color.BLACK);
        tv.setText(getResources().getString(R.string.pause_between_frames));
        tv.setWidth((screenWidth - padding) / 100 * 80);
        tv.setLayoutParams(lpView);
        tv.setTypeface(Typeface.DEFAULT_BOLD);

        lpView.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv.getId());
        tv.setLayoutParams(lpView);
        linLayout1.addView(tv);

// 1.4 Интервал между фото (notes)
        TextView tv_notes = new TextView(this);
        tv_notes.setTypeface(null, Typeface.NORMAL);
        tv_notes.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_notes.setTextColor(Color.BLACK);
        tv_notes.setText(getResources().getString(R.string.pause_between_frames_description));
        tv_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_notes.setLayoutParams(lpView);
        tv_notes.setPadding(1, 5, 5, 5);
        linLayout1_notes.addView(tv_notes);

// 1.5 Интервал между фото (ввод данных)
        Photo_Frequency = new EditText(this);
        Photo_Frequency.setLayoutParams(lpView_et);
        Photo_Frequency.setTextColor(Color.rgb(50,100,150));
        Photo_Frequency.setText(Integer.toString(fb.Photo_Frequency));
        ViewGroup.LayoutParams lp = Photo_Frequency.getLayoutParams();
        lp.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 80);
        Photo_Frequency.setLayoutParams(lp);
        Photo_Frequency.setGravity(Gravity.RIGHT);

        lpView_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, Photo_Frequency.getId());
        Photo_Frequency.setLayoutParams(lpView_m);
        linLayout1.addView(Photo_Frequency);

// 1.6 Интервал между фото (divider)
        View line = new View(this);
        line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line.setBackgroundColor(Color.rgb(210, 210, 210));
        line.getLayoutParams().height = 3;
        linLayout1_divider.addView(line);

// ------------------------------------------------------------------------------------------------

// 2. Интервал между процессами (основной контейнер)
        RelativeLayout linLayout_process_delay = new RelativeLayout(this);
//        linLayout_process_delay.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_process_delay.setBackgroundColor(Color.rgb(192,192,192));
        RelativeLayout.LayoutParams lpView_m1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_m2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
      //  RelativeLayout.LayoutParams lpView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

// 2. Интервал между процессами (пояснение контейнер)
        LinearLayout linLayout_Flash_notes = new LinearLayout(this);
        linLayout_Flash_notes.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Flash_notes.setBackgroundColor(Color.rgb(192,192,192));
        linLayout_Flash_notes.setPadding(0,0,0,9);

// 2.1 Интервал между процессами (divider контейнер)
        LinearLayout linLayout_Flash_divider = new LinearLayout(this);
        linLayout_Flash_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Flash_divider.setPadding(1,5,5,5);
        linLayout_Flash_divider.setBackgroundColor(Color.rgb(192,192,192));

// 2.2 Интервал между процессами (название поля)
        TextView tv_process_delay = new TextView(this);
        tv_process_delay.setTypeface(Typeface.DEFAULT_BOLD);
        tv_process_delay.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_process_delay.setTextColor(Color.BLACK);
        tv_process_delay.setText(getResources().getString(R.string.pause_between_processes));
        tv_process_delay.setWidth((screenWidth - padding) / 100 * 80);
        tv_process_delay.setLayoutParams(lpView);
        tv_process_delay.setTypeface(Typeface.DEFAULT_BOLD);
        tv_process_delay.setPadding(1, 5, 5, 5);

        lpView_m1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_process_delay.getId());
        tv_process_delay.setLayoutParams(lpView_m1);
        //linLayout1.addView(Photo_Frequency);

        linLayout_process_delay.addView(tv_process_delay);

// 2.3 Интервал между процессами (ввод данных)
        process_delay = new EditText(this);
        process_delay.setLayoutParams(lpView_et);
        process_delay.setText(Integer.toString(fb.process_delay));
        process_delay.setTextColor(Color.rgb(50, 100, 150));
        ViewGroup.LayoutParams lp_process_delay = process_delay.getLayoutParams();
        lp_process_delay.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 80);
        process_delay.setLayoutParams(lp);
        process_delay.setGravity(Gravity.RIGHT);

        lpView_m2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, process_delay.getId());
        process_delay.setLayoutParams(lpView_m2);
        linLayout_process_delay.addView(process_delay);

// 2.4 Интервал между процессами (ввод notes)
        TextView tv_process_notes = new TextView(this);
        tv_process_notes.setTypeface(null, Typeface.NORMAL);
        tv_process_notes.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_process_notes.setTextColor(Color.BLACK);
        tv_process_notes.setText(getResources().getString(R.string.pause_between_processes_description));
                tv_process_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_process_notes.setLayoutParams(lpView);
      //  tv_process_notes.setTextColor(Color.GRAY);
        linLayout_Flash_notes.addView(tv_process_notes);

// 2.5 Интервал между процессами (divider)
        View line1 = new View(this);
        line1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line1.setBackgroundColor(Color.rgb(210, 210, 210));
        line1.getLayoutParams().height = 3;
        linLayout_Flash_divider.addView(line1);

// ------------------------------------------------------------------------------------------------

// 3. Шрифты (Config_Font_Size Container)
        RelativeLayout linLayout_config_font_size = new RelativeLayout(this);
      //  linLayout_config_font_size.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_config_font_size.setBackgroundColor(Color.rgb(192,192,192));

// 3.1 Шрифты (Log_Font_Size Container)
        RelativeLayout linLayout_log_font_size = new RelativeLayout(this);
      //  linLayout_config_log_font_size.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_log_font_size.setBackgroundColor(Color.rgb(192,192,192));

        RelativeLayout.LayoutParams lpView_m3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_m4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_m5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_m6 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

// 3.2 Шрифты (Horizontal LinearLayout)
        LinearLayout linLayout2 = new LinearLayout(this);
        linLayout2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams lpViewbutton1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        LinearLayout.LayoutParams lpViewbutton2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        linLayout2.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        linLayout2.setLayoutParams(lpView2);




        linLayout2.setBackgroundColor(Color.rgb(192,192,192));

// Шрифты (Config_Font_Size)
        TextView cfs = new TextView(this);
        cfs.setTypeface(Typeface.DEFAULT_BOLD);
        cfs.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        cfs.setTextColor(Color.BLACK);
        cfs.setText(getResources().getString(R.string.settings_font));
        cfs.setWidth((screenWidth - padding) / 100 * 80);
        cfs.setLayoutParams(lpView);
        cfs.setTypeface(Typeface.DEFAULT_BOLD);

        lpView_m3.addRule(RelativeLayout.ALIGN_PARENT_LEFT, cfs.getId());
        cfs.setLayoutParams(lpView_m3);
     //   linLayout_process_delay.addView(cfs);

        linLayout_config_font_size.addView(cfs);

// Шрифты (ввод данных)
        Config_Font_Size = new EditText(this);
        Config_Font_Size.setLayoutParams(lpView_et);
        Config_Font_Size.setText(Integer.toString(fb.Config_Font_Size));
        Config_Font_Size.setTextColor(Color.rgb(50,100,150));
        ViewGroup.LayoutParams lp_cfs = Config_Font_Size.getLayoutParams();
        lp_cfs.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 80);
        Config_Font_Size.setLayoutParams(lp_cfs);
        Config_Font_Size.setGravity(Gravity.RIGHT);

        lpView_m4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, Config_Font_Size.getId());
        Config_Font_Size.setLayoutParams(lpView_m4);
        linLayout_config_font_size.addView(Config_Font_Size);

// Log_Font_Size
        TextView lfs = new TextView(this);
        lfs.setTypeface(Typeface.DEFAULT_BOLD);
        lfs.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        lfs.setTextColor(Color.BLACK);
        lfs.setText(getResources().getString(R.string.log_font));
        lfs.setWidth((screenWidth - padding) / 100 * 80);
        lfs.setLayoutParams(lpView);
        lfs.setTypeface(Typeface.DEFAULT_BOLD);

        lpView_m5.addRule(RelativeLayout.ALIGN_PARENT_LEFT, lfs.getId());
        lfs.setLayoutParams(lpView_m5);
        //linLayout_config_font_size.addView(Config_Font_Size);

        linLayout_log_font_size.addView(lfs);

// Log_Font_Size
        Log_Font_Size = new EditText(this);
        Log_Font_Size.setLayoutParams(lpView_et);
        Log_Font_Size.setText(Integer.toString(fb.Log_Font_Size));
        Log_Font_Size.setTextColor(Color.rgb(50,100,150));
        ViewGroup.LayoutParams lp_lfs = Log_Font_Size.getLayoutParams();
        lp_lfs.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 80);
        Log_Font_Size.setLayoutParams(lp_lfs);
        Log_Font_Size.setGravity(Gravity.RIGHT);

        lpView_m6.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, Log_Font_Size.getId());
        Log_Font_Size.setLayoutParams(lpView_m6);
        //linLayout_config_font_size.addView(Config_Font_Size);

    //    linLayout_config_font_size.addView(Log_Font_Size);

        linLayout_log_font_size.addView(Log_Font_Size);

// ------------------------------------------------------------------------------------------------

// Buttons

// Container
        LinearLayout linLayout_Buttons = new LinearLayout(this);
        linLayout_Buttons.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Buttons.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        lpViewbutton1.setMargins(0, 0, 5, 0);
        LinearLayout.LayoutParams lpView3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        linLayout_Buttons.setLayoutParams(lpView3);
        linLayout_Buttons.setBackgroundColor(Color.rgb(192, 192, 192));
        linLayout_Buttons.setPadding(15, 15, 15, 15);

        linLayout_Buttons.setBaselineAligned(false);
        linLayout_Buttons.setGravity(Gravity.BOTTOM);



// Apply Button
        btn = new Button(this);
        btn.setText(getResources().getString(R.string.apply_button));
        btn.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        btn.setBackgroundColor(Color.rgb(90, 89, 91));
        btn.setTextColor(Color.rgb(250, 250, 250));
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        btn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn.setBackgroundColor(Color.rgb(90, 90, 90));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn.setBackgroundColor(Color.rgb(128, 128, 128));
                }
                return false;
            }

        });

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               // btn.setBackgroundColor(Color.rgb(228,228,228));

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putInt("Photo_Frequency", Integer.parseInt(Photo_Frequency.getText().toString()));
                editor.putInt("process_delay", Integer.parseInt(process_delay.getText().toString()));
                editor.putInt("Config_Font_Size", Integer.parseInt(Config_Font_Size.getText().toString()));
                editor.putInt("Log_Font_Size", Integer.parseInt(Log_Font_Size.getText().toString()));

// Save the changes in SharedPreferences
                editor.commit();
            }
        });

// GoTo Main Page Button
        btn_mp = new Button(this);
        btn_mp.setText(getResources().getString(R.string.back_button));
        btn_mp.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        btn_mp.setBackgroundColor(Color.rgb(90, 89, 91));
        btn_mp.setTextColor(Color.rgb(250, 250, 250));
       // lpViewbutton2.setMargins(5,5,5,5);
        btn_mp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        btn_mp.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn_mp.setBackgroundColor(Color.rgb(90, 90, 90));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn_mp.setBackgroundColor(Color.rgb(128, 128, 128));
                }
                return false;
            }

        });

        btn_mp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        linLayout_Buttons.addView(btn, lpViewbutton1);
        linLayout_Buttons.addView(btn_mp, lpViewbutton2);

// ------------------------------------------------------------------------------------------------

// Расставляем контейнеры (порядок важен)
        FullFrame.addView(linLayout1);
        FullFrame.addView(linLayout1_notes);
    //    FullFrame.addView(linLayout1_divider);
        FullFrame.addView(linLayout_process_delay);
        FullFrame.addView(linLayout_Flash_notes);
    //    FullFrame.addView(linLayout_Flash_divider);
        FullFrame.addView(linLayout_config_font_size);
        FullFrame.addView(linLayout_log_font_size);
        FullFrame.addView(linLayout2);
        FullFrame.addView(linLayout_Buttons);

        ScrollView m_Scroll = new ScrollView(this);
        m_Scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        m_Scroll.addView(FullFrame, new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));

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
