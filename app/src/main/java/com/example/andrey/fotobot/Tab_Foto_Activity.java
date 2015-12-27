package com.example.andrey.fotobot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
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

import java.util.ArrayList;

public class Tab_Foto_Activity  extends Activity {
    final String LOG_NETWORK_ACTIVITY = "Logs";
    private CheckBox check_box_flash;
    private EditText edit_text_jpeg_compression;
    private int screenWidth, screenHeight;
    private int padding = 15;
    Button btn, btn_mp;
    CheckBox checkBox_Flash;
    EditText editText_JPEG_Compression, editText_Photo_Processing_Method;
    Spinner spinner_Hardware, spinner_ppm, spinner_Software;
    ArrayAdapter<String> spinnerArrayAdapter1, spinnerArrayAdapter_Hardware;
    ArrayList<String> spinnerArray_ppm;
    final String LOG_TAG = "Logs";
    TextView tv_Photo_Size_h, tv_Photo_Size_s;
    LinearLayout linLayout_Photo_Size_h_notes, linLayout_Photo_Size_s_notes;

    protected void onCreate(Bundle savedInstanceState) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        super.onCreate(savedInstanceState);
        fb.LoadData();
        Log.d(LOG_TAG, "Tab3: onCreate");
        //      final FotoBot fb = (FotoBot) getApplicationContext();
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

// Main Container (Vertical LinearLayout)
        LinearLayout FullFrame = new LinearLayout(this);
        FullFrame.setOrientation(LinearLayout.VERTICAL);
        FullFrame.setPadding(5, 5, 5, 5);
        FullFrame.setBackgroundColor(Color.rgb(192,192,192));
        LinearLayout.LayoutParams lpFull_Frame = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        FullFrame.setLayoutParams(lpFull_Frame);
        FullFrame.setMinimumHeight(fb.Working_Area_Height - fb.menuheight);
       // FullFrame.setBackgroundColor(Color.WHITE);
        //  setContentView(FullFrame);

// ------------------------------------------------------------------------------------------------

// JPEG сжатие

// Контейнер для JPG сжатие
        RelativeLayout linLayout_JPEG_Compression = new RelativeLayout(this);
     //   linLayout_JPEG_Compression.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams lpView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout_JPEG_Compression.setBackgroundColor(Color.rgb(192,192,192));

// Контейнер для пояснение
        LinearLayout linLayout_JPEG_Compression_notes = new LinearLayout(this);
        linLayout_JPEG_Compression_notes.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_JPEG_Compression_notes.setBackgroundColor(Color.rgb(192,192,192));

// Контейнер для разделителя
        LinearLayout linLayout_JPEG_Compression_divider = new LinearLayout(this);
        linLayout_JPEG_Compression_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_JPEG_Compression_divider.setPadding(5, 9, 5, 9);

// Название
        TextView tv_JPEG_Compression = new TextView(this);
        tv_JPEG_Compression.setTypeface(Typeface.DEFAULT_BOLD);
        tv_JPEG_Compression.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_JPEG_Compression.setTextColor(Color.BLACK);
        tv_JPEG_Compression.setText("Степень сжатия JPEG");
        tv_JPEG_Compression.setWidth((screenWidth - padding) / 100 * 80);
        tv_JPEG_Compression.setLayoutParams(lpView);
        tv_JPEG_Compression.setTypeface(Typeface.DEFAULT_BOLD);

        lpView.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_JPEG_Compression.getId());
        tv_JPEG_Compression.setLayoutParams(lpView);
        linLayout_JPEG_Compression.addView(tv_JPEG_Compression);

// Ввод данных

        editText_JPEG_Compression = new EditText(this);
        editText_JPEG_Compression.setLayoutParams(lpView_et);
        String jpg = Integer.toString(fb.JPEG_Compression);
        editText_JPEG_Compression.setText(jpg);
        editText_JPEG_Compression.setTextColor(Color.rgb(50, 100, 150));
        ViewGroup.LayoutParams lp = editText_JPEG_Compression.getLayoutParams();
        lp.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 80);
        editText_JPEG_Compression.setLayoutParams(lp);
        editText_JPEG_Compression.setGravity(Gravity.RIGHT);

        lpView_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, editText_JPEG_Compression.getId());
        editText_JPEG_Compression.setLayoutParams(lpView_m);
        linLayout_JPEG_Compression.addView(editText_JPEG_Compression);

