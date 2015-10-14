package com.example.andrey.fotobot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Tab_Network_Activity extends Activity {
    Spinner spinner1;
    Spinner spinner2;
    private CheckBox check_box_flash;
    private EditText edit_text_jpeg_compression;
    private int screenWidth, screenHeight;
    private int padding = 15;
    final String LOG_NETWORK_ACTIVITY = "Logs";

    String[] connect_methods = {"Wi-Fi", "Mobile Data", "Both"};



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

// Main Container (Vertical LinearLayout)
        LinearLayout FullFrame = new LinearLayout(this);
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

// TextView1
        TextView tv = new TextView(this);
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
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner1.setAdapter(spinnerArrayAdapter1);
        spinner1.setSelection(spinnerArrayAdapter1.getPosition("Both"));
        spinner1.setMinimumWidth((screenWidth - padding) / 100 * 50);
        linLayout1.addView(spinner1);

// TextView2
        TextView tv_Flash = new TextView(this);
        tv_Flash.setText("Метод подключения");
        tv_Flash.setWidth((screenWidth - padding) / 100 * 50);
        tv_Flash.setLayoutParams(lpView_Flash);
        linLayout_Flash.addView(tv_Flash);

//Spinner2
        ArrayList<String> spinnerArray2 = new ArrayList<String>();
        spinnerArray2.add("В самом начале");
        spinnerArray2.add("На каждом шаге");

        spinner2 = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray2);
        spinner2.setAdapter(spinnerArrayAdapter2);
        spinner2.setSelection(spinnerArrayAdapter2.getPosition("На каждом шаге"));
        spinner2.setMinimumWidth((screenWidth - padding) / 100 * 50);
        linLayout_Flash.addView(spinner2);

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

}
