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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Tab_Main_Activity extends Activity {

    final String LOG_NETWORK_ACTIVITY = "Logs";
    Button btn, btn_mp;
    private CheckBox check_box_flash;
    private EditText edit_text_jpeg_compression;
    private int screenWidth, screenHeight;
    private int padding = 15;
    CheckBox checkBox_Clean_Log;
    CheckBox checkBox_Clean_SystemLog;
    CheckBox checkBox_Clean_Text;
    CheckBox checkBox_Attach_Log;
    CheckBox checkBox_Delete_Foto;
    EditText Photo_Frequency;
    EditText Config_Font_Size;
    EditText Log_Font_Size;
    EditText process_delay;
    EditText editText_Fotobot_Camera_Name;
    EditText editText_fbloglength;
    EditText editText_fbfloglength;
    EditText Wake_Up;
    EditText editText_Work_Dir;
    EditText editText_SMS_Password;
    Spinner spinner_ppm;
    ArrayAdapter<String> spinnerArrayAdapter1, spinnerArrayAdapter_Hardware;
    ArrayList<String> spinnerArray_ppm;
    LinearLayout linLayout_Work_Dir;
    LinearLayout linLayout_Work_Dir_note;
    final String LOG_TAG = "Logs";

    Logger fblogger = Logger.getLogger(FotoBot.class.getName());

    protected void onCreate(Bundle savedInstanceState) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        super.onCreate(savedInstanceState);
        fb.LoadSettings();

            //   Logger fblogger = Logger.getLogger(FotoBot.class.getName());
      //  fb.logger.fine("Tab_Main_Activity");

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
// Camera name

// camera name Container
        LinearLayout linLayout_Fotobot_Camera_Name = new LinearLayout(this);
        linLayout_Fotobot_Camera_Name.setOrientation(LinearLayout.VERTICAL);
        linLayout_Fotobot_Camera_Name.setPadding(5, 9, 5, 9);
        linLayout_Fotobot_Camera_Name.setBackgroundColor(Color.rgb(192, 192, 192));

// Название
        TextView tv_Fotobot_Camera_Name = new TextView(this);
        tv_Fotobot_Camera_Name.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Fotobot_Camera_Name.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Fotobot_Camera_Name.setTextColor(Color.BLACK);
        tv_Fotobot_Camera_Name.setText(getResources().getString(R.string.Camera_Name));
        linLayout_Fotobot_Camera_Name.addView(tv_Fotobot_Camera_Name);

// Camera Name
        editText_Fotobot_Camera_Name = new EditText(this);
        editText_Fotobot_Camera_Name.setSingleLine(true);
        editText_Fotobot_Camera_Name.setText(fb.Camera_Name);
        editText_Fotobot_Camera_Name.setTextColor(Color.rgb(50, 100, 150));
        linLayout_Fotobot_Camera_Name.addView(editText_Fotobot_Camera_Name);

// Заметка для названия камеры
        TextView tv_Fotobot_Camera_Name_note = new TextView(this);
        tv_Fotobot_Camera_Name_note.setTypeface(null, Typeface.NORMAL);
        tv_Fotobot_Camera_Name_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Fotobot_Camera_Name_note.setTextColor(Color.BLACK);
        tv_Fotobot_Camera_Name_note.setText(getResources().getString(R.string.Camera_Name_description));
        linLayout_Fotobot_Camera_Name.addView(tv_Fotobot_Camera_Name_note);

// ------------------------------------------------------------------------------------------------
// SMS password

// sms password Container
        LinearLayout linLayout_SMS_Password = new LinearLayout(this);
        linLayout_SMS_Password.setOrientation(LinearLayout.VERTICAL);
        linLayout_SMS_Password.setPadding(5, 9, 5, 9);
        linLayout_SMS_Password.setBackgroundColor(Color.rgb(208, 208, 208));

// Название
        TextView tv_SMS_Password = new TextView(this);
        tv_SMS_Password.setTypeface(Typeface.DEFAULT_BOLD);
        tv_SMS_Password.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_SMS_Password.setTextColor(Color.BLACK);
        tv_SMS_Password.setText(getResources().getString(R.string.SMS_Password));
        linLayout_SMS_Password.addView(tv_SMS_Password);

// Camera Name
        editText_SMS_Password = new EditText(this);
        editText_SMS_Password.setSingleLine(true);
        editText_SMS_Password.setText(fb.sms_passwd);
        editText_SMS_Password.setTextColor(Color.rgb(50, 100, 150));
        linLayout_SMS_Password.addView(editText_SMS_Password);

// Заметка для названия камеры
        TextView tv_SMS_Password_note = new TextView(this);
        tv_SMS_Password_note.setTypeface(null, Typeface.NORMAL);
        tv_SMS_Password_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_SMS_Password_note.setTextColor(Color.BLACK);
        tv_SMS_Password_note.setText(getResources().getString(R.string.SMS_Password_description));
        tv_SMS_Password_note.setPadding(5, 9, 5, 9);
        linLayout_SMS_Password.addView(tv_SMS_Password_note);

// ------------------------------------------------------------------------------------------------

// 1. Интервал между фото (Horizontal LinearLayout контейнер)
        LinearLayout linLayout1 = new LinearLayout(this);
        linLayout1.setOrientation(LinearLayout.VERTICAL);
        linLayout1.setPadding(5, 9, 5, 9);
        linLayout1.setBackgroundColor(Color.rgb(192, 192, 192));

// 1.3 Интервал между фото
        TextView tv = new TextView(this);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv.setTextColor(Color.BLACK);
        tv.setText(getResources().getString(R.string.pause_between_frames));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        linLayout1.addView(tv);

// 1.4 Интервал между фото (notes)
        TextView tv_notes = new TextView(this);
        tv_notes.setTypeface(null, Typeface.NORMAL);
        tv_notes.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_notes.setTextColor(Color.BLACK);
        tv_notes.setText(getResources().getString(R.string.pause_between_frames_description));
        tv_notes.setPadding(1, 5, 5, 5);
        linLayout1.addView(tv_notes);

// 1.5 Интервал между фото (ввод данных)
        Photo_Frequency = new EditText(this);
        Photo_Frequency.setTextColor(Color.rgb(50,100,150));
        Photo_Frequency.setText(Integer.toString(fb.Photo_Frequency));
        linLayout1.addView(Photo_Frequency);

// ------------------------------------------------------------------------------------------------

// Storage

// Контейнер для метода
        LinearLayout linLayout_Storage = new LinearLayout(this);
        linLayout1.setOrientation(LinearLayout.VERTICAL);
        linLayout1.setPadding(5, 9, 5, 9);
        linLayout1.setBackgroundColor(Color.rgb(192, 192, 192));

// Название
        TextView tv_Storage = new TextView(this);
        tv_Storage.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Storage.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Storage.setTextColor(Color.BLACK);
        tv_Storage.setText(getResources().getString(R.string.storage));
        tv_Storage.setTypeface(Typeface.DEFAULT_BOLD);
        linLayout_Storage.addView(tv_Storage);

// Список
        spinnerArray_ppm = new ArrayList<String>();
        spinnerArray_ppm.add("Internal");
        spinnerArray_ppm.add("External");

        spinner_ppm = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter_ppm = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerArray_ppm);
        spinner_ppm.setAdapter(spinnerArrayAdapter_ppm);
        spinner_ppm.setSelection(getIndex(spinner_ppm, fb.storage_type));
        spinner_ppm.setMinimumWidth((screenWidth - padding) / 100 * 50);
        spinner_ppm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {

                if (spinnerArray_ppm.get(i) == "Internal") {
                    linLayout_Work_Dir.setVisibility(View.GONE);
                    linLayout_Work_Dir_note.setVisibility(View.GONE);
                } else {
                    linLayout_Work_Dir.setVisibility(View.VISIBLE);
                    linLayout_Work_Dir_note.setVisibility(View.VISIBLE);
                }

            }

            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        linLayout_Storage.addView(spinner_ppm);