// Заметка для JPEG сжатия
        TextView tv_JPEG_Compression_note = new TextView(this);
        tv_JPEG_Compression_note.setTypeface(null, Typeface.NORMAL);
        tv_JPEG_Compression_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_JPEG_Compression_note.setTextColor(Color.BLACK);
        tv_JPEG_Compression_note.setText("Степень сжатия изображения, чем ближе к 100 тем выше качество изображения.");
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_JPEG_Compression_note.setLayoutParams(lpView);
      //  tv_JPEG_Compression_note.setTextColor(Color.GRAY);
        tv_JPEG_Compression_note.setPadding(5, 9, 5, 9);
        linLayout_JPEG_Compression_notes.addView(tv_JPEG_Compression_note);

// Разделитель
        View line_JPEG_Compression = new View(this);
        line_JPEG_Compression.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line_JPEG_Compression.setBackgroundColor(Color.rgb(210, 210, 210));
        line_JPEG_Compression.getLayoutParams().height = 3;
        linLayout_JPEG_Compression_divider.addView(line_JPEG_Compression);

// ------------------------------------------------------------------------------------------------

// Метод обработки фото

// Контейнер для метода
        RelativeLayout linLayout_Photo_Processing_Method = new RelativeLayout(this);
     //   linLayout_Photo_Processing_Method.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Photo_Processing_Method.setBackgroundColor(Color.rgb(192,192,192));
        RelativeLayout.LayoutParams lpView_m1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_m2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);



//        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
  //      LinearLayout.LayoutParams lpView_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Контейнер для пояснение
        LinearLayout linLayout_Photo_Processing_Method_notes = new LinearLayout(this);
        linLayout_Photo_Processing_Method_notes.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Photo_Processing_Method_notes.setBackgroundColor(Color.rgb(192,192,192));

// Контейнер для разделителя
        LinearLayout linLayout_Photo_Processing_Method_divider = new LinearLayout(this);
        linLayout_Photo_Processing_Method_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Photo_Processing_Method_divider.setPadding(5, 9, 5, 9);

// Название
        TextView tv_Photo_Processing_Method = new TextView(this);
        tv_Photo_Processing_Method.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Photo_Processing_Method.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Photo_Processing_Method.setTextColor(Color.BLACK);
        tv_Photo_Processing_Method.setText("Метод обработки фото");
        tv_Photo_Processing_Method.setWidth((screenWidth - padding) / 100 * 80);
        tv_Photo_Processing_Method.setLayoutParams(lpView);
        tv_Photo_Processing_Method.setTypeface(Typeface.DEFAULT_BOLD);

        lpView_m1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Photo_Processing_Method.getId());
        tv_Photo_Processing_Method.setLayoutParams(lpView_m1);
        linLayout_Photo_Processing_Method.addView(tv_Photo_Processing_Method);

// Список
        spinnerArray_ppm = new ArrayList<String>();
        spinnerArray_ppm.add("Hardware");
        spinnerArray_ppm.add("Software");

        spinner_ppm = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter_ppm = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerArray_ppm);
        spinner_ppm.setAdapter(spinnerArrayAdapter_ppm);
        spinner_ppm.setSelection(getIndex(spinner_ppm, fb.Photo_Post_Processing_Method));
        spinner_ppm.setMinimumWidth((screenWidth - padding) / 100 * 50);
        spinner_ppm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {

                if (spinnerArray_ppm.get(i) == "Hardware") {
                    tv_Photo_Size_s.setVisibility(View.GONE);
                    spinner_Software.setVisibility(View.GONE);
                    tv_Photo_Size_h.setVisibility(View.VISIBLE);
                    spinner_Hardware.setVisibility(View.VISIBLE);
                    linLayout_Photo_Size_h_notes.setVisibility(View.VISIBLE);
                    linLayout_Photo_Size_s_notes.setVisibility(View.GONE);
                } else {
                    tv_Photo_Size_s.setVisibility(View.VISIBLE);
                    spinner_Software.setVisibility(View.VISIBLE);
                    tv_Photo_Size_h.setVisibility(View.GONE);
                    spinner_Hardware.setVisibility(View.GONE);
                    linLayout_Photo_Size_h_notes.setVisibility(View.GONE);
                    linLayout_Photo_Size_s_notes.setVisibility(View.VISIBLE);
                }

            }

            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        lpView_m2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, spinner_ppm.getId());
        spinner_ppm.setLayoutParams(lpView_m2);
        linLayout_Photo_Processing_Method.addView(spinner_ppm);

