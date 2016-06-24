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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Tab_Main_Activity extends Activity {

    //  final String LOG_NETWORK_ACTIVITY = "Logs";
    Button btn, btn_mp;
    //   private CheckBox check_box_flash;
    //   private EditText edit_text_jpeg_compression;
    private int screenWidth, screenHeight;
    private int padding = 15;
    CheckBox checkBox_Clean_Log;
    CheckBox checkBox_Clean_SystemLog;
    CheckBox checkBox_Clean_Text;
    CheckBox checkBox_Attach_Log;
    CheckBox checkBox_Adv_Settings;
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
    //   ArrayAdapter<String> spinnerArrayAdapter1, spinnerArrayAdapter_Hardware;
    ArrayList<String> spinnerArray_ppm;
    LinearLayout linLayout_Work_Dir;

    LinearLayout linLayout_process_delay;
    LinearLayout linLayout_fbloglength;
    LinearLayout linLayout_Attach_Log;
    LinearLayout linLayout_Clean_SystemLog;
    LinearLayout linLayout_fbfloglength;
    LinearLayout linLayout_Clean_Text;
    LinearLayout linLayout_config_font_size;
    LinearLayout linLayout_log_font_size;
    LinearLayout linLayout_Wake_Up;

    //   LinearLayout linLayout_Work_Dir_note;
    final String LOG_TAG = "Logs";

    //  Logger fblogger = Logger.getLogger(FotoBot.class.getName());

    protected void onCreate(Bundle savedInstanceState) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        super.onCreate(savedInstanceState);

        fb.LoadSettings();

       // if ( fb.launched_first_time ) {
       //     fb.set_default_storage();
       //     fb.launched_first_time = false;
       // }

        //   Logger fblogger = Logger.getLogger(FotoBot.class.getName());
        //  fb.logger.fine("Tab_Main_Activity");

        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

// Main Container (Vertical LinearLayout)
// Главный контейнер внутри которого вся раскладка
        LinearLayout FullFrame = new LinearLayout(this);
        FullFrame.setOrientation(LinearLayout.VERTICAL);
        FullFrame.setPadding(0, 0, 0, 0);
        FullFrame.setBackgroundColor(Color.rgb(192, 192, 192));
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

// Интервал между фото (ввод данных)
        Photo_Frequency = new EditText(this);
        Photo_Frequency.setTextColor(Color.rgb(50, 100, 150));
        Photo_Frequency.setText(Integer.toString(fb.Photo_Frequency));
        linLayout1.addView(Photo_Frequency);


// Интервал между фото (notes)
        TextView tv_notes = new TextView(this);
        tv_notes.setTypeface(null, Typeface.NORMAL);
        tv_notes.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_notes.setTextColor(Color.BLACK);
        tv_notes.setText(getResources().getString(R.string.pause_between_frames_description));
        //   tv_notes.setPadding(1, 5, 5, 5);
        linLayout1.addView(tv_notes);


// ------------------------------------------------------------------------------------------------

// Storage

// Контейнер для метода
        LinearLayout linLayout_Storage = new LinearLayout(this);
        linLayout_Storage.setOrientation(LinearLayout.VERTICAL);
        linLayout_Storage.setPadding(5, 9, 5, 9);
        linLayout_Storage.setBackgroundColor(Color.rgb(192, 192, 192));

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
        //   spinner_ppm.setMinimumWidth((screenWidth - padding) / 100 * 50);
        spinner_ppm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {

                if (spinnerArray_ppm.get(i) == "Internal") {
                    linLayout_Work_Dir.setVisibility(View.GONE);
                    //   linLayout_Work_Dir_note.setVisibility(View.GONE);
                } else {
                    linLayout_Work_Dir.setVisibility(View.VISIBLE);
                    //  linLayout_Work_Dir_note.setVisibility(View.VISIBLE);
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
        linLayout_Work_Dir.addView(tv_Work_Dir_note);

// ------------------------------------------------------------------------------------------------

// 2. Интервал между процессами (основной контейнер)
        linLayout_process_delay = new LinearLayout(this);
        linLayout_process_delay.setOrientation(LinearLayout.VERTICAL);
        linLayout_process_delay.setPadding(5, 9, 5, 9);
        linLayout_process_delay.setBackgroundColor(Color.rgb(240, 150, 150));

// 2.2 Интервал между процессами (название поля)
        TextView tv_process_delay = new TextView(this);
        tv_process_delay.setTypeface(Typeface.DEFAULT_BOLD);
        tv_process_delay.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_process_delay.setTextColor(Color.BLACK);
        tv_process_delay.setText(getResources().getString(R.string.pause_between_processes));
        tv_process_delay.setTypeface(Typeface.DEFAULT_BOLD);
        linLayout_process_delay.addView(tv_process_delay);

// 2.3 Интервал между процессами (ввод данных)
        process_delay = new EditText(this);
        process_delay.setText(Integer.toString(fb.process_delay));
        process_delay.setTextColor(Color.rgb(50, 100, 150));
        linLayout_process_delay.addView(process_delay);

// 2.4 Интервал между процессами (ввод notes)
        TextView tv_process_notes = new TextView(this);
        tv_process_notes.setTypeface(null, Typeface.NORMAL);
        tv_process_notes.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_process_notes.setTextColor(Color.BLACK);
        tv_process_notes.setText(getResources().getString(R.string.pause_between_processes_description));
        linLayout_process_delay.addView(tv_process_notes);

// ------------------------------------------------------------------------------------------------

// 3. Шрифты (Config_Font_Size Container)
        linLayout_config_font_size = new LinearLayout(this);
        linLayout_config_font_size.setOrientation(LinearLayout.VERTICAL);
        linLayout_config_font_size.setPadding(5, 9, 5, 9);
        linLayout_config_font_size.setBackgroundColor(Color.rgb(240, 150, 150));

// Шрифты (Config_Font_Size)
        TextView cfs = new TextView(this);
        cfs.setTypeface(Typeface.DEFAULT_BOLD);
        cfs.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        cfs.setTextColor(Color.BLACK);
        cfs.setText(getResources().getString(R.string.settings_font));
        cfs.setTypeface(Typeface.DEFAULT_BOLD);
        linLayout_config_font_size.addView(cfs);

// Шрифты (ввод данных)
        Config_Font_Size = new EditText(this);
        Config_Font_Size.setText(Integer.toString(fb.Config_Font_Size));
        Config_Font_Size.setTextColor(Color.rgb(50, 100, 150));
        Config_Font_Size.setWidth((screenWidth - padding) - ((screenWidth - padding) / 2));
        linLayout_config_font_size.addView(Config_Font_Size);

// 3.1 Шрифты (Log_Font_Size Container)
        linLayout_log_font_size = new LinearLayout(this);
        linLayout_log_font_size.setOrientation(LinearLayout.VERTICAL);
        linLayout_log_font_size.setPadding(5, 9, 5, 9);
        linLayout_log_font_size.setBackgroundColor(Color.rgb(210, 129, 129));

// Log_Font_Size
        TextView lfs = new TextView(this);
        lfs.setTypeface(Typeface.DEFAULT_BOLD);
        lfs.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        lfs.setTextColor(Color.BLACK);
        lfs.setText(getResources().getString(R.string.log_font));
        lfs.setTypeface(Typeface.DEFAULT_BOLD);
        linLayout_log_font_size.addView(lfs);

// Log_Font_Size
        Log_Font_Size = new EditText(this);
        Log_Font_Size.setText(Integer.toString(fb.Log_Font_Size));
        Log_Font_Size.setTextColor(Color.rgb(50, 100, 150));
        Log_Font_Size.setWidth((screenWidth - padding) - ((screenWidth - padding) / 2));
        linLayout_log_font_size.addView(Log_Font_Size);

// ------------------------------------------------------------------------------------------------

// Почистить вывод на экран

// Clean Log Container
        linLayout_Clean_Text = new LinearLayout(this);
        linLayout_Clean_Text.setOrientation(LinearLayout.VERTICAL);
        linLayout_Clean_Text.setPadding(5, 9, 5, 9);
        linLayout_Clean_Text.setBackgroundColor(Color.rgb(210, 129, 129));

// Clean Log TextView
        TextView tv_Clean_Text = new TextView(this);
        tv_Clean_Text.setText(getResources().getString(R.string.clean_text));
        tv_Clean_Text.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Clean_Text.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Clean_Text.setTextColor(Color.BLACK);
        linLayout_Clean_Text.addView(tv_Clean_Text);

// CheckBox
        checkBox_Clean_Text = new CheckBox(this);
        checkBox_Clean_Text.setChecked(false);
        linLayout_Clean_Text.addView(checkBox_Clean_Text);

// Second Container (Horizontal LinearLayout)
        LinearLayout linLayout_Clean_Text_m = new LinearLayout(this);
        linLayout_Clean_Text_m.setOrientation(LinearLayout.HORIZONTAL);

// Заметка для метода
        TextView tv_Clean_Text_note = new TextView(this);
        tv_Clean_Text_note.setTypeface(null, Typeface.NORMAL);
        tv_Clean_Text_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Clean_Text_note.setTextColor(Color.BLACK);
        tv_Clean_Text_note.setText(getResources().getString(R.string.clean_text_note));
        linLayout_Clean_Text.addView(tv_Clean_Text_note);

// ------------------------------------------------------------------------------------------------

// Почистить журнал

// Clean Log Container
        linLayout_Clean_SystemLog = new LinearLayout(this);
        linLayout_Clean_SystemLog.setOrientation(LinearLayout.VERTICAL);
        linLayout_Clean_SystemLog.setPadding(5, 9, 5, 9);
        linLayout_Clean_SystemLog.setBackgroundColor(Color.rgb(210, 129, 129));

// Clean SystemLog TextView
        TextView tv_Clean_SystemLog = new TextView(this);
        tv_Clean_SystemLog.setText(getResources().getString(R.string.clean_systemlog));
        tv_Clean_SystemLog.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Clean_SystemLog.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Clean_SystemLog.setTextColor(Color.BLACK);
        linLayout_Clean_SystemLog.addView(tv_Clean_SystemLog);

// CheckBox
        checkBox_Clean_SystemLog = new CheckBox(this);
        checkBox_Clean_SystemLog.setChecked(false);
        linLayout_Clean_SystemLog.addView(checkBox_Clean_SystemLog);

// Заметка для метода
        TextView tv_Clean_SystemLog_note = new TextView(this);
        tv_Clean_SystemLog_note.setTypeface(null, Typeface.NORMAL);
        tv_Clean_SystemLog_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Clean_SystemLog_note.setTextColor(Color.BLACK);
        tv_Clean_SystemLog_note.setText(getResources().getString(R.string.clean_systemlog_note));
        linLayout_Clean_SystemLog.addView(tv_Clean_SystemLog_note);

// ------------------------------------------------------------------------------------------------

// fb.log length

// fb.log length Container
        linLayout_fbloglength = new LinearLayout(this);
        linLayout_fbloglength.setOrientation(LinearLayout.VERTICAL);
        linLayout_fbloglength.setPadding(5, 9, 5, 9);
        linLayout_fbloglength.setBackgroundColor(Color.rgb(210, 129, 129));

// Название
        TextView tv_fbloglength = new TextView(this);
        tv_fbloglength.setTypeface(Typeface.DEFAULT_BOLD);
        tv_fbloglength.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_fbloglength.setTextColor(Color.BLACK);
        tv_fbloglength.setText(getResources().getString(R.string.fbloglength));
        tv_fbloglength.setMinimumWidth((screenWidth - padding) / 100 * 60);

        linLayout_fbloglength.addView(tv_fbloglength);

// fb.log length
        editText_fbloglength = new EditText(this);
        editText_fbloglength.setSingleLine(true);
        editText_fbloglength.setText(Integer.toString(fb.loglength));
        editText_fbloglength.setTextColor(Color.rgb(50, 100, 150));
        editText_fbloglength.setWidth((screenWidth - padding) / 100 * 40);
        linLayout_fbloglength.addView(editText_fbloglength);

// Заметка для названия камеры
        TextView tv_fbloglength_note = new TextView(this);
        tv_fbloglength_note.setTypeface(null, Typeface.NORMAL);
        tv_fbloglength_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_fbloglength_note.setTextColor(Color.BLACK);
        tv_fbloglength_note.setText(getResources().getString(R.string.fbloglength_description));
        linLayout_fbloglength.addView(tv_fbloglength_note);

// ------------------------------------------------------------------------------------------------

// fbf.log length

// fbf.log length Container
        linLayout_fbfloglength = new LinearLayout(this);
        linLayout_fbfloglength.setOrientation(LinearLayout.VERTICAL);
        linLayout_fbfloglength.setPadding(5, 9, 5, 9);
        linLayout_fbfloglength.setBackgroundColor(Color.rgb(240, 150, 150));

// Название
        TextView tv_fbfloglength = new TextView(this);
        tv_fbfloglength.setTypeface(Typeface.DEFAULT_BOLD);
        tv_fbfloglength.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_fbfloglength.setTextColor(Color.BLACK);
        tv_fbfloglength.setText(getResources().getString(R.string.log_size));
        tv_fbfloglength.setMinimumWidth((screenWidth - padding) / 100 * 60);
        linLayout_fbfloglength.addView(tv_fbfloglength);

// fb.log length
        editText_fbfloglength = new EditText(this);
        editText_fbfloglength.setSingleLine(true);
        editText_fbfloglength.setText(Integer.toString(fb.log_size));
        editText_fbfloglength.setTextColor(Color.rgb(50, 100, 150));
        editText_fbfloglength.setWidth((screenWidth - padding) / 100 * 40);
        linLayout_fbfloglength.addView(editText_fbfloglength);

// ------------------------------------------------------------------------------------------------

// Wake Up (Horizontal LinearLayout контейнер)
        linLayout_Wake_Up = new LinearLayout(this);
        linLayout_Wake_Up.setOrientation(LinearLayout.VERTICAL);
        linLayout_Wake_Up.setPadding(5, 9, 5, 9);
        linLayout_Wake_Up.setBackgroundColor(Color.rgb(240, 150, 150));

// Wake Up
        TextView tv_Wake_Up = new TextView(this);
        tv_Wake_Up.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Wake_Up.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Wake_Up.setTextColor(Color.BLACK);
        tv_Wake_Up.setText(getResources().getString(R.string.wake_up));
        tv_Wake_Up.setWidth((screenWidth - padding) / 2);
        tv_Wake_Up.setTypeface(Typeface.DEFAULT_BOLD);
        linLayout_Wake_Up.addView(tv_Wake_Up);

// Wake Up (ввод данных)
        Wake_Up = new EditText(this);
        Wake_Up.setTextColor(Color.rgb(50, 100, 150));
        Wake_Up.setText(Integer.toString(fb.wake_up_interval));
        Wake_Up.setWidth((screenWidth - padding) - ((screenWidth - padding) / 2));
        linLayout_Wake_Up.addView(Wake_Up);

// Wake Up (notes)
        TextView tv_Wake_Up_notes = new TextView(this);
        tv_Wake_Up_notes.setTypeface(null, Typeface.NORMAL);
        tv_Wake_Up_notes.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Wake_Up_notes.setTextColor(Color.BLACK);
        tv_Wake_Up_notes.setText(getResources().getString(R.string.wake_up_description));
        tv_Wake_Up_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_Wake_Up_notes.setPadding(1, 5, 5, 5);
        linLayout_Wake_Up.addView(tv_Wake_Up_notes);



// ------------------------------------------------------------------------------------------------

// Приаттачить лог

// Attach Log Container
        linLayout_Attach_Log = new LinearLayout(this);
        linLayout_Attach_Log.setOrientation(LinearLayout.VERTICAL);
        linLayout_Attach_Log.setPadding(5, 9, 5, 9);
        linLayout_Attach_Log.setBackgroundColor(Color.rgb(240, 150, 150));

// Attach Log TextView
        TextView tv_Attach_Log = new TextView(this);
        tv_Attach_Log.setText(getResources().getString(R.string.attach_log));
        tv_Attach_Log.setWidth((screenWidth - padding) / 100 * 90);
        tv_Attach_Log.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Attach_Log.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Attach_Log.setTextColor(Color.BLACK);
        linLayout_Attach_Log.addView(tv_Attach_Log);

// CheckBox
        checkBox_Attach_Log = new CheckBox(this);
        checkBox_Attach_Log.setChecked(fb.attach_log);
        linLayout_Attach_Log.addView(checkBox_Attach_Log);

        // Wake Up (notes)
        TextView tv_Attach_Log_notes = new TextView(this);
        tv_Attach_Log_notes.setTypeface(null, Typeface.NORMAL);
        tv_Attach_Log_notes.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Attach_Log_notes.setTextColor(Color.BLACK);
        tv_Attach_Log_notes.setText(getResources().getString(R.string.attach_log_description));
        tv_Attach_Log_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_Attach_Log_notes.setPadding(1, 5, 5, 5);
        linLayout_Attach_Log.addView(tv_Attach_Log_notes);


// ------------------------------------------------------------------------------------------------

// Удалить фото после отправки на почту

// Delete Foto Container
        LinearLayout linLayout_Delete_Foto = new LinearLayout(this);
        linLayout_Delete_Foto.setOrientation(LinearLayout.VERTICAL);
        linLayout_Delete_Foto.setPadding(5, 9, 5, 9);
        linLayout_Delete_Foto.setBackgroundColor(Color.rgb(208, 208, 208));

// Delete Foto TextView
        TextView tv_Delete_Foto = new TextView(this);
        tv_Delete_Foto.setText(getResources().getString(R.string.Delete_Foto));
        tv_Delete_Foto.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Delete_Foto.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Delete_Foto.setTextColor(Color.BLACK);
        linLayout_Delete_Foto.addView(tv_Delete_Foto);

// CheckBox
        checkBox_Delete_Foto = new CheckBox(this);
        checkBox_Delete_Foto.setChecked(fb.delete_foto);
        linLayout_Delete_Foto.addView(checkBox_Delete_Foto);

// Заметка для метода
        TextView tv_Delete_Foto_note = new TextView(this);
        tv_Delete_Foto_note.setTypeface(null, Typeface.NORMAL);
        tv_Delete_Foto_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Delete_Foto_note.setTextColor(Color.BLACK);
        tv_Delete_Foto_note.setText(getResources().getString(R.string.Delete_Foto_description));
        linLayout_Delete_Foto.addView(tv_Delete_Foto_note);

// ------------------------------------------------------------------------------------------------

// Advanced Settings

// Advanced Settingd Container
        LinearLayout linLayout_Adv_Settings_Log = new LinearLayout(this);
        linLayout_Adv_Settings_Log.setOrientation(LinearLayout.VERTICAL);
        linLayout_Adv_Settings_Log.setPadding(5, 9, 5, 9);
        linLayout_Adv_Settings_Log.setBackgroundColor(Color.rgb(208, 208, 208));

// Attach Log TextView
        TextView tv_Adv_Settings = new TextView(this);
        tv_Adv_Settings.setText(getResources().getString(R.string.advanced_settings));
        tv_Adv_Settings.setWidth((screenWidth - padding) / 100 * 90);
        tv_Adv_Settings.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Adv_Settings.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Adv_Settings.setTextColor(Color.BLACK);
        linLayout_Adv_Settings_Log.addView(tv_Adv_Settings);

// CheckBox
        checkBox_Adv_Settings = new CheckBox(this);
        checkBox_Adv_Settings.setChecked(fb.advanced_settings);
        linLayout_Adv_Settings_Log.addView(checkBox_Adv_Settings);

        checkBox_Adv_Settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                   //if ( ((CheckBox)v).isChecked() ) {

                if (checkBox_Adv_Settings.isChecked()) {
                    fb.advanced_settings = true;
                    linLayout_process_delay.setVisibility(View.VISIBLE);
                    linLayout_fbloglength.setVisibility(View.VISIBLE);
                    linLayout_Attach_Log.setVisibility(View.VISIBLE);
                    linLayout_Clean_SystemLog.setVisibility(View.VISIBLE);
                    linLayout_fbfloglength.setVisibility(View.VISIBLE);
                    linLayout_Clean_Text.setVisibility(View.VISIBLE);
                    linLayout_config_font_size.setVisibility(View.VISIBLE);
                    linLayout_log_font_size.setVisibility(View.VISIBLE);
                    linLayout_Wake_Up.setVisibility(View.VISIBLE);
                } else {
                    fb.advanced_settings = false;
                    linLayout_process_delay.setVisibility(View.GONE);
                    linLayout_fbloglength.setVisibility(View.GONE);
                    linLayout_Attach_Log.setVisibility(View.GONE);
                    linLayout_Clean_SystemLog.setVisibility(View.GONE);
                    linLayout_fbfloglength.setVisibility(View.GONE);
                    linLayout_Clean_Text.setVisibility(View.GONE);
                    linLayout_config_font_size.setVisibility(View.GONE);
                    linLayout_log_font_size.setVisibility(View.GONE);
                    linLayout_Wake_Up.setVisibility(View.GONE);
                }

            }

            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });


// Buttons

// Container
        LinearLayout linLayout_Buttons = new LinearLayout(this);
        linLayout_Buttons.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Buttons.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        LinearLayout.LayoutParams lpViewbutton1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
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

                if (checkBox_Adv_Settings.isChecked()) {
                    fb.advanced_settings = true;
                    editor.putBoolean("Advanced_Settings", true);
                } else {
                    fb.advanced_settings = false;
                    editor.putBoolean("Advanced_Settings", false);
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
                fb.work_dir = editText_Work_Dir.getText().toString();
                editor.putString("Storage_Type", spinner_ppm.getSelectedItem().toString());
                editor.putBoolean("Launched_First_Time", fb.launched_first_time);


// Save the changes in SharedPreferences
                editor.commit();

                if (!check_working_dir(editText_Work_Dir.getText().toString())){
                    Toast.makeText(Tab_Main_Activity.this,
                            getResources().getString(R.string.Work_Dir_error1) + "\n" + editText_Work_Dir.getText().toString() + "\n" + getResources().getString(R.string.Work_Dir_error2), Toast.LENGTH_LONG).show();
                }

            }
        });

// GoTo Main Page Button
        btn_mp = new Button(this);
        btn_mp.setText(getResources().getString(R.string.back_button));
        btn_mp.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        btn_mp.setBackgroundColor(Color.rgb(90, 89, 91));
        btn_mp.setTextColor(Color.rgb(250, 250, 250));
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
        LinearLayout.LayoutParams lpViewbutton2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        linLayout_Buttons.addView(btn_mp, lpViewbutton2);