// Заметка для метода
        TextView tv_Storage_note = new TextView(this);
        tv_Storage_note.setTypeface(null, Typeface.NORMAL);
        tv_Storage_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Storage_note.setTextColor(Color.BLACK);
        tv_Storage_note.setText(getResources().getString(R.string.storage_description));
//        tv_Storage_note.setLayoutParams(lpView);
        tv_Storage_note.setPadding(5, 9, 5, 9);
        linLayout_Storage.addView(tv_Storage_note);


// ------------------------------------------------------------------------------------------------
// Work Dir

// Work Dir Container
        linLayout_Work_Dir = new LinearLayout(this);
        linLayout_Work_Dir.setOrientation(LinearLayout.VERTICAL);
        linLayout_Work_Dir.setPadding(5, 9, 5, 9);
        linLayout_Work_Dir.setBackgroundColor(Color.rgb(192, 192, 192));

// Название
        TextView tv_Work_Dir = new TextView(this);
        tv_Work_Dir.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Work_Dir.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Work_Dir.setTextColor(Color.BLACK);
        tv_Work_Dir.setText(getResources().getString(R.string.Work_Dir));
        linLayout_Work_Dir.addView(tv_Work_Dir);

// Work Dir
        editText_Work_Dir = new EditText(this);
        editText_Work_Dir.setSingleLine(true);
        editText_Work_Dir.setText(fb.work_dir);
        editText_Work_Dir.setTextColor(Color.rgb(50, 100, 150));
        linLayout_Work_Dir.addView(editText_Work_Dir);