// Заметка для метода
        TextView tv_Photo_Processing_Method_note = new TextView(this);
        tv_Photo_Processing_Method_note.setTypeface(null, Typeface.NORMAL);
        tv_Photo_Processing_Method_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Photo_Processing_Method_note.setTextColor(Color.BLACK);
        tv_Photo_Processing_Method_note.setText("Камера обрабатывает изображение или софт.");
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_Photo_Processing_Method_note.setLayoutParams(lpView);
     //   tv_Photo_Processing_Method_note.setTextColor(Color.GRAY);
        tv_Photo_Processing_Method_note.setPadding(5, 9, 5, 9);
        linLayout_Photo_Processing_Method_notes.addView(tv_Photo_Processing_Method_note);

// ------------------------------------------------------------------------------------------------

// Параметры изображения

// Контейнер для метода
        RelativeLayout linLayout_Photo_Size = new RelativeLayout(this);
       // linLayout_Photo_Size.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView_photo_size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_photo_size_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams lpView_m3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_m4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_m5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_m6 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


        linLayout_Photo_Size.setBackgroundColor(Color.rgb(192,192,192));

// Контейнер для пояснение
        linLayout_Photo_Size_h_notes = new LinearLayout(this);
        linLayout_Photo_Size_h_notes.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Photo_Size_h_notes.setBackgroundColor(Color.rgb(192,192,192));

// Контейнер для пояснение
        linLayout_Photo_Size_s_notes = new LinearLayout(this);
        linLayout_Photo_Size_s_notes.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Photo_Size_s_notes.setBackgroundColor(Color.rgb(192,192,192));

// Контейнер для разделителя
        LinearLayout linLayout_Photo_Size_divider = new LinearLayout(this);
        linLayout_Photo_Processing_Method_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Photo_Processing_Method_divider.setPadding(5, 9, 5, 9);

// Масштаб фото
        tv_Photo_Size_h = new TextView(this);
        tv_Photo_Size_h.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Photo_Size_h.setTextSize(14);
        tv_Photo_Size_h.setTextColor(Color.BLACK);
        tv_Photo_Size_h.setText("Масштаб фото");
        tv_Photo_Size_h.setWidth((screenWidth - padding) / 100 * 80);
        tv_Photo_Size_h.setLayoutParams(lpView_photo_size);

        lpView_m3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, tv_Photo_Size_h.getId());
        tv_Photo_Size_h.setLayoutParams(lpView_m3);
        linLayout_Photo_Size.addView(tv_Photo_Size_h);

// Коэффициенты масштабирования
        ArrayList<String> spinnerArray_Hardware = new ArrayList<String>();
        spinnerArray_Hardware.add("1/16");
        spinnerArray_Hardware.add("1/8");
        spinnerArray_Hardware.add("1/4");
        spinnerArray_Hardware.add("1/2");
        spinnerArray_Hardware.add("1");

        spinner_Hardware = new Spinner(this);
        spinnerArrayAdapter_Hardware = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerArray_Hardware);
        spinner_Hardware.setAdapter(spinnerArrayAdapter_Hardware);
        spinner_Hardware.setSelection(getIndex(spinner_Hardware, fb.Image_Scale));
        spinner_Hardware.setMinimumWidth((screenWidth - padding) / 100 * 20);
       // spinner_Hardware.setGravity(Gravity.RIGHT);

        lpView_m4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, spinner_Hardware.getId());
        spinner_Hardware.setLayoutParams(lpView_m4);
        linLayout_Photo_Size.addView(spinner_Hardware);

