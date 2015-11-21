package com.example.andrey.fotobot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Сетевые настройки для FotoBot
 * <table border=1>
 *     <caption>Layout</caption>
 *     <tr>
 *         <td>
 *             FullFrame
 *         </td>
 *     </tr>
 * </table>
 *
 */
public class Tab_Network_Activity extends Activity {
    Spinner spinner1;
    Spinner spinner2;
    EditText editText_email_sender;
    EditText editText_email_password;
    EditText editText_email_recepient;
    private CheckBox check_box_flash;
    private EditText edit_text_jpeg_compression;
    private int screenWidth, screenHeight;
    private int padding = 15;
    final String LOG_NETWORK_ACTIVITY = "Logs";

    String[] connect_methods = {"Wi-Fi", "Mobile Data", "Both"};



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.LoadData();
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

// Main Container (Vertical LinearLayout)
        LinearLayout FullFrame = new LinearLayout(this);
        FullFrame.setOrientation(LinearLayout.VERTICAL);
        FullFrame.setPadding(5, padding, 0, 0);
//        setContentView(FullFrame);

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

// E-Mail Container
        LinearLayout linLayout_email = new LinearLayout(this);
        linLayout_email.setOrientation(LinearLayout.HORIZONTAL);
//        linLayout_email.setBackgroundColor(Color.parseColor("#00ff00"));
        LinearLayout.LayoutParams lpView_email = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Password Container
        LinearLayout linLayout_email_password = new LinearLayout(this);
        linLayout_email_password.setOrientation(LinearLayout.HORIZONTAL);
//        linLayout_email_password.setBackgroundColor(Color.parseColor("#ff0000"));
        LinearLayout.LayoutParams lpView_email_password = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// E-Mail Container2
        LinearLayout linLayout_email_recepient = new LinearLayout(this);
        linLayout_email_recepient.setOrientation(LinearLayout.HORIZONTAL);
//        linLayout_email.setBackgroundColor(Color.parseColor("#00ff00"));
        LinearLayout.LayoutParams lpView_email_recepient = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


// TextView1
        TextView tv = new TextView(this);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(14);
        tv.setTextColor(Color.BLACK);
        tv.setText("Каналы связи");
        tv.setWidth((screenWidth - padding) / 100 * 50);
        tv.setLayoutParams(lpView);
        linLayout1.addView(tv);

//Spinner1
        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Mobile Data");
        spinnerArray.add("Wi-Fi");
        spinnerArray.add("Both");

        spinner1 = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        spinner1.setAdapter(spinnerArrayAdapter1);
       // spinner1.setSelection(spinnerArrayAdapter1.getPosition("Both"));
        spinner1.setSelection(getIndex(spinner1, fb.Network_Channel));
        spinner1.setMinimumWidth((screenWidth - padding) / 100 * 50);
        linLayout1.addView(spinner1);

// TextView2
        TextView tv_Flash = new TextView(this);
        tv_Flash.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Flash.setTextSize(14);
        tv_Flash.setTextColor(Color.BLACK);
        tv_Flash.setText("Метод подключения");
        tv_Flash.setWidth((screenWidth - padding) / 100 * 50);
        tv_Flash.setLayoutParams(lpView_Flash);
        linLayout_Flash.addView(tv_Flash);

//Spinner2
        ArrayList<String> spinnerArray2 = new ArrayList<String>();
        spinnerArray2.add("Method1");
        spinnerArray2.add("Method2");

        spinner2 = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray2);
        spinner2.setAdapter(spinnerArrayAdapter2);
      //  spinner2.setSelection(spinnerArrayAdapter2.getPosition("На каждом шаге"));
        spinner2.setSelection(getIndex(spinner2, fb.Network_Connection_Method));
        spinner2.setMinimumWidth((screenWidth - padding) / 100 * 50);
        linLayout_Flash.addView(spinner2);

// TextView3
        TextView tv_email_sender = new TextView(this);
        tv_email_sender.setTypeface(Typeface.DEFAULT_BOLD);
        tv_email_sender.setTextSize(14);
        tv_email_sender.setTextColor(Color.BLACK);
        tv_email_sender.setText("FotoBot's e-mail");
        tv_email_sender.setMinimumWidth((screenWidth - padding) / 100 * 50);
        tv_email_sender.setLayoutParams(lpView_email);
        linLayout_email.addView(tv_email_sender);

// EditText
        editText_email_sender = new EditText(this);
        editText_email_sender.setLayoutParams(lpView_et);
        editText_email_sender.setSingleLine(true);
        editText_email_sender.setText(fb.EMail_Sender);
        ViewGroup.LayoutParams lp = editText_email_sender.getLayoutParams();
        editText_email_sender.setWidth((screenWidth - padding) - ((screenWidth - padding) / 100 * 50));
       // lp.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 50);
        editText_email_sender.setLayoutParams(lpView_email);
        editText_email_sender.setGravity(Gravity.RIGHT);
        linLayout_email.addView(editText_email_sender);