// Заметка для названия камеры
        TextView tv_Work_Dir_note = new TextView(this);
        tv_Work_Dir_note.setTypeface(null, Typeface.NORMAL);
        tv_Work_Dir_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Work_Dir_note.setTextColor(Color.BLACK);
        tv_Work_Dir_note.setText(getResources().getString(R.string.Work_Dir_description));
        tv_Work_Dir_note.setPadding(5, 9, 5, 9);
        linLayout_Work_Dir.addView(tv_Work_Dir_note);

// ------------------------------------------------------------------------------------------------

// 2. Интервал между процессами (основной контейнер)
        RelativeLayout linLayout_process_delay = new RelativeLayout(this);
//        linLayout_process_delay.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_process_delay.setBackgroundColor(Color.rgb(192,192,192));
        lpView_m1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpView_m2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
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
     //   tv_process_delay.setLayoutParams(lpView);
        tv_process_delay.setTypeface(Typeface.DEFAULT_BOLD);
        tv_process_delay.setPadding(1, 5, 5, 5);

        lpView_m1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_process_delay.getId());
        tv_process_delay.setLayoutParams(lpView_m1);
        //linLayout1.addView(Photo_Frequency);

        linLayout_process_delay.addView(tv_process_delay);

// 2.3 Интервал между процессами (ввод данных)
        process_delay = new EditText(this);
      //  process_delay.setLayoutParams(lpView_et);
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
     //   tv_process_notes.setLayoutParams(lpView);
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
       // cfs.setLayoutParams(lpView);
        cfs.setTypeface(Typeface.DEFAULT_BOLD);

        lpView_m3.addRule(RelativeLayout.ALIGN_PARENT_LEFT, cfs.getId());
        cfs.setLayoutParams(lpView_m3);
     //   linLayout_process_delay.addView(cfs);

        linLayout_config_font_size.addView(cfs);

// Шрифты (ввод данных)
        Config_Font_Size = new EditText(this);
      //  Config_Font_Size.setLayoutParams(lpView_et);
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
      //  lfs.setLayoutParams(lpView);
        lfs.setTypeface(Typeface.DEFAULT_BOLD);

        lpView_m5.addRule(RelativeLayout.ALIGN_PARENT_LEFT, lfs.getId());
        lfs.setLayoutParams(lpView_m5);
        //linLayout_config_font_size.addView(Config_Font_Size);

        linLayout_log_font_size.addView(lfs);

// Log_Font_Size
        Log_Font_Size = new EditText(this);
      //  Log_Font_Size.setLayoutParams(lpView_et);
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

// Почистить лог

// Clean Log Container
        RelativeLayout linLayout_Clean_Log = new RelativeLayout(this);
        RelativeLayout.LayoutParams lpView_Clean_Log = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_Clean_Log_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et_Clean_Log = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Clean_Log.setBackgroundColor(Color.rgb(192,192,192));

// Clean Log TextView
        TextView tv_Clean_Log = new TextView(this);
        tv_Clean_Log.setText(getResources().getString(R.string.clean_log));
        tv_Clean_Log.setWidth((screenWidth - padding) / 100 * 90);
        tv_Clean_Log.setLayoutParams(lpView_Clean_Log);
        tv_Clean_Log.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Clean_Log.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Clean_Log.setTextColor(Color.BLACK);

        lpView_Clean_Log.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Clean_Log.getId());
        tv_Clean_Log.setLayoutParams(lpView_Clean_Log);
        linLayout_Clean_Log.addView(tv_Clean_Log);

// CheckBox
        checkBox_Clean_Log = new CheckBox(this);
        checkBox_Clean_Log.setChecked(false);

        lpView_Clean_Log_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, checkBox_Clean_Log.getId());
        checkBox_Clean_Log.setLayoutParams(lpView_Clean_Log_m);
        linLayout_Clean_Log.addView(checkBox_Clean_Log);

// Second Container (Horizontal LinearLayout)
        LinearLayout linLayout_Clean_Log_m = new LinearLayout(this);
        linLayout_Clean_Log_m.setOrientation(LinearLayout.HORIZONTAL);
     //   LinearLayout.LayoutParams lpView_Clean_Log_m = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
     //   LinearLayout.LayoutParams lpViewbutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        linLayout_Clean_Log_m.setGravity(Gravity.BOTTOM | Gravity.CENTER);


