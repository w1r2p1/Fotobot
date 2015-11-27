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
    EditText editText_JPEG_Compression;
    Spinner spinner_Hardware, spinner_ppm, spinner1;
    ArrayAdapter<String> spinnerArrayAdapter1, spinnerArrayAdapter_Hardware;
    ArrayList<String> spinnerArray_ppm;
    final String LOG_TAG = "Logs";
    TextView tv_photo_size, tv_photo_size_h;

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

// First Container (Horizontal LinearLayout)
        LinearLayout linLayout1 = new LinearLayout(this);
        linLayout1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Foto postprocessing method (Horizontal LinearLayout)
        LinearLayout linLayout_ppm = new LinearLayout(this);
        linLayout_ppm.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView_ppm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et_ppm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        // First Container (Horizontal LinearLayout)
        LinearLayout linLayout_photo_size = new LinearLayout(this);
        linLayout_photo_size.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView_photo_size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_photo_size_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

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
        tv.setText("Степень сжатия JPEG");
        tv.setWidth((screenWidth - padding) / 100 * 80);
        tv.setLayoutParams(lpView);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv.setTextColor(Color.BLACK);
        linLayout1.addView(tv);

// Photo PostProcessing Method
        TextView tv_ppm = new TextView(this);
        tv_ppm.setText("Метод обработки фото");
        tv_ppm.setWidth((screenWidth - padding) / 100 * 80);
        tv_ppm.setLayoutParams(lpView_ppm);
        tv_ppm.setTypeface(Typeface.DEFAULT_BOLD);
        tv_ppm.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_ppm.setTextColor(Color.BLACK);
//        tv_ppm.setVisibility(View.GONE);
        linLayout_ppm.addView(tv_ppm);

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

// EditText
        editText_JPEG_Compression = new EditText(this);
        editText_JPEG_Compression.setLayoutParams(lpView_et);
        String jpg = Integer.toString(fb.JPEG_Compression);
        editText_JPEG_Compression.setText(jpg);
        ViewGroup.LayoutParams lp = editText_JPEG_Compression.getLayoutParams();
        lp.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 80);
        editText_JPEG_Compression.setLayoutParams(lp);
        editText_JPEG_Compression.setGravity(Gravity.RIGHT);
        linLayout1.addView(editText_JPEG_Compression);

//Photo PostProcessing Method
        spinnerArray_ppm = new ArrayList<String>();
        spinnerArray_ppm.add("Hardware");
        spinnerArray_ppm.add("Software");

        spinner_ppm = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter_ppm = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray_ppm);
        spinner_ppm.setAdapter(spinnerArrayAdapter_ppm);
        // spinner1.setSelection(spinnerArrayAdapter1.getPosition("Both"));
        spinner_ppm.setSelection(getIndex(spinner_ppm, fb.Photo_Post_Processing_Method));
        spinner_ppm.setMinimumWidth((screenWidth - padding) / 100 * 50);
        //   spinner1.setBackgroundResource(android.R.drawable.spinner_bg);
        // spinner1.setBackgroundColor(Color.WHITE);
//        spinner_ppm.setVisibility(View.GONE);

        // Set the ClickListener for Spinner
        spinner_ppm.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {

                if (spinnerArray_ppm.get(i) == "Hardware") {
                    tv_photo_size.setVisibility(View.GONE);
                    spinner1.setVisibility(View.GONE);
                    tv_photo_size_h.setVisibility(View.VISIBLE);
                    spinner_Hardware.setVisibility(View.VISIBLE);

                } else {
                    tv_photo_size.setVisibility(View.VISIBLE);
                    spinner1.setVisibility(View.VISIBLE);
                    tv_photo_size_h.setVisibility(View.GONE);
                    spinner_Hardware.setVisibility(View.GONE);
                }

//                Toast.makeText(Tab_Foto_Activity.this, "You Selected : "

//                        + spinnerArray_ppm.get(i) + " Level ", Toast.LENGTH_SHORT).show();

            }
            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });


        linLayout_ppm.addView(spinner_ppm);




        // TextView1
        tv_photo_size_h = new TextView(this);
        tv_photo_size_h.setTypeface(Typeface.DEFAULT_BOLD);
        tv_photo_size_h.setTextSize(14);
        tv_photo_size_h.setTextColor(Color.BLACK);
        tv_photo_size_h.setText("Масштаб фото");
        tv_photo_size_h.setWidth((screenWidth - padding) / 100 * 50);
        tv_photo_size_h.setLayoutParams(lpView_photo_size);
        linLayout_photo_size.addView(tv_photo_size_h);

//Spinner1
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
        linLayout_photo_size.addView(spinner_Hardware);
























        // TextView1
        tv_photo_size = new TextView(this);
        tv_photo_size.setTypeface(Typeface.DEFAULT_BOLD);
        tv_photo_size.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_photo_size.setTextColor(Color.BLACK);
        tv_photo_size.setText("Размер фото");
        tv_photo_size.setWidth((screenWidth - padding) / 100 * 50);
        tv_photo_size.setLayoutParams(lpView_photo_size);
        linLayout_photo_size.addView(tv_photo_size);

//Spinner1
        ArrayList<String> spinnerArray = new ArrayList<String>();

        Camera.Size mSize = null;
        for (Camera.Size size : fb.camera_resolutions) {
            spinnerArray.add(size.width+"x"+size.height);
          //  Log.i(LOG_TAG, "Available resolution: " +size.width+" "+size.height);
            //   if (wantToUseThisResolution(size)) {
          //  mSize = size;
            //       break;
            //  }
        }



 //       spinnerArray.add("1/16");
 //       spinnerArray.add("1/8");
 //       spinnerArray.add("1/4");
 //       spinnerArray.add("1/2");
 //       spinnerArray.add("1");

        spinner1 = new Spinner(this);
        spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        spinner1.setAdapter(spinnerArrayAdapter1);

      //  spinner1.setSelection(getIndex(spinner1, fb.Image_Scale));
        spinner1.setSelection(getIndex(spinner1, fb.Image_Size));
        spinner1.setMinimumWidth((screenWidth - padding) / 100 * 50);
        linLayout_photo_size.addView(spinner1);











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
        FullFrame.addView(linLayout_ppm);
        FullFrame.addView(linLayout_photo_size);
        FullFrame.addView(linLayout_Flash);
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


/*    final String LOG_NETWORK_ACTIVITY = "Logs";
    private EditText edit_text_photo_frequency;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_scheduler);
    }







    public void Apply(View v) {

        edit_text_photo_frequency = (EditText) findViewById(R.id.editText_photo_frequency);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String input = edit_text_photo_frequency.getText().toString();

        editor.putInt("Photo_Frequency", Integer.parseInt(edit_text_photo_frequency.getText().toString()));

// Save the changes in SharedPreferences
        editor.commit(); // commit changes

    }
*/

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