// Размер фото
        tv_Photo_Size_s = new TextView(this);
        tv_Photo_Size_s.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Photo_Size_s.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Photo_Size_s.setTextColor(Color.BLACK);
        tv_Photo_Size_s.setText("Размер фото");
        tv_Photo_Size_s.setWidth((screenWidth - padding) / 100 * 80);
        tv_Photo_Size_s.setLayoutParams(lpView_photo_size);

        lpView_m5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, tv_Photo_Size_s.getId());
        tv_Photo_Size_s.setLayoutParams(lpView_m5);
        linLayout_Photo_Size.addView(tv_Photo_Size_s);

// Доступные разрешения
        ArrayList<String> spinnerArray = new ArrayList<String>();

        Camera.Size mSize = null;
        for (Camera.Size size : fb.camera_resolutions) {
            spinnerArray.add(size.width+"x"+size.height);

        }

        spinner_Software = new Spinner(this);
        spinnerArrayAdapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerArray);
        spinner_Software.setAdapter(spinnerArrayAdapter1);

        spinner_Software.setSelection(getIndex(spinner_Software, fb.Image_Size));
        spinner_Software.setMinimumWidth((screenWidth - padding) / 100 * 20);
      //  spinner_Software.setGravity(Gravity.RIGHT);

        lpView_m6.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, spinner_Software.getId());
        spinner_Software.setLayoutParams(lpView_m6);
        linLayout_Photo_Size.addView(spinner_Software);

// Заметка для Hardware
        TextView tv_Photo_Size_h_note = new TextView(this);
        tv_Photo_Size_h_note.setTypeface(null, Typeface.NORMAL);
        tv_Photo_Size_h_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Photo_Size_h_note.setTextColor(Color.BLACK);
        tv_Photo_Size_h_note.setText("Hardware");
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_Photo_Size_h_note.setLayoutParams(lpView);
        //tv_Photo_Size_h_note.setTextColor(Color.GRAY);
        tv_Photo_Size_h_note.setPadding(5, 9, 5, 9);
        linLayout_Photo_Size_h_notes.addView(tv_Photo_Size_h_note);

// Заметка для Software
        TextView tv_Photo_Size_s_note = new TextView(this);
        tv_Photo_Size_s_note.setTypeface(null, Typeface.NORMAL);
        tv_Photo_Size_s_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Photo_Size_s_note.setTextColor(Color.BLACK);
        tv_Photo_Size_s_note.setText("Software");
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_Photo_Size_s_note.setLayoutParams(lpView);
     //   tv_Photo_Size_s_note.setTextColor(Color.GRAY);
        tv_Photo_Size_s_note.setPadding(5, 9, 5, 9);
        linLayout_Photo_Size_s_notes.addView(tv_Photo_Size_s_note);

// Разделитель
        View line_Photo_Size = new View(this);
        line_Photo_Size.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line_Photo_Size.setBackgroundColor(Color.rgb(210, 210, 210));
        line_Photo_Size.getLayoutParams().height = 3;
        linLayout_Photo_Size_divider.addView(line_Photo_Size);

// ------------------------------------------------------------------------------------------------

// Вспышка

// Flash Container
        RelativeLayout linLayout_Flash = new RelativeLayout(this);
        // linLayout_Flash.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams lpView_Flash = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_Flash_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et_Flash = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Flash.setBackgroundColor(Color.rgb(192,192,192));

// Flash TextView
        TextView tv_Flash = new TextView(this);
        tv_Flash.setText("Использовать вспышку");
        tv_Flash.setWidth((screenWidth - padding) / 100 * 90);
        tv_Flash.setLayoutParams(lpView_Flash);
        tv_Flash.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Flash.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Flash.setTextColor(Color.BLACK);

        lpView_Flash.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Flash.getId());
        tv_Flash.setLayoutParams(lpView_Flash);
        linLayout_Flash.addView(tv_Flash);

// CheckBox
        checkBox_Flash = new CheckBox(this);
        checkBox_Flash.setChecked(fb.Use_Flash);

        lpView_Flash_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, checkBox_Flash.getId());
        checkBox_Flash.setLayoutParams(lpView_Flash_m);
        linLayout_Flash.addView(checkBox_Flash);

// Second Container (Horizontal LinearLayout)
        LinearLayout linLayout2 = new LinearLayout(this);
        linLayout2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams lpViewbutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        linLayout2.setGravity(Gravity.BOTTOM | Gravity.CENTER);



        // linLayout2.setLayoutParams(lpView2);

