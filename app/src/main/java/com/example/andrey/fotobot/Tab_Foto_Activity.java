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

import java.util.ArrayList;

public class Tab_Foto_Activity  extends Activity {
    final String LOG_NETWORK_ACTIVITY = "Logs";
    private CheckBox check_box_flash;
    private EditText edit_text_jpeg_compression;
    private int screenWidth, screenHeight;
    private int padding = 15;
    CheckBox checkBox_Flash;
    EditText editText_JPEG_Compression, editText_Photo_Processing_Method;
    Spinner spinner_Hardware, spinner_ppm, spinner1;
    ArrayAdapter<String> spinnerArrayAdapter1, spinnerArrayAdapter_Hardware;
    ArrayList<String> spinnerArray_ppm;
    final String LOG_TAG = "Logs";
    TextView tv_Photo_Size_h, tv_Photo_Size_s;

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
        FullFrame.setPadding(5, padding, 0, 0);
        //  setContentView(FullFrame);

// ------------------------------------------------------------------------------------------------

// JPEG сжатие

// Контейнер для JPG сжатие
        LinearLayout linLayout_JPEG_Compression = new LinearLayout(this);
        linLayout_JPEG_Compression.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Контейнер для пояснение
        LinearLayout linLayout_JPEG_Compression_notes = new LinearLayout(this);
        linLayout_JPEG_Compression_notes.setOrientation(LinearLayout.HORIZONTAL);

// Контейнер для разделителя
        LinearLayout linLayout_JPEG_Compression_divider = new LinearLayout(this);
        linLayout_JPEG_Compression_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_JPEG_Compression_divider.setPadding(5, 15, 5, 15);

// Название
        TextView tv_JPEG_Compression = new TextView(this);
        tv_JPEG_Compression.setTypeface(Typeface.DEFAULT_BOLD);
        tv_JPEG_Compression.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_JPEG_Compression.setTextColor(Color.BLACK);
        tv_JPEG_Compression.setText("Степень сжатия JPEG");
        tv_JPEG_Compression.setWidth((screenWidth - padding) / 100 * 80);
        tv_JPEG_Compression.setLayoutParams(lpView);
        tv_JPEG_Compression.setTypeface(Typeface.DEFAULT_BOLD);
        linLayout_JPEG_Compression.addView(tv_JPEG_Compression);

// Ввод данных

        editText_JPEG_Compression = new EditText(this);
        editText_JPEG_Compression.setLayoutParams(lpView_et);
        String jpg = Integer.toString(fb.JPEG_Compression);
        editText_JPEG_Compression.setText(jpg);
        ViewGroup.LayoutParams lp = editText_JPEG_Compression.getLayoutParams();
        lp.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 80);
        editText_JPEG_Compression.setLayoutParams(lp);
        editText_JPEG_Compression.setGravity(Gravity.RIGHT);
        linLayout_JPEG_Compression.addView(editText_JPEG_Compression);

// Заметка для JPEG сжатия
        TextView tv_JPEG_Compression_note = new TextView(this);
        tv_JPEG_Compression_note.setTypeface(null, Typeface.ITALIC);
        tv_JPEG_Compression_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_JPEG_Compression_note.setTextColor(Color.BLACK);
        tv_JPEG_Compression_note.setText("Степень сжатия изображения, чем ближе к 100 тем выше качество изображения.");
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_JPEG_Compression_note.setLayoutParams(lpView);
        tv_JPEG_Compression_note.setTextColor(Color.GRAY);
        tv_JPEG_Compression_note.setPadding(5, 15, 5, 15);
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
        LinearLayout linLayout_Photo_Processing_Method = new LinearLayout(this);
        linLayout_Photo_Processing_Method.setOrientation(LinearLayout.HORIZONTAL);
//        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
  //      LinearLayout.LayoutParams lpView_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Контейнер для пояснение
        LinearLayout linLayout_Photo_Processing_Method_notes = new LinearLayout(this);
        linLayout_Photo_Processing_Method_notes.setOrientation(LinearLayout.HORIZONTAL);