// ------------------------------------------------------------------------------------------------

// Расставляем контейнеры (порядок важен)
        FullFrame.addView(linLayout_Fotobot_Camera_Name);
        FullFrame.addView(linLayout_SMS_Password);
      //  FullFrame.addView(linLayout_Storage);
        FullFrame.addView(linLayout_Work_Dir);
        FullFrame.addView(linLayout_Delete_Foto);
        FullFrame.addView(linLayout1);
        FullFrame.addView(linLayout_Adv_Settings_Log);


        FullFrame.addView(linLayout_process_delay);
        FullFrame.addView(linLayout_fbloglength);
        FullFrame.addView(linLayout_Attach_Log);
        FullFrame.addView(linLayout_Clean_SystemLog);
        FullFrame.addView(linLayout_fbfloglength);
        FullFrame.addView(linLayout_Clean_Text);
        FullFrame.addView(linLayout_config_font_size);
        FullFrame.addView(linLayout_log_font_size);
        FullFrame.addView(linLayout_Wake_Up);

        if ( !fb.advanced_settings ) {
            linLayout_process_delay.setVisibility(View.GONE);
            linLayout_fbloglength.setVisibility(View.GONE);
            linLayout_Attach_Log.setVisibility(View.GONE);
            linLayout_Clean_SystemLog.setVisibility(View.GONE);
            linLayout_fbfloglength.setVisibility(View.GONE);
            linLayout_Clean_Text.setVisibility(View.GONE);
            linLayout_config_font_size.setVisibility(View.GONE);
            linLayout_log_font_size.setVisibility(View.GONE);
            linLayout_Wake_Up.setVisibility(View.GONE);
        }

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

    public void clearLog() {
        try {
            Process process = new ProcessBuilder()
                    .command("logcat", "-c")
                    .redirectErrorStream(true)
                    .start();
        } catch (IOException e) {
        }
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private boolean check_working_dir(String str) {

        File dir = new File(str);

        boolean exists = dir.exists();

        return exists;

    }

}