// ------------------------------------------------------------------------------------------------

// Buttons

// Container
        LinearLayout linLayout_Buttons = new LinearLayout(this);
        linLayout_Buttons.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Buttons.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        LinearLayout.LayoutParams lpViewbutton1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        LinearLayout.LayoutParams lpViewbutton2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        lpViewbutton1.setMargins(0,0,5,0);
        LinearLayout.LayoutParams lpView3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        linLayout_Buttons.setLayoutParams(lpView3);
        linLayout_Buttons.setBackgroundColor(Color.rgb(192,192,192));
        linLayout_Buttons.setPadding(15, 15, 15, 15);

        linLayout_Buttons.setBaselineAligned(false);
        linLayout_Buttons.setGravity(Gravity.BOTTOM);

// Apply Button
        btn = new Button(this);
        btn.setText("ПРИМЕНИТЬ");
        btn.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        btn.setBackgroundColor(Color.rgb(90,89,91));
        btn.setTextColor(Color.rgb(250,250,250));
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        btn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    btn.setBackgroundColor(Color.rgb(90,90,90));
                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn.setBackgroundColor(Color.rgb(128,128,128));
                }
                return false;
            }

        });

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
                editor.putString("Photo_Post_Processing_Method", spinner_ppm.getSelectedItem().toString());
                editor.putInt("JPEG_Compression", Integer.parseInt(editText_JPEG_Compression.getText().toString()));
                editor.putString("Image_Scale", spinner_Hardware.getSelectedItem().toString());
                editor.putString("Image_Size", spinner_Software.getSelectedItem().toString());

// Save the changes in SharedPreferences
                editor.commit(); // commit changes
            }
        });

// GoTo Main Page Button
        btn_mp = new Button(this);
        btn_mp.setText("ВЕРНУТЬСЯ");
        btn_mp.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        btn_mp.setBackgroundColor(Color.rgb(90,89,91));
        btn_mp.setTextColor(Color.rgb(250,250,250));
        // lpViewbutton2.setMargins(5,5,5,5);
        btn_mp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        btn_mp.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    btn_mp.setBackgroundColor(Color.rgb(90,90,90));
                } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn_mp.setBackgroundColor(Color.rgb(128,128,128));
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

        FullFrame.addView(linLayout_JPEG_Compression);
        FullFrame.addView(linLayout_JPEG_Compression_notes);
     //   FullFrame.addView(linLayout_JPEG_Compression_divider);

        FullFrame.addView(linLayout_Photo_Processing_Method);
        FullFrame.addView(linLayout_Photo_Processing_Method_notes);

        FullFrame.addView(linLayout_Photo_Size);
        FullFrame.addView(linLayout_Photo_Size_h_notes);
        FullFrame.addView(linLayout_Photo_Size_s_notes);
//        FullFrame.addView(linLayout_Photo_Size_divider);

        FullFrame.addView(linLayout_Flash);

        FullFrame.addView(linLayout_Buttons);


        ScrollView m_Scroll = new ScrollView(this);
        m_Scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        m_Scroll.addView( FullFrame, new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT) );

        setContentView(m_Scroll);

    }

    protected void onResume(SurfaceHolder holder) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.LoadData();
        editText_JPEG_Compression.setText(Integer.toString(fb.JPEG_Compression));
        Log.d(LOG_TAG, "Tab3: onResume");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "Tab3: onRestoreInstanceState");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Tab3: onDestroy");
    }

    protected void onPause() {
        super.onPause();
        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.LoadData();
        // editText_JPEG_Compression.setText(Integer.toString(fb.JPEG_Compression));
        editText_JPEG_Compression.setText(Integer.toString(fb.JPEG_Compression));
       // spinner1.setSelection(getIndex(spinner1, fb.Image_Scale));
        spinner_Software.setSelection(getIndex(spinner_Software, fb.Image_Size));
        checkBox_Flash.setChecked(fb.Use_Flash);
        //   spinner1.setSelection(spinnerArrayAdapter1.getPosition(fb.Image_Scale));
      //  spinner1.setSelection(0);

        // releaseCamera();
        Log.d(LOG_TAG, "Tab3: onPause");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "Tab3: onRestart");
    }
    //private method of your class
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