// Заметка для метода
        TextView tv_Clean_Log_note = new TextView(this);
        tv_Clean_Log_note.setTypeface(null, Typeface.NORMAL);
        tv_Clean_Log_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Clean_Log_note.setTextColor(Color.BLACK);
        tv_Clean_Log_note.setText(getResources().getString(R.string.clean_log_note));
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
       // tv_Clean_Log_note.setLayoutParams(lpView);
        //   tv_Photo_Processing_Method_note.setTextColor(Color.GRAY);
        tv_Clean_Log_note.setPadding(5, 9, 5, 9);
        linLayout_Clean_Log_m.addView(tv_Clean_Log_note);

// ------------------------------------------------------------------------------------------------

// Почистить вывод на экран

// Clean Log Container
        RelativeLayout linLayout_Clean_Text = new RelativeLayout(this);
        RelativeLayout.LayoutParams lpView_Clean_Text = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_Clean_Text_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et_Clean_Text = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Clean_Text.setBackgroundColor(Color.rgb(192,192,192));

// Clean Log TextView
        TextView tv_Clean_Text = new TextView(this);
        tv_Clean_Text.setText(getResources().getString(R.string.clean_text));
        tv_Clean_Text.setWidth((screenWidth - padding) / 100 * 90);
        tv_Clean_Text.setLayoutParams(lpView_Clean_Text);
        tv_Clean_Text.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Clean_Text.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Clean_Text.setTextColor(Color.BLACK);

        lpView_Clean_Text.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Clean_Text.getId());
        tv_Clean_Text.setLayoutParams(lpView_Clean_Text);
        linLayout_Clean_Text.addView(tv_Clean_Text);

// CheckBox
        checkBox_Clean_Text = new CheckBox(this);
        checkBox_Clean_Text.setChecked(false);

        lpView_Clean_Text_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, checkBox_Clean_Text.getId());
        checkBox_Clean_Text.setLayoutParams(lpView_Clean_Text_m);
        linLayout_Clean_Text.addView(checkBox_Clean_Text);

// Second Container (Horizontal LinearLayout)
        LinearLayout linLayout_Clean_Text_m = new LinearLayout(this);
        linLayout_Clean_Text_m.setOrientation(LinearLayout.HORIZONTAL);
        //   LinearLayout.LayoutParams lpView_Clean_Log_m = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //   LinearLayout.LayoutParams lpViewbutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        linLayout_Clean_Text_m.setGravity(Gravity.BOTTOM | Gravity.CENTER);


// Заметка для метода
        TextView tv_Clean_Text_note = new TextView(this);
        tv_Clean_Text_note.setTypeface(null, Typeface.NORMAL);
        tv_Clean_Text_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Clean_Text_note.setTextColor(Color.BLACK);
        tv_Clean_Text_note.setText(getResources().getString(R.string.clean_text_note));
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
       // tv_Clean_Text_note.setLayoutParams(lpView);
        //   tv_Photo_Processing_Method_note.setTextColor(Color.GRAY);
        tv_Clean_Text_note.setPadding(5, 9, 5, 9);
        linLayout_Clean_Text_m.addView(tv_Clean_Text_note);

// ------------------------------------------------------------------------------------------------

// Почистить журнал

// Clean Log Container
        RelativeLayout linLayout_Clean_SystemLog = new RelativeLayout(this);
        RelativeLayout.LayoutParams lpView_Clean_SystemLog = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_Clean_SystemLog_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et_Clean_SystemLog = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Clean_SystemLog.setBackgroundColor(Color.rgb(192,192,192));

// Clean SystemLog TextView
        TextView tv_Clean_SystemLog = new TextView(this);
        tv_Clean_SystemLog.setText(getResources().getString(R.string.clean_systemlog));
        tv_Clean_SystemLog.setWidth((screenWidth - padding) / 100 * 90);
        tv_Clean_SystemLog.setLayoutParams(lpView_Clean_SystemLog);
        tv_Clean_SystemLog.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Clean_SystemLog.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Clean_SystemLog.setTextColor(Color.BLACK);

        lpView_Clean_SystemLog.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Clean_SystemLog.getId());
        tv_Clean_SystemLog.setLayoutParams(lpView_Clean_SystemLog);
        linLayout_Clean_SystemLog.addView(tv_Clean_SystemLog);

// CheckBox
        checkBox_Clean_SystemLog = new CheckBox(this);
        checkBox_Clean_SystemLog.setChecked(false);

        lpView_Clean_SystemLog_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, checkBox_Clean_SystemLog.getId());
        checkBox_Clean_SystemLog.setLayoutParams(lpView_Clean_SystemLog_m);
        linLayout_Clean_SystemLog.addView(checkBox_Clean_SystemLog);

