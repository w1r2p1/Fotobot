package com.example.andrey.fotobot;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

public class Tab_Network_Activity extends Activity {

    final String LOG_NETWORK_ACTIVITY = "Logs";

    String[] connect_methods = {"Wi-Fi", "Mobile Data", "Both"};



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_network);

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


    }

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

        Log.d(LOG_NETWORK_ACTIVITY, "spinner" + spinner_value);

     //   if (check_box_wifi.isChecked()) {
     //       Log.d(LOG_NETWORK_ACTIVITY, "checkBox_WiFi turned On");
     //   } else {
     //       Log.d(LOG_NETWORK_ACTIVITY, "checkBox_WiFi turned Off");
     //   }

    }

}