// TextView4
        TextView tv_email_password = new TextView(this);
        tv_email_password.setTypeface(Typeface.DEFAULT_BOLD);
        tv_email_password.setTextSize(14);
        tv_email_password.setTextColor(Color.BLACK);
        tv_email_password.setText("e-mail passswd");
        tv_email_password.setMinimumWidth((screenWidth - padding) / 100 * 50);
        tv_email_password.setLayoutParams(lpView_email);
        linLayout_email_password.addView(tv_email_password);

// Password
        editText_email_password = new EditText(this);
        editText_email_password.setLayoutParams(lpView_et);
        editText_email_password.setSingleLine(true);
        editText_email_password.setText(fb.EMail_Sender_Password);
        ViewGroup.LayoutParams lp_password = editText_email_password.getLayoutParams();
      //  lp_password.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 50);
        editText_email_password.setWidth((screenWidth - padding) - ((screenWidth - padding) / 100 * 50));
        editText_email_password.setLayoutParams(lpView_email_password);
        editText_email_password.setGravity(Gravity.RIGHT);
        linLayout_email_password.addView(editText_email_password);

        // TextView4
        TextView tv_email_recepient = new TextView(this);
        tv_email_recepient.setTypeface(Typeface.DEFAULT_BOLD);
        tv_email_recepient.setTextSize(14);
        tv_email_recepient.setTextColor(Color.BLACK);
        tv_email_recepient.setText("e-mail получателя");
        tv_email_recepient.setMinimumWidth((screenWidth - padding) / 100 * 50);
        tv_email_recepient.setLayoutParams(lpView_email);
        linLayout_email_recepient.addView(tv_email_recepient);

// EditText
        editText_email_recepient = new EditText(this);
        editText_email_recepient.setLayoutParams(lpView_et);
        editText_email_recepient.setSingleLine(true);
        editText_email_recepient.setText(fb.EMail_Recepient);
        ViewGroup.LayoutParams lp_recepient = editText_email_recepient.getLayoutParams();
        editText_email_recepient.setWidth((screenWidth - padding) - ((screenWidth - padding) / 100 * 50));
    //    lp_recepient.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 50);
        editText_email_recepient.setLayoutParams(lpView_email_recepient);
        editText_email_recepient.setGravity(Gravity.RIGHT);
        linLayout_email_recepient.addView(editText_email_recepient);


// Apply Button
        Button btn = new Button(this);
        btn.setText("Применить");
        btn.setGravity(Gravity.BOTTOM);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("Network_Channel", spinner1.getSelectedItem().toString());
                editor.putString("Network_Connection_Method", spinner2.getSelectedItem().toString());

                editor.putString("EMail_Sender", editText_email_sender.getText().toString());
                editor.putString("EMail_Sender_Password", editText_email_password.getText().toString());
                editor.putString("EMail_Recepient", editText_email_recepient.getText().toString());

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
        FullFrame.addView(linLayout_email);
        FullFrame.addView(linLayout_email_password);
        FullFrame.addView(linLayout_email_recepient);
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




















/*        setContentView(R.layout.tab_network);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_connect_method);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                connect_methods);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Title");

        // выделяем элемент
        spinner.setSelection(2);



        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
*/




    public void Apply (View v) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_connect_method);
        String spinner_value = spinner.getSelectedItem().toString();

       // check_box_wifi = (CheckBox) findViewById(R.id.checkBox_WiFi);
      //  check_box_mobile_data = (CheckBox) findViewById(R.id.checkBox_MobileData);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        switch (spinner_value) {
            case "Wi-Fi":
                editor.putBoolean("Use_WiFi", true);
                editor.putBoolean("Use_Mobile_Data", false);
                break;
            case "Mobile Data":
                editor.putBoolean("Use_WiFi", false);
                editor.putBoolean("Use_Mobile_Data", true);
                break;
            case "Both":
                editor.putBoolean("Use_WiFi", true);
                editor.putBoolean("Use_Mobile_Data", true);
                break;
        }

        // Save the changes in SharedPreferences
        editor.commit(); // commit changes

        Log.d(LOG_NETWORK_ACTIVITY, "spinner" + spinner_value);

     //   if (check_box_wifi.isChecked()) {
     //       Log.d(LOG_NETWORK_ACTIVITY, "checkBox_WiFi turned On");
     //   } else {
     //       Log.d(LOG_NETWORK_ACTIVITY, "checkBox_WiFi turned Off");
     //   }

    }

    protected void onPause() {
        super.onPause();
        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.LoadData();
        spinner1.setSelection(getIndex(spinner1, fb.Network_Channel));
        spinner2.setSelection(getIndex(spinner2, fb.Network_Connection_Method));
        editText_email_sender.setText(fb.EMail_Sender);
        editText_email_password.setText(fb.EMail_Sender_Password);
        editText_email_recepient.setText(fb.EMail_Recepient);

    }

    protected void onRestart() {
        super.onRestart();

    }
 /*   protected void onResume(SurfaceHolder holder) {
        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.LoadData();
        spinner1.setSelection(getIndex(spinner1, fb.Network_Channel));
        spinner2.setSelection(getIndex(spinner1, fb.Network_Connection_Method));
        editText_email_sender.setText(fb.EMail_Sender);
        editText_email_password.setText(fb.EMail_Sender_Password);
        editText_email_recepient.setText(fb.EMail_Recepient);
    }
*/

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

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