// Second Container (Horizontal LinearLayout)
        LinearLayout linLayout_Clean_SystemLog_m = new LinearLayout(this);
        linLayout_Clean_SystemLog_m.setOrientation(LinearLayout.HORIZONTAL);
        //   LinearLayout.LayoutParams lpView_Clean_Log_m = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //   LinearLayout.LayoutParams lpViewbutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        linLayout_Clean_SystemLog_m.setGravity(Gravity.BOTTOM | Gravity.CENTER);


// Заметка для метода
        TextView tv_Clean_SystemLog_note = new TextView(this);
        tv_Clean_SystemLog_note.setTypeface(null, Typeface.NORMAL);
        tv_Clean_SystemLog_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Clean_SystemLog_note.setTextColor(Color.BLACK);
        tv_Clean_SystemLog_note.setText(getResources().getString(R.string.clean_text_note));
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
      //  tv_Clean_SystemLog_note.setLayoutParams(lpView);
        //   tv_Photo_Processing_Method_note.setTextColor(Color.GRAY);
        tv_Clean_SystemLog_note.setPadding(5, 9, 5, 9);
        linLayout_Clean_SystemLog_m.addView(tv_Clean_SystemLog_note);

// ------------------------------------------------------------------------------------------------

// fb.log length

// fb.log length Container
        RelativeLayout linLayout_fbloglength = new RelativeLayout(this);
        RelativeLayout.LayoutParams lpView_fbloglength = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_fbloglength_m1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linLayout_fbloglength.setPadding(5, 9, 5, 9);
        linLayout_fbloglength.setBackgroundColor(Color.rgb(192, 192, 192));

// Пояснение контейнер
        LinearLayout linLayout_fbloglength_note = new LinearLayout(this);
        linLayout_fbloglength_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_fbloglength_note.setPadding(5, 9, 5, 9);
        linLayout_fbloglength_note.setBackgroundColor(Color.rgb(192, 192, 192));

// Контейнер для разделителя
        LinearLayout linLayout_fbloglength_divider = new LinearLayout(this);
        linLayout_fbloglength_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_fbloglength_divider.setPadding(5, 9, 5, 9);

// Название
        TextView tv_fbloglength = new TextView(this);
        tv_fbloglength.setTypeface(Typeface.DEFAULT_BOLD);
        tv_fbloglength.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_fbloglength.setTextColor(Color.BLACK);
        tv_fbloglength.setText(getResources().getString(R.string.fbloglength));
        tv_fbloglength.setMinimumWidth((screenWidth - padding) / 100 * 60);
    //    tv_fbloglength.setLayoutParams(lpView_camera_name);

        lpView_fbloglength.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_fbloglength.getId());
        tv_fbloglength.setLayoutParams(lpView_fbloglength);
        linLayout_fbloglength.addView(tv_fbloglength);

// fb.log length
        editText_fbloglength = new EditText(this);
        editText_fbloglength.setLayoutParams(lpView_fbloglength);
        editText_fbloglength.setSingleLine(true);
        editText_fbloglength.setText(Integer.toString(fb.loglength));
        editText_fbloglength.setTextColor(Color.rgb(50, 100, 150));
        editText_fbloglength.setWidth((screenWidth - padding) / 100 * 40);
        editText_fbloglength.setLayoutParams(lpView_fbloglength);
        editText_fbloglength.setGravity(Gravity.RIGHT);

        lpView_fbloglength_m1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, editText_fbloglength.getId());
        editText_fbloglength.setLayoutParams(lpView_fbloglength_m1);
        linLayout_fbloglength.addView(editText_fbloglength);

// Заметка для названия камеры
        TextView tv_fbloglength_note = new TextView(this);
        tv_fbloglength_note.setTypeface(null, Typeface.NORMAL);
        tv_fbloglength_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_fbloglength_note.setTextColor(Color.BLACK);
        tv_fbloglength_note.setText(getResources().getString(R.string.fbloglength_description));
        tv_fbloglength_note.setLayoutParams(lpView_fbloglength);
        tv_fbloglength_note.setPadding(5, 9, 5, 9);
        linLayout_fbloglength_note.addView(tv_fbloglength_note);

// ------------------------------------------------------------------------------------------------

// fbf.log length

// fbf.log length Container
        RelativeLayout linLayout_fbfloglength = new RelativeLayout(this);
        RelativeLayout.LayoutParams lpView_fbfloglength = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_fbfloglength_m1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linLayout_fbfloglength.setPadding(5, 9, 5, 9);
        linLayout_fbfloglength.setBackgroundColor(Color.rgb(192, 192, 192));