// Контейнер для разделителя
        LinearLayout linLayout_Photo_Processing_Method_divider = new LinearLayout(this);
        linLayout_Photo_Processing_Method_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Photo_Processing_Method_divider.setPadding(5, 15, 5, 15);

// Название
        TextView tv_Photo_Processing_Method = new TextView(this);
        tv_Photo_Processing_Method.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Photo_Processing_Method.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Photo_Processing_Method.setTextColor(Color.BLACK);
        tv_Photo_Processing_Method.setText("Метод обработки фото");
        tv_Photo_Processing_Method.setWidth((screenWidth - padding) / 100 * 80);
        tv_Photo_Processing_Method.setLayoutParams(lpView);
        tv_Photo_Processing_Method.setTypeface(Typeface.DEFAULT_BOLD);
        linLayout_Photo_Processing_Method.addView(tv_Photo_Processing_Method);

// Список
        spinnerArray_ppm = new ArrayList<String>();
        spinnerArray_ppm.add("Hardware");
        spinnerArray_ppm.add("Software");

        spinner_ppm = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter_ppm = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray_ppm);
        spinner_ppm.setAdapter(spinnerArrayAdapter_ppm);
        spinner_ppm.setSelection(getIndex(spinner_ppm, fb.Photo_Post_Processing_Method));
        spinner_ppm.setMinimumWidth((screenWidth - padding) / 100 * 50);
        spinner_ppm.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {

                if (spinnerArray_ppm.get(i) == "Hardware") {
                    tv_Photo_Size_s.setVisibility(View.GONE);
                    spinner1.setVisibility(View.GONE);
                    tv_Photo_Size_h.setVisibility(View.VISIBLE);
                    spinner_Hardware.setVisibility(View.VISIBLE);

                } else {
                    tv_Photo_Size_s.setVisibility(View.VISIBLE);
                    spinner1.setVisibility(View.VISIBLE);
                    tv_Photo_Size_h.setVisibility(View.GONE);
                    spinner_Hardware.setVisibility(View.GONE);
                }



            }
            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });


        linLayout_Photo_Processing_Method.addView(spinner_ppm);


// Заметка для метода
        TextView tv_Photo_Processing_Method_note = new TextView(this);
        tv_Photo_Processing_Method_note.setTypeface(null, Typeface.ITALIC);
        tv_Photo_Processing_Method_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Photo_Processing_Method_note.setTextColor(Color.BLACK);
        tv_Photo_Processing_Method_note.setText("Камера обрабатывает изображение или софт.");
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_Photo_Processing_Method_note.setLayoutParams(lpView);
        tv_Photo_Processing_Method_note.setTextColor(Color.GRAY);
        tv_Photo_Processing_Method_note.setPadding(5, 15, 5, 15);
        linLayout_Photo_Processing_Method_notes.addView(tv_Photo_Processing_Method_note);

// ------------------------------------------------------------------------------------------------

// Параметры изображения

// Контейнер для метода
        LinearLayout linLayout_Photo_Size = new LinearLayout(this);
        linLayout_Photo_Size.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView_photo_size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_photo_size_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Контейнер для пояснение
        LinearLayout linLayout_Photo_Size_notes = new LinearLayout(this);
        linLayout_Photo_Processing_Method_notes.setOrientation(LinearLayout.HORIZONTAL);

// Контейнер для разделителя
        LinearLayout linLayout_Photo_Size_divider = new LinearLayout(this);
        linLayout_Photo_Processing_Method_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Photo_Processing_Method_divider.setPadding(5, 15, 5, 15);

// Масштаб фото
        tv_Photo_Size_h = new TextView(this);
        tv_Photo_Size_h.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Photo_Size_h.setTextSize(14);
        tv_Photo_Size_h.setTextColor(Color.BLACK);
        tv_Photo_Size_h.setText("Масштаб фото");
        tv_Photo_Size_h.setWidth((screenWidth - padding) / 100 * 50);
        tv_Photo_Size_h.setLayoutParams(lpView_photo_size);
        linLayout_Photo_Size.addView(tv_Photo_Size_h);

