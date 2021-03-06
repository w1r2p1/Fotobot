/*
Copyright (C) 2017 Andrey Voronin

This file is part of Fotobot.

    Fotobot is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Fotobot is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Fotobot.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.droid.app.fotobot;

import android.app.Activity;
import android.content.Context;
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
import android.view.LayoutInflater;
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

import java.util.ArrayList;

public class Tab_Foto_Activity extends Activity {

    private int screenWidth, screenHeight;
    private int padding = 15;
    Button btn, btn_mp;
    CheckBox checkBox_Flash;
    CheckBox checkBox_bc;
    CheckBox checkBox_fc;
    CheckBox checkBox_Autofocus;
    CheckBox checkBox_Attach;
    CheckBox checkBox_Delete;
    CheckBox checkBox_fc_Attach;
    CheckBox checkBox_fc_Delete;
    EditText editText_JPEG_Compression;
    EditText editText_Autofocus;
    Spinner spinner_Hardware, spinner_ppm, spinner_Software;
    Spinner fc_spinner_Software;
    ArrayAdapter<String> spinnerArrayAdapter1, spinnerArrayAdapter_Hardware;
    ArrayAdapter<String> spinnerArrayAdapter2;
    ArrayAdapter<String> fc_spinnerArrayAdapter1;
    ArrayList<String> spinnerArray_ppm;
    ArrayList<String> spinnerArray;
    final String LOG_TAG = "Logs";
    TextView tv_Photo_Size_h, tv_Photo_Size_s;
    TextView fc_tv_Photo_Size_s;
    TextView tv_Photo_Size_h_note;
    TextView tv_Photo_Size_s_note;

    LinearLayout linLayout_Flash;
    LinearLayout linLayout_Autofocus;
    LinearLayout linLayout_Attach;
    LinearLayout linLayout_Delete;

    LinearLayout linLayout_fc;
    LinearLayout linLayout_camera;
    LinearLayout linLayout_fc_Attach;

    LinearLayout linLayout_fc_Delete;

    int bc_camera_color = Color.rgb(205, 196, 210);
    int fc_camera_color = Color.rgb(184, 199, 210);

    protected void onCreate(Bundle savedInstanceState) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        super.onCreate(savedInstanceState);
        fb.LoadSettings();

        Log.d(LOG_TAG, "Tab3: onCreate");
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

// Main Container (Vertical LinearLayout)
        LinearLayout FullFrame = new LinearLayout(this);
        FullFrame.setOrientation(LinearLayout.VERTICAL);
        FullFrame.setPadding(0, 0, 0, 0);
        FullFrame.setBackgroundColor(Color.rgb(192, 192, 192));
        LinearLayout.LayoutParams lpFull_Frame = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        FullFrame.setLayoutParams(lpFull_Frame);
        FullFrame.setMinimumHeight(fb.Working_Area_Height - fb.menuheight);

// ------------------------------------------------------------------------------------------------

// JPEG сжатие

// Контейнер для JPG сжатие
        LinearLayout linLayout_JPEG_Compression = new LinearLayout(this);
        linLayout_JPEG_Compression.setOrientation(LinearLayout.VERTICAL);
        linLayout_JPEG_Compression.setPadding(5, 9, 5, 9);
        if ( fb.front_camera) {
            linLayout_JPEG_Compression.setBackgroundColor(Color.rgb(192,192,192));
        } else {
            linLayout_JPEG_Compression.setBackgroundColor(Color.rgb(208,208,208));
        }

// Название
        TextView tv_JPEG_Compression = new TextView(this);
        tv_JPEG_Compression.setTypeface(Typeface.DEFAULT_BOLD);
        tv_JPEG_Compression.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_JPEG_Compression.setTextColor(Color.BLACK);
        tv_JPEG_Compression.setText(getResources().getString(R.string.jpeg_compression));
        tv_JPEG_Compression.setTypeface(Typeface.DEFAULT_BOLD);
        linLayout_JPEG_Compression.addView(tv_JPEG_Compression);

// Ввод данных
        editText_JPEG_Compression = new EditText(this);
        String jpg = Integer.toString(fb.JPEG_Compression);
        editText_JPEG_Compression.setText(jpg);
        editText_JPEG_Compression.setTextColor(Color.rgb(50, 100, 150));
        linLayout_JPEG_Compression.addView(editText_JPEG_Compression);

// Заметка для JPEG сжатия
        TextView tv_JPEG_Compression_note = new TextView(this);
        tv_JPEG_Compression_note.setTypeface(null, Typeface.NORMAL);
        tv_JPEG_Compression_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_JPEG_Compression_note.setTextColor(Color.BLACK);
        tv_JPEG_Compression_note.setText(getResources().getString(R.string.jpeg_compression_description));
        linLayout_JPEG_Compression.addView(tv_JPEG_Compression_note);

// ------------------------------------------------------------------------------------------------

// Параметры изображения

// Контейнер для метода
        LinearLayout linLayout_Photo_Size = new LinearLayout(this);
        linLayout_Photo_Size.setOrientation(LinearLayout.VERTICAL);
        linLayout_Photo_Size.setPadding(5, 9, 5, 9);
        linLayout_Photo_Size.setBackgroundColor(Color.rgb(208, 208, 208));

// Масштаб фото
        tv_Photo_Size_h = new TextView(this);
        tv_Photo_Size_h.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Photo_Size_h.setTextSize(14);
        tv_Photo_Size_h.setTextColor(Color.BLACK);
        tv_Photo_Size_h.setText("Hardware");
    //    linLayout_Photo_Size.addView(tv_Photo_Size_h);

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
        linLayout_Photo_Size.addView(spinner_Hardware);

        // Заметка для Hardware
        tv_Photo_Size_h_note = new TextView(this);
        tv_Photo_Size_h_note.setTypeface(null, Typeface.NORMAL);
        tv_Photo_Size_h_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Photo_Size_h_note.setTextColor(Color.BLACK);
        tv_Photo_Size_h_note.setText(getResources().getString(R.string.photo_scale));
        linLayout_Photo_Size.addView(tv_Photo_Size_h_note);

// Размер фото
        tv_Photo_Size_s = new TextView(this);
        tv_Photo_Size_s.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Photo_Size_s.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Photo_Size_s.setTextColor(Color.BLACK);
        tv_Photo_Size_s.setText("Software");
//        linLayout_Photo_Size.addView(tv_Photo_Size_s);



// камера
// Контейнер для камеры
        linLayout_camera = new LinearLayout(this);
        linLayout_camera.setOrientation(LinearLayout.VERTICAL);
        linLayout_camera.setPadding(5, 9, 5, 9);
        linLayout_camera.setBackgroundColor(bc_camera_color);
// Название
        TextView tv_bc = new TextView(this);
        tv_bc.setTypeface(Typeface.DEFAULT_BOLD);
        tv_bc.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size + 4);
        tv_bc.setTextColor(Color.BLACK);
        tv_bc.setText(getResources().getString(R.string.Back_Camera));
        tv_bc.setTypeface(Typeface.DEFAULT_BOLD);
        linLayout_camera.addView(tv_bc);

// Доступные разрешения
        spinnerArray = new ArrayList<String>();

        Camera.Size mSize = null;

        int fe_w = (int) fb.camera_resolutions.get(0).width;
        int fe_h = (int) fb.camera_resolutions.get(0).height;
        float fe_s, fe_z;

        fe_z = (float) fe_w / (float) fe_h;

        for (Camera.Size size : fb.camera_resolutions) {
            fe_w = (int) size.width;
            fe_h = (int) size.height;
            fe_s = (float) fe_w / (float) fe_h;

         //   if (Math.abs(fe_s - fe_z) < 0.01f) {
                spinnerArray.add(size.width + "x" + size.height);
         //   }

        }

        spinner_Software = new Spinner(this);
  //      spinnerArrayAdapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerArray);
      //  CustomAdapter customAdapter=new CustomAdapter(this, R.layout.spinner_item, spinnerArray);
        //Spinner mySpinner = (Spinner)findViewById(R.id.spinner);
        spinner_Software.setAdapter(new MyAdapter(this, R.layout.spinner_item, spinnerArray));
       // spinner_Software.setAdapter(customAdapter);
      //  spinner_Software.setAdapter(spinnerArrayAdapter1);
        spinner_Software.setSelection(getIndex(spinner_Software, fb.Image_Size));
        linLayout_camera.addView(spinner_Software);

// Заметка для Software
        tv_Photo_Size_s_note = new TextView(this);
        tv_Photo_Size_s_note.setTypeface(null, Typeface.NORMAL);
        tv_Photo_Size_s_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Photo_Size_s_note.setTextColor(Color.BLACK);
        tv_Photo_Size_s_note.setText(getResources().getString(R.string.photo_resolution));
        linLayout_camera.addView(tv_Photo_Size_s_note);

// Использовать камеру
        TextView tv_use_bc = new TextView(this);
        tv_use_bc.setText(getResources().getString(R.string.use_bc_camera));
        tv_use_bc.setWidth((screenWidth - padding) / 100 * 90);
        tv_use_bc.setTypeface(Typeface.DEFAULT_BOLD);
        tv_use_bc.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_use_bc.setTextColor(Color.BLACK);
        linLayout_camera.addView(tv_use_bc);

// CheckBox
        checkBox_bc = new CheckBox(this);
        checkBox_bc.setChecked(fb.make_photo_bc);
        linLayout_camera.addView(checkBox_bc);

        checkBox_bc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (checkBox_bc.isChecked()) {
                    fb.make_photo_bc = true;
                    linLayout_Flash.setVisibility(View.VISIBLE);
                    linLayout_Autofocus.setVisibility(View.VISIBLE);
                    linLayout_Attach.setVisibility(View.VISIBLE);
                    linLayout_Delete.setVisibility(View.VISIBLE);
                } else {
                    fb.make_photo_bc = false;
                    linLayout_Flash.setVisibility(View.GONE);
                    linLayout_Autofocus.setVisibility(View.GONE);
                    linLayout_Attach.setVisibility(View.GONE);
                    linLayout_Delete.setVisibility(View.GONE);
                }

            }

            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

// Вспышка

// Flash Container
        linLayout_Flash = new LinearLayout(this);
        linLayout_Flash.setOrientation(LinearLayout.VERTICAL);
        linLayout_Flash.setPadding(5, 9, 5, 9);
        linLayout_Flash.setBackgroundColor(bc_camera_color);

// Flash TextView
        TextView tv_Flash = new TextView(this);
        tv_Flash.setText(getResources().getString(R.string.flash));
        tv_Flash.setWidth((screenWidth - padding) / 100 * 90);
        tv_Flash.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Flash.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Flash.setTextColor(Color.BLACK);
        linLayout_Flash.addView(tv_Flash);
//        linLayout_camera.addView(tv_Flash);

// CheckBox
        checkBox_Flash = new CheckBox(this);
        checkBox_Flash.setChecked(fb.Use_Flash);
        linLayout_Flash.addView(checkBox_Flash);
//        linLayout_camera.addView(checkBox_Flash);

// ------------------------------------------------------------------------------------------------
// Autofocus

// Autofocus Container
        linLayout_Autofocus = new LinearLayout(this);
        linLayout_Autofocus.setOrientation(LinearLayout.VERTICAL);
        linLayout_Autofocus.setPadding(5, 9, 5, 9);
        linLayout_Autofocus.setBackgroundColor(bc_camera_color);

// Autofocus TextView
        TextView tv_Autofocus = new TextView(this);
        tv_Autofocus.setText(getResources().getString(R.string.Autofocus));
        tv_Autofocus.setWidth((screenWidth - padding) / 100 * 90);
        tv_Autofocus.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Autofocus.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Autofocus.setTextColor(Color.BLACK);

        linLayout_Autofocus.addView(tv_Autofocus);

// CheckBox
        checkBox_Autofocus = new CheckBox(this);
        checkBox_Autofocus.setChecked(fb.use_autofocus);
        linLayout_Autofocus.addView(checkBox_Autofocus);

// Time for focusing
        final TextView tv_Time_for_Focusing = new TextView(this);
        tv_Time_for_Focusing.setText(getResources().getString(R.string.Time_for_Focusing));
        tv_Time_for_Focusing.setWidth((screenWidth - padding) / 100 * 90);
        tv_Time_for_Focusing.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Time_for_Focusing.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Time_for_Focusing.setTextColor(Color.BLACK);

        linLayout_Autofocus.addView(tv_Time_for_Focusing);

        // Ввод данных
        editText_Autofocus = new EditText(this);
        editText_Autofocus.setText(Integer.toString(fb.time_for_focusing));
        editText_Autofocus.setTextColor(Color.rgb(50, 100, 150));

        // if (fb.use_autofocus) {
        linLayout_Autofocus.addView(editText_Autofocus);
        // }

        //   linLayout_Autofocus.addView(tv_Time_for_Focusing);

        if ( fb.use_autofocus ) {
            tv_Time_for_Focusing.setVisibility(View.VISIBLE);
            editText_Autofocus.setVisibility(View.VISIBLE);
        } else {
            tv_Time_for_Focusing.setVisibility(View.GONE);
            editText_Autofocus.setVisibility(View.GONE);
        }

        checkBox_Autofocus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (checkBox_Autofocus.isChecked()) {
                    fb.use_autofocus = true;
                    tv_Time_for_Focusing.setVisibility(View.VISIBLE);
                    editText_Autofocus.setVisibility(View.VISIBLE);
                } else {
                    fb.use_autofocus = false;
                    tv_Time_for_Focusing.setVisibility(View.GONE);
                    editText_Autofocus.setVisibility(View.GONE);
                }

            }

            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

// ------------------------------------------------------------------------------------------------
// Присоединить изображение к письму

// Attach Container
        linLayout_Attach = new LinearLayout(this);
        linLayout_Attach.setOrientation(LinearLayout.VERTICAL);
        linLayout_Attach.setPadding(5, 9, 5, 9);
        linLayout_Attach.setBackgroundColor(bc_camera_color);

// Attach TextView
        TextView tv_Attach = new TextView(this);
        tv_Attach.setText(getResources().getString(R.string.attach_image));
        tv_Attach.setWidth((screenWidth - padding) / 100 * 90);
        tv_Attach.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Attach.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Attach.setTextColor(Color.BLACK);
        linLayout_Attach.addView(tv_Attach);

// CheckBox
        checkBox_Attach = new CheckBox(this);
        checkBox_Attach.setChecked(fb.bc_image_attach);
        linLayout_Attach.addView(checkBox_Attach);

// ------------------------------------------------------------------------------------------------
// Удалить изображение после отправки на почту

// Delete Container
        linLayout_Delete = new LinearLayout(this);
        linLayout_Delete.setOrientation(LinearLayout.VERTICAL);
        linLayout_Delete.setPadding(5, 9, 5, 9);
        linLayout_Delete.setBackgroundColor(bc_camera_color);

// Delete TextView
        TextView tv_Delete = new TextView(this);
        tv_Delete.setText(getResources().getString(R.string.delete_image));
        tv_Delete.setWidth((screenWidth - padding) / 100 * 90);
        tv_Delete.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Delete.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Delete.setTextColor(Color.BLACK);
        linLayout_Delete.addView(tv_Delete);

// CheckBox
        checkBox_Delete = new CheckBox(this);
        checkBox_Delete.setChecked(fb.bc_image_delete);
        linLayout_Delete.addView(checkBox_Delete);


// ------------------------------------------------------------------------------------------------

// Фронтальная камера
// Контейнер для фронтальной камеры
        linLayout_fc = new LinearLayout(this);
        linLayout_fc.setOrientation(LinearLayout.VERTICAL);
        linLayout_fc.setPadding(5, 9, 5, 9);
        linLayout_fc.setBackgroundColor(fc_camera_color);

        if ( fb.front_camera) {
// Название
            TextView tv_fc = new TextView(this);
            tv_fc.setTypeface(Typeface.DEFAULT_BOLD);
            tv_fc.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size + 4);
            tv_fc.setTextColor(Color.BLACK);
            tv_fc.setText(getResources().getString(R.string.Front_Camera));
            tv_fc.setTypeface(Typeface.DEFAULT_BOLD);
            linLayout_fc.addView(tv_fc);

// Контейнер для метода
            LinearLayout fc_linLayout_Photo_Size = new LinearLayout(this);
            fc_linLayout_Photo_Size.setOrientation(LinearLayout.VERTICAL);
            fc_linLayout_Photo_Size.setPadding(5, 9, 5, 9);
            fc_linLayout_Photo_Size.setBackgroundColor(fc_camera_color);

            // Размер фото
            fc_tv_Photo_Size_s = new TextView(this);
            fc_tv_Photo_Size_s.setTypeface(Typeface.DEFAULT_BOLD);
            fc_tv_Photo_Size_s.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
            fc_tv_Photo_Size_s.setTextColor(Color.BLACK);
            fc_tv_Photo_Size_s.setText("Software");
            //  linLayout_fc.addView(fc_tv_Photo_Size_s);

// Доступные разрешения
            ArrayList<String> fc_spinnerArray = new ArrayList<String>();

            Camera.Size fc_mSize = null;

            int fc_fe_w = (int) fb.fc_camera_resolutions.get(0).width;
            int fc_fe_h = (int) fb.fc_camera_resolutions.get(0).height;
            float fc_fe_s, fc_fe_z;

            fc_fe_z = (float) fc_fe_w / (float) fc_fe_h;

            for (Camera.Size size : fb.fc_camera_resolutions) {
                fc_fe_w = (int) size.width;
                fc_fe_h = (int) size.height;
                fc_fe_s = (float) fc_fe_w / (float) fc_fe_h;

             //   if (Math.abs(fc_fe_s - fc_fe_z) < 0.01f) {
                    fc_spinnerArray.add(size.width + "x" + size.height);
             //   }

            }

            fc_spinner_Software = new Spinner(this);
            fc_spinnerArrayAdapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, fc_spinnerArray);
            fc_spinner_Software.setAdapter(fc_spinnerArrayAdapter1);
            fc_spinner_Software.setSelection(getIndex(fc_spinner_Software, fb.fc_Image_Size));

            linLayout_fc.addView(fc_spinner_Software);

            // ------------------------------------------------------------------------------------------------
// Удалить изображение после отправки на почту

// Delete Container
            linLayout_fc_Delete = new LinearLayout(this);
            linLayout_fc_Delete.setOrientation(LinearLayout.VERTICAL);
            linLayout_fc_Delete.setPadding(5, 9, 5, 9);
            linLayout_fc_Delete.setBackgroundColor(fc_camera_color);

// Delete TextView
            TextView tv_fc_Delete = new TextView(this);
            tv_fc_Delete.setText(getResources().getString(R.string.delete_image));
            tv_fc_Delete.setWidth((screenWidth - padding) / 100 * 90);
            tv_fc_Delete.setTypeface(Typeface.DEFAULT_BOLD);
            tv_fc_Delete.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
            tv_fc_Delete.setTextColor(Color.BLACK);
            //linLayout_fc.addView(tv_fc_Delete);
            linLayout_fc_Delete.addView(tv_fc_Delete);

// CheckBox
            checkBox_fc_Delete = new CheckBox(this);
            checkBox_fc_Delete.setChecked(fb.fc_image_delete);
            //linLayout_fc.addView(checkBox_fc_Delete);
            linLayout_fc_Delete.addView(checkBox_fc_Delete);
        }

// Использовать фронтальную камеру
            TextView tv_use_fc = new TextView(this);
            tv_use_fc.setText(getResources().getString(R.string.use_fc_camera));
            tv_use_fc.setWidth((screenWidth - padding) / 100 * 90);
            tv_use_fc.setTypeface(Typeface.DEFAULT_BOLD);
            tv_use_fc.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
            tv_use_fc.setTextColor(Color.BLACK);
            linLayout_fc.addView(tv_use_fc);

// CheckBox
            checkBox_fc = new CheckBox(this);
            checkBox_fc.setChecked(fb.make_photo_fc);
            linLayout_fc.addView(checkBox_fc);

            checkBox_fc.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (checkBox_fc.isChecked()) {
                        fb.make_photo_fc = true;
                        linLayout_fc_Attach.setVisibility(View.VISIBLE);
                        linLayout_fc_Delete.setVisibility(View.VISIBLE);

                    } else {
                        fb.make_photo_fc = false;
                        linLayout_fc_Attach.setVisibility(View.GONE);
                        linLayout_fc_Delete.setVisibility(View.GONE);

                    }

                }

                // If no option selected
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }

            });

// ------------------------------------------------------------------------------------------------
// Присоединить изображение к письму

// Attach Container
            linLayout_fc_Attach = new LinearLayout(this);
            linLayout_fc_Attach.setOrientation(LinearLayout.VERTICAL);
            linLayout_fc_Attach.setPadding(5, 9, 5, 9);
            linLayout_fc_Attach.setBackgroundColor(fc_camera_color);

// Attach TextView
            TextView tv_fc_Attach = new TextView(this);
            tv_fc_Attach.setText(getResources().getString(R.string.attach_image));
            tv_fc_Attach.setWidth((screenWidth - padding) / 100 * 90);
            tv_fc_Attach.setTypeface(Typeface.DEFAULT_BOLD);
            tv_fc_Attach.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
            tv_fc_Attach.setTextColor(Color.BLACK);
            //linLayout_fc.addView(tv_fc_Attach);
            linLayout_fc_Attach.addView(tv_fc_Attach);

// CheckBox
            checkBox_fc_Attach = new CheckBox(this);
            checkBox_fc_Attach.setChecked(fb.fc_image_attach);
            //linLayout_fc.addView(checkBox_fc_Attach);
            linLayout_fc_Attach.addView(checkBox_fc_Attach);

// ------------------------------------------------------------------

// Метод обработки фото

// Контейнер для метода
        LinearLayout linLayout_Photo_Processing_Method = new LinearLayout(this);
        linLayout_Photo_Processing_Method.setOrientation(LinearLayout.VERTICAL);
        linLayout_Photo_Processing_Method.setPadding(5, 9, 5, 9);
        linLayout_Photo_Processing_Method.setBackgroundColor(Color.rgb(208, 208, 208));

// Название
        TextView tv_Photo_Processing_Method = new TextView(this);
        tv_Photo_Processing_Method.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Photo_Processing_Method.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Photo_Processing_Method.setTextColor(Color.BLACK);
        tv_Photo_Processing_Method.setText(getResources().getString(R.string.photo_processing_method));
        tv_Photo_Processing_Method.setTypeface(Typeface.DEFAULT_BOLD);
        linLayout_Photo_Processing_Method.addView(tv_Photo_Processing_Method);

// Список
        spinnerArray_ppm = new ArrayList<String>();
        spinnerArray_ppm.add("Hardware");
        spinnerArray_ppm.add("Software");

        spinner_ppm = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter_ppm = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerArray_ppm);
        spinner_ppm.setAdapter(spinnerArrayAdapter_ppm);
        spinner_ppm.setSelection(getIndex(spinner_ppm, fb.Photo_Post_Processing_Method));
        spinner_ppm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {

                if (spinnerArray_ppm.get(i) == "Hardware") {
                    fb.Photo_Post_Processing_Method = "Hardware";
                    tv_Photo_Size_s.setVisibility(View.GONE);
                    spinner_Software.setVisibility(View.GONE);
                    tv_Photo_Size_s_note.setVisibility(View.GONE);
                    tv_Photo_Size_h.setVisibility(View.VISIBLE);
                    spinner_Hardware.setVisibility(View.VISIBLE);
                    tv_Photo_Size_h_note.setVisibility(View.VISIBLE);
                    if ( fb.front_camera ) {
                        fc_spinner_Software.setVisibility(View.GONE);
                    }
                } else {
                    fb.Photo_Post_Processing_Method = "Software";
                    tv_Photo_Size_s.setVisibility(View.VISIBLE);
                    spinner_Software.setVisibility(View.VISIBLE);
                    tv_Photo_Size_s_note.setVisibility(View.VISIBLE);
                    tv_Photo_Size_h.setVisibility(View.GONE);
                    spinner_Hardware.setVisibility(View.GONE);
                    tv_Photo_Size_h_note.setVisibility(View.GONE);
                    if ( fb.front_camera ) {
                        fc_spinner_Software.setVisibility(View.VISIBLE);
                    }
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
        tv_Photo_Processing_Method_note.setTypeface(null, Typeface.NORMAL);
        tv_Photo_Processing_Method_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Photo_Processing_Method_note.setTextColor(Color.BLACK);
        tv_Photo_Processing_Method_note.setText(getResources().getString(R.string.photo_processing_method_dscription));
        linLayout_Photo_Processing_Method.addView(tv_Photo_Processing_Method_note);

// -------------------------------------------------------------------------------


// Buttons

// Container
        LinearLayout linLayout_Buttons = new LinearLayout(this);
        linLayout_Buttons.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Buttons.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        LinearLayout.LayoutParams lpViewbutton1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        LinearLayout.LayoutParams lpViewbutton2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
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

                if (checkBox_Flash.isChecked()) {
                    editor.putBoolean("Use_Flash", true);
                } else {
                    editor.putBoolean("Use_Flash", false);
                }

                if ( fb.front_camera ) {
                    if (checkBox_fc.isChecked()) {
                        editor.putBoolean("Use_Fc", true);
                    } else {
                        editor.putBoolean("Use_Fc", false);
                    }

                    if (checkBox_fc_Attach.isChecked()){
                        editor.putBoolean("Fc_Image_Attach", true);
                        fb.fc_image_attach = true;
                    } else {
                        editor.putBoolean("Fc_Image_Attach", false);
                        fb.fc_image_attach = false;
                    }

                    if (checkBox_fc_Delete.isChecked()){
                        editor.putBoolean("Fc_Image_Delete", true);
                        fb.fc_image_delete = true;
                    } else {
                        editor.putBoolean("Fc_Image_Delete", false);
                        fb.fc_image_delete = false;
                    }

                }

                if (checkBox_bc.isChecked()) {
                    editor.putBoolean("Use_Bc", true);
                } else {
                    editor.putBoolean("Use_Bc", false);
                }

                if (checkBox_Attach.isChecked()){
                    editor.putBoolean("Bc_Image_Attach", true);
                    fb.bc_image_attach = true;
                } else {
                    editor.putBoolean("Bc_Image_Attach", false);
                    fb.bc_image_attach = false;
                }

                if (checkBox_Autofocus.isChecked()) {
                    editor.putBoolean("Use_Autofocus", true);
                } else {
                    editor.putBoolean("Use_Autofocus", false);
                }

                if (checkBox_Delete.isChecked()){
                    editor.putBoolean("Bc_Image_Delete", true);
                    fb.bc_image_delete = true;
                } else {
                    editor.putBoolean("Bc_Image_Delete", false);
                    fb.bc_image_delete = false;
                }

                String input = editText_JPEG_Compression.getText().toString();
                editor.putString("Photo_Post_Processing_Method", spinner_ppm.getSelectedItem().toString());
                editor.putInt("JPEG_Compression", Integer.parseInt(editText_JPEG_Compression.getText().toString()));

                if ( fb.autofocus ) {
                    editor.putInt("Time_For_Focusing", Integer.parseInt(editText_Autofocus.getText().toString()));
                }

                editor.putString("Image_Scale", spinner_Hardware.getSelectedItem().toString());
                editor.putString("Image_Size", spinner_Software.getSelectedItem().toString());
                if ( fb.front_camera ) {
                    editor.putString("fc_Image_Size", fc_spinner_Software.getSelectedItem().toString());
                }

// Save the changes in SharedPreferences
                editor.commit(); // commit changes
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
                fb.Tab_Foto_Activity_activated = true;
                intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        linLayout_Buttons.addView(btn, lpViewbutton1);
        linLayout_Buttons.addView(btn_mp, lpViewbutton2);


//        FullFrame.addView(linLayout_Photo_Processing_Method);
//        FullFrame.addView(linLayout_Photo_Size);


        FullFrame.addView(linLayout_camera);
        FullFrame.addView(linLayout_Flash);
        if ( fb.autofocus ) {
            FullFrame.addView(linLayout_Autofocus);
        }
        FullFrame.addView(linLayout_Attach);
        FullFrame.addView(linLayout_Delete);

        if ( !fb.make_photo_bc ) {
            linLayout_Flash.setVisibility(View.GONE);
            linLayout_Autofocus.setVisibility(View.GONE);
            linLayout_Attach.setVisibility(View.GONE);
            linLayout_Delete.setVisibility(View.GONE);
        }

        if ( fb.front_camera ) {
            FullFrame.addView(linLayout_fc);

    FullFrame.addView(linLayout_fc_Attach);
    FullFrame.addView(linLayout_fc_Delete);
            if ( !fb.make_photo_fc ) {
                linLayout_fc_Attach.setVisibility(View.GONE);
                linLayout_fc_Delete.setVisibility(View.GONE);
            }
        }


            FullFrame.addView(linLayout_JPEG_Compression);

        FullFrame.addView(linLayout_Photo_Processing_Method);
        FullFrame.addView(linLayout_Photo_Size);

        FullFrame.addView(linLayout_Buttons);

        ScrollView m_Scroll = new ScrollView(this);
        m_Scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        m_Scroll.addView(FullFrame, new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));

        setContentView(m_Scroll);

    }

    protected void onResume(SurfaceHolder holder) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.LoadSettings();
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
        fb.LoadSettings();
        editText_JPEG_Compression.setText(Integer.toString(fb.JPEG_Compression));
        spinner_Software.setSelection(getIndex(spinner_Software, fb.Image_Size));
        checkBox_Flash.setChecked(fb.Use_Flash);
        Log.d(LOG_TAG, "Tab3: onPause");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "Tab3: onRestart");
    }

    //private method of your class
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

    public class MyAdapter extends ArrayAdapter<String>
    {
// http://abhiandroid.com/ui/custom-arrayadapter-tutorial-example.html
// http://www.edureka.co/blog/custom-spinner-in-android
// http://karanbalkar.com/2013/07/tutorial-39-create-custom-spinner-in-android/

        public MyAdapter(Context context, int textViewResourceId, ArrayList<String> objects)
        {
            super(context, textViewResourceId, objects);
        }


        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent)
        {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent)
        {

            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.spinner_item, parent, false);
            TextView label=(TextView)row.findViewById(R.id.textView1);
            String str = spinnerArray.get(position);
            String[] parts = str.split("x");
            int width = Integer.parseInt(parts[0]);
            int height = Integer.parseInt(parts[1]);
            float diff = Math.abs((float)width/(float)height - 4.0f/3.0f );
            Log.d("DEUG", "width = " + width + " height = " + height + " diff = " + diff + " " + position);
            if ( diff > 0.01f ) {
                label.setTextColor(Color.rgb(210,90,90));
            } else {
                label.setTextColor(Color.rgb(50,100,150));
            }

            label.setText(str);



//            label.setText("sss");

            return row;
        }

    }



}