// Пояснение контейнер
        LinearLayout linLayout_fbfloglength_note = new LinearLayout(this);
        linLayout_fbfloglength_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_fbfloglength_note.setPadding(5, 9, 5, 9);
        linLayout_fbfloglength_note.setBackgroundColor(Color.rgb(192, 192, 192));

// Контейнер для разделителя
        LinearLayout linLayout_fbfloglength_divider = new LinearLayout(this);
        linLayout_fbfloglength_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_fbfloglength_divider.setPadding(5, 9, 5, 9);

// Название
        TextView tv_fbfloglength = new TextView(this);
        tv_fbfloglength.setTypeface(Typeface.DEFAULT_BOLD);
        tv_fbfloglength.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_fbfloglength.setTextColor(Color.BLACK);
        tv_fbfloglength.setText(getResources().getString(R.string.log_size));
        tv_fbfloglength.setMinimumWidth((screenWidth - padding) / 100 * 60);
    //    tv_fbfloglength.setLayoutParams(lpView_camera_name);

        lpView_fbfloglength.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_fbfloglength.getId());
        tv_fbloglength.setLayoutParams(lpView_fbloglength);
        linLayout_fbfloglength.addView(tv_fbfloglength);

// fb.log length
        editText_fbfloglength = new EditText(this);
        editText_fbfloglength.setLayoutParams(lpView_fbfloglength);
        editText_fbfloglength.setSingleLine(true);
        editText_fbfloglength.setText(Integer.toString(fb.log_size));
        editText_fbfloglength.setTextColor(Color.rgb(50, 100, 150));
        editText_fbfloglength.setWidth((screenWidth - padding) / 100 * 40);
        editText_fbfloglength.setLayoutParams(lpView_fbfloglength);
        editText_fbfloglength.setGravity(Gravity.RIGHT);

        lpView_fbfloglength_m1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, editText_fbfloglength.getId());
        editText_fbfloglength.setLayoutParams(lpView_fbfloglength_m1);
        linLayout_fbfloglength.addView(editText_fbfloglength);

// Заметка
        TextView tv_fbfloglength_note = new TextView(this);
        tv_fbfloglength_note.setTypeface(null, Typeface.NORMAL);
        tv_fbfloglength_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_fbfloglength_note.setTextColor(Color.BLACK);
        tv_fbfloglength_note.setText(getResources().getString(R.string.fbfloglength_description));
        tv_fbfloglength_note.setLayoutParams(lpView_fbfloglength);
        tv_fbfloglength_note.setPadding(5, 9, 5, 9);
        linLayout_fbfloglength_note.addView(tv_fbfloglength_note);

// ------------------------------------------------------------------------------------------------

// Wake Up (Horizontal LinearLayout контейнер)
        RelativeLayout linLayout_Wake_Up = new RelativeLayout(this);
        RelativeLayout.LayoutParams lpView_Wake_Up = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_Wake_Up_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_Wake_Up_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout1.setBackgroundColor(Color.rgb(192, 192, 192));


// Wake Up (пояснение контейнер)
        LinearLayout linLayout_Wake_Up_notes = new LinearLayout(this);
        linLayout_Wake_Up_notes.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Wake_Up_notes.setBackgroundColor(Color.rgb(192, 192, 192));
        linLayout_Wake_Up_notes.setPadding(0, 0, 0, 9);

// Wake Up
        TextView tv_Wake_Up = new TextView(this);
        tv_Wake_Up.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Wake_Up.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Wake_Up.setTextColor(Color.BLACK);
        tv_Wake_Up.setText(getResources().getString(R.string.wake_up));
        tv_Wake_Up.setWidth((screenWidth - padding) / 100 * 80);
       // tv_Wake_Up.setLayoutParams(lpView);
        tv_Wake_Up.setTypeface(Typeface.DEFAULT_BOLD);

        lpView_Wake_Up.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Wake_Up.getId());
        tv_Wake_Up.setLayoutParams(lpView_Wake_Up);
        linLayout_Wake_Up.addView(tv_Wake_Up);

// Wake Up (notes)
        TextView tv_Wake_Up_notes = new TextView(this);
        tv_Wake_Up_notes.setTypeface(null, Typeface.NORMAL);
        tv_Wake_Up_notes.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Wake_Up_notes.setTextColor(Color.BLACK);
        tv_Wake_Up_notes.setText(getResources().getString(R.string.wake_up_description));
        tv_Wake_Up_notes.setWidth((screenWidth - padding) / 100 * 99);
       // tv_Wake_Up_notes.setLayoutParams(lpView);
        tv_Wake_Up_notes.setPadding(1, 5, 5, 5);
        linLayout_Wake_Up_notes.addView(tv_Wake_Up_notes);