// Коэффициенты масштабирования
        ArrayList<String> spinnerArray_Hardware = new ArrayList<String>();
        spinnerArray_Hardware.add("1/16");
        spinnerArray_Hardware.add("1/8");
        spinnerArray_Hardware.add("1/4");
        spinnerArray_Hardware.add("1/2");
        spinnerArray_Hardware.add("1");

        spinner_Hardware = new Spinner(this);
        spinnerArrayAdapter_Hardware = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray_Hardware);
        spinner_Hardware.setAdapter(spinnerArrayAdapter_Hardware);
        //  spinner1.setSelection(spinnerArrayAdapter1.getPosition(fb.Image_Scale));
        spinner_Hardware.setSelection(getIndex(spinner_Hardware, fb.Image_Scale));
        spinner_Hardware.setMinimumWidth((screenWidth - padding) / 100 * 50);
        linLayout_Photo_Size.addView(spinner_Hardware);

// Размер фото
        tv_Photo_Size_s = new TextView(this);
        tv_Photo_Size_s.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Photo_Size_s.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Photo_Size_s.setTextColor(Color.BLACK);
        tv_Photo_Size_s.setText("Размер фото");
        tv_Photo_Size_s.setWidth((screenWidth - padding) / 100 * 50);
        tv_Photo_Size_s.setLayoutParams(lpView_photo_size);
        linLayout_Photo_Size.addView(tv_Photo_Size_s);

// Доступные разрешения
        ArrayList<String> spinnerArray = new ArrayList<String>();

        Camera.Size mSize = null;
        for (Camera.Size size : fb.camera_resolutions) {
            spinnerArray.add(size.width+"x"+size.height);

        }

        spinner1 = new Spinner(this);
        spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        spinner1.setAdapter(spinnerArrayAdapter1);

        spinner1.setSelection(getIndex(spinner1, fb.Image_Size));
        spinner1.setMinimumWidth((screenWidth - padding) / 100 * 50);
        linLayout_Photo_Size.addView(spinner1);


// Разделитель
        View line_Photo_Size = new View(this);
        line_Photo_Size.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line_Photo_Size.setBackgroundColor(Color.rgb(210, 210, 210));
        line_Photo_Size.getLayoutParams().height = 3;
        linLayout_Photo_Size_divider.addView(line_Photo_Size);







// Flash Container
        LinearLayout linLayout_Flash = new LinearLayout(this);
        linLayout_Flash.setOrientation(LinearLayout.HORIZONTAL);
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


// Flash TextView
        TextView tv_Flash = new TextView(this);
        tv_Flash.setText("Использовать вспышку");
        tv_Flash.setWidth((screenWidth - padding) / 100 * 90);
        tv_Flash.setLayoutParams(lpView_Flash);
        tv_Flash.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Flash.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Flash.setTextColor(Color.BLACK);
        linLayout_Flash.addView(tv_Flash);

// CheckBox
        checkBox_Flash = new CheckBox(this);
        checkBox_Flash.setChecked(fb.Use_Flash);
        linLayout_Flash.addView(checkBox_Flash);









// Apply Button
        Button btn = new Button(this);
        btn.setText("Применить");
        btn.setGravity(Gravity.CENTER);

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
                editor.putString("Image_Size", spinner1.getSelectedItem().toString());

// Save the changes in SharedPreferences
                editor.commit(); // commit changes
            }
        });

// GoTo Main Page Button
        Button btn_mp = new Button(this);
        btn_mp.setText("Выйти из настроек");
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

        FullFrame.addView(linLayout_JPEG_Compression);
        FullFrame.addView(linLayout_JPEG_Compression_notes);
        FullFrame.addView(linLayout_JPEG_Compression_divider);

        FullFrame.addView(linLayout_Photo_Processing_Method);
        FullFrame.addView(linLayout_Photo_Processing_Method_notes);

        FullFrame.addView(linLayout_Photo_Size);
        FullFrame.addView(linLayout_Photo_Size_divider);

        FullFrame.addView(linLayout_Flash);

        FullFrame.addView(linLayout2);


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
      //  spinner1.setSelection(spinnerArrayAdapter1.getPosition(fb.Image_Scale));
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
        spinner1.setSelection(getIndex(spinner1, fb.Image_Size));
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