// Wake Up (ввод данных)
        Wake_Up = new EditText(this);
        Wake_Up.setLayoutParams(lpView_Wake_Up_et);
        Wake_Up.setTextColor(Color.rgb(50, 100, 150));
        Wake_Up.setText(Integer.toString(fb.wake_up_interval));
        ViewGroup.LayoutParams lp_Wake_Up = Wake_Up.getLayoutParams();
        lp_Wake_Up.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 80);
        Wake_Up.setLayoutParams(lp);
        Wake_Up.setGravity(Gravity.RIGHT);

        lpView_Wake_Up_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, Wake_Up.getId());
        Wake_Up.setLayoutParams(lpView_Wake_Up_m);
        linLayout_Wake_Up.addView(Wake_Up);

        // ------------------------------------------------------------------------------------------------

// Приаттачить лог

// Attach Log Container
        RelativeLayout linLayout_Attach_Log = new RelativeLayout(this);
        RelativeLayout.LayoutParams lpView_Attach_Log = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_Attach_Log_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et_Attach_Log = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Clean_Text.setBackgroundColor(Color.rgb(192,192,192));

// Attach Log TextView
        TextView tv_Attach_Log = new TextView(this);
        tv_Attach_Log.setText(getResources().getString(R.string.attach_log));
        tv_Attach_Log.setWidth((screenWidth - padding) / 100 * 90);
        tv_Attach_Log.setLayoutParams(lpView_Attach_Log);
        tv_Attach_Log.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Attach_Log.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Attach_Log.setTextColor(Color.BLACK);

        lpView_Attach_Log.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Attach_Log.getId());
        tv_Attach_Log.setLayoutParams(lpView_Attach_Log);
        linLayout_Attach_Log.addView(tv_Attach_Log);

// CheckBox
        checkBox_Attach_Log = new CheckBox(this);
        checkBox_Attach_Log.setChecked(fb.attach_log);

        lpView_Attach_Log_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, checkBox_Clean_Text.getId());
        checkBox_Attach_Log.setLayoutParams(lpView_Attach_Log_m);
        linLayout_Attach_Log.addView(checkBox_Attach_Log);

// ------------------------------------------------------------------------------------------------

// Удалить фото после отправки на почту

// Delete Foto Container
        RelativeLayout linLayout_Delete_Foto = new RelativeLayout(this);
        RelativeLayout.LayoutParams lpView_Delete_Foto = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_Delete_Foto_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et_Delete_Foto = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Delete_Foto.setBackgroundColor(Color.rgb(192,192,192));

// Delete Foto TextView
        TextView tv_Delete_Foto = new TextView(this);
        tv_Delete_Foto.setText(getResources().getString(R.string.Delete_Foto));
        tv_Delete_Foto.setWidth((screenWidth - padding) / 100 * 90);
        tv_Delete_Foto.setLayoutParams(lpView_Clean_Text);
        tv_Delete_Foto.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Delete_Foto.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Delete_Foto.setTextColor(Color.BLACK);

        lpView_Delete_Foto.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Delete_Foto.getId());
        tv_Delete_Foto.setLayoutParams(lpView_Delete_Foto);
        linLayout_Delete_Foto.addView(tv_Delete_Foto);

// CheckBox
        checkBox_Delete_Foto = new CheckBox(this);
        checkBox_Delete_Foto.setChecked(fb.delete_foto);

        lpView_Delete_Foto_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, checkBox_Delete_Foto.getId());
        checkBox_Delete_Foto.setLayoutParams(lpView_Delete_Foto_m);
        linLayout_Delete_Foto.addView(checkBox_Delete_Foto);

// Second Container (Horizontal LinearLayout)
        LinearLayout linLayout_Delete_Foto_m = new LinearLayout(this);
        linLayout_Delete_Foto_m.setOrientation(LinearLayout.HORIZONTAL);
        //   LinearLayout.LayoutParams lpView_Clean_Log_m = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //   LinearLayout.LayoutParams lpViewbutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        linLayout_Delete_Foto_m.setGravity(Gravity.BOTTOM | Gravity.CENTER);


// Заметка для метода
        TextView tv_Delete_Foto_note = new TextView(this);
        tv_Delete_Foto_note.setTypeface(null, Typeface.NORMAL);
        tv_Delete_Foto_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Delete_Foto_note.setTextColor(Color.BLACK);
        tv_Delete_Foto_note.setText(getResources().getString(R.string.Delete_Foto_description));
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
      //  tv_Delete_Foto_note.setLayoutParams(lpView);
        //   tv_Photo_Processing_Method_note.setTextColor(Color.GRAY);
        tv_Delete_Foto_note.setPadding(5, 9, 5, 9);
        linLayout_Delete_Foto_m.addView(tv_Delete_Foto_note);


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

                FileWriter log;

                if (checkBox_Clean_SystemLog.isChecked()) {

                        clearLog();

                }

                if (checkBox_Clean_Text.isChecked()) {
                      fb.clean_log = true;
                }

                if (checkBox_Attach_Log.isChecked()) {
                    fb.attach_log = true;
                    editor.putBoolean("Attach_Log", true);
                } else {
                    fb.attach_log = false;
                    editor.putBoolean("Attach_Log", false);
                }

                if (checkBox_Delete_Foto.isChecked()) {

                    fb.delete_foto = true;
                    editor.putBoolean("Delete_Foto", true);

                } else {

                    fb.delete_foto = false;
                    editor.putBoolean("Delete_Foto", false);

                }

                editor.putString("Camera_Name", editText_Fotobot_Camera_Name.getText().toString());
                editor.putString("SMS_Password", editText_SMS_Password.getText().toString());
                editor.putInt("Photo_Frequency", Integer.parseInt(Photo_Frequency.getText().toString()));
                editor.putInt("Wake_Up_Interval", Integer.parseInt(Wake_Up.getText().toString()));
                editor.putInt("process_delay", Integer.parseInt(process_delay.getText().toString()));
                editor.putInt("Config_Font_Size", Integer.parseInt(Config_Font_Size.getText().toString()));
                editor.putInt("Log_Font_Size", Integer.parseInt(Log_Font_Size.getText().toString()));
                editor.putInt("Log_Length", Integer.parseInt(editText_fbloglength.getText().toString()));
                editor.putInt("Log_Size", Integer.parseInt(editText_fbfloglength.getText().toString()));
                editor.putString("Work_Dir", editText_Work_Dir.getText().toString());
                editor.putString("Storage_Type", spinner_ppm.getSelectedItem().toString());

// Save the changes in SharedPreferences
                editor.commit();

//                fb.SendMessage("Default work dir: " + getApplicationContext().getFilesDir().toString());

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
               // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        linLayout_Buttons.addView(btn, lpViewbutton1);
        linLayout_Buttons.addView(btn_mp, lpViewbutton2);

// ------------------------------------------------------------------------------------------------

// Расставляем контейнеры (порядок важен)
        FullFrame.addView(linLayout_Fotobot_Camera_Name);
        FullFrame.addView(linLayout_SMS_Password);
        FullFrame.addView(linLayout_Storage);
        FullFrame.addView(linLayout_Work_Dir);
        FullFrame.addView(linLayout_Work_Dir_note);
        FullFrame.addView(linLayout_Delete_Foto);
        FullFrame.addView(linLayout_Delete_Foto_m);
        FullFrame.addView(linLayout1);
        FullFrame.addView(linLayout_process_delay);
        FullFrame.addView(linLayout_Flash_notes);
        FullFrame.addView(linLayout_fbloglength);
        FullFrame.addView(linLayout_fbloglength_note);
        FullFrame.addView(linLayout_Attach_Log);
        FullFrame.addView(linLayout_Clean_SystemLog);
        FullFrame.addView(linLayout_Clean_SystemLog_m);
        FullFrame.addView(linLayout_fbfloglength);
        FullFrame.addView(linLayout_Clean_Text);
        FullFrame.addView(linLayout_Clean_Text_m);
        FullFrame.addView(linLayout_config_font_size);
        FullFrame.addView(linLayout_log_font_size);
        FullFrame.addView(linLayout_Wake_Up);
        FullFrame.addView(linLayout_Wake_Up_notes);
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
        fb.LoadSettings();
        Photo_Frequency.setText(Integer.toString(fb.Photo_Frequency));
        process_delay.setText(Integer.toString(fb.process_delay));
    }

    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "Tab1: onRestart");
    }

    protected void onResume(SurfaceHolder holder) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.LoadSettings();
        Photo_Frequency.setText(Integer.toString(fb.Photo_Frequency));
        //  spinner1.setSelection(spinnerArrayAdapter1.getPosition(fb.Image_Scale));
        Log.d(LOG_TAG, "Tab1: onResume");
    }


    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "Tab1: onRestoreInstanceState");
    }

    public void clearLog(){
        try {
            Process process = new ProcessBuilder()
                    .command("logcat", "-c")
                    .redirectErrorStream(true)
                    .start();
        } catch (IOException e) {
        }
    }
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
}
