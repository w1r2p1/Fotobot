package com.droid.app.fotobot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Сетевые настройки для FotoBot
 * <table border=1>
 * <caption>Layout</caption>
 * <tr>
 * <td>
 * FullFrame
 * </td>
 * </tr>
 * </table>
 */
public class Tab_Network_Activity extends Activity {
    final String LOG_NETWORK_ACTIVITY = "Logs";
    Spinner spinner_Channels;
    Spinner spinner_Connection_Method;
    EditText editText_Fotobot_Email;
    EditText editText_Fotobot_Password;
    EditText editText_Fotobot_Recipient;
    EditText editText_SMTP_Host;
    EditText editText_SMTP_Port;
    EditText editText_Check_Web_Page;
    EditText editText_Network_Up_Delay;
    LinearLayout linLayout_Channels;
    LinearLayout linLayout_Connection_Method;
    LinearLayout linLayout_Fotobot_Email;
    LinearLayout linLayout_Fotobot_Password;
    LinearLayout linLayout_Fotobot_Recipient;
    LinearLayout linLayout_SMTP_Host;
    LinearLayout linLayout_SMTP_Port;

    Button btn, btn_mp;
    Spinner spinner_ppm;

    ArrayList<String> spinnerArray_ppm;

    private int screenWidth, screenHeight;
    private int padding = 5;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.LoadSettings();

        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

// Main Container (Vertical LinearLayout)
// Главный контейнер внутри которого вся раскладка
        LinearLayout FullFrame = new LinearLayout(this);
        FullFrame.setOrientation(LinearLayout.VERTICAL);
        FullFrame.setPadding(0, 0, 0, 0);
        FullFrame.setBackgroundColor(Color.rgb(192, 192, 192));
        FullFrame.setMinimumHeight(fb.Working_Area_Height - fb.menuheight);

// ------------------------------------------------------------------------------------------------

// Каналы связи

// Контейнер для каналов связи
        linLayout_Channels = new LinearLayout(this);
        linLayout_Channels.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams lpView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //   RelativeLayout.LayoutParams lpView1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //   RelativeLayout.LayoutParams lpView2m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        //    LinearLayout.LayoutParams lpView_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Channels.setPadding(5, 9, 5, 9);
        linLayout_Channels.setBackgroundColor(Color.rgb(208, 208, 208));

// Название
        TextView tv_Channels = new TextView(this);
        tv_Channels.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Channels.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Channels.setTextColor(Color.BLACK);
        tv_Channels.setText(getResources().getString(R.string.Internet_connection_channel));
        linLayout_Channels.addView(tv_Channels);

// Список
        ArrayList<String> spinnerArray_Channels = new ArrayList<String>();
        spinnerArray_Channels.add("Mobile Data");
        spinnerArray_Channels.add("Wi-Fi");
        spinnerArray_Channels.add("Both");

        spinner_Channels = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerArray_Channels);
        spinner_Channels.setAdapter(spinnerArrayAdapter1);
        spinner_Channels.setSelection(getIndex(spinner_Channels, fb.Network_Channel));
        linLayout_Channels.addView(spinner_Channels);

// Заметка для каналов связи
        TextView tv_Channels_note = new TextView(this);
        tv_Channels_note.setTypeface(null, Typeface.NORMAL);
        tv_Channels_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Channels_note.setTextColor(Color.BLACK);
        tv_Channels_note.setText(getResources().getString(R.string.Internet_connection_channel_description));
        linLayout_Channels.addView(tv_Channels_note);

// ------------------------------------------------------------------------------------------------

// Метод подключения

// Контейнер для метода подключения
        linLayout_Connection_Method = new LinearLayout(this);
        linLayout_Connection_Method.setOrientation(LinearLayout.VERTICAL);
        linLayout_Connection_Method.setPadding(5, 9, 5, 9);
//        LinearLayout.LayoutParams lpView_Flash = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        LinearLayout.LayoutParams lpView_et_Flash = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

//        RelativeLayout.LayoutParams lpView_m1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        RelativeLayout.LayoutParams lpView_m2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        linLayout_Connection_Method.setBackgroundColor(Color.rgb(192, 192, 192));

// Название
        TextView tv_Connection_Method = new TextView(this);
        tv_Connection_Method.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Connection_Method.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Connection_Method.setTextColor(Color.BLACK);
        tv_Connection_Method.setText(getResources().getString(R.string.connection_method));
        linLayout_Connection_Method.addView(tv_Connection_Method);

// Список
        ArrayList<String> spinnerArray_Connection_Method = new ArrayList<String>();
        spinnerArray_Connection_Method.add("Method 1");
        spinnerArray_Connection_Method.add("Method 2");

        spinner_Connection_Method = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerArray_Connection_Method);
        spinner_Connection_Method.setAdapter(spinnerArrayAdapter2);
        spinner_Connection_Method.setSelection(getIndex(spinner_Connection_Method, fb.Network_Connection_Method));
        linLayout_Connection_Method.addView(spinner_Connection_Method);

// Заметка для каналов связи
        TextView tv_Connection_Method_note = new TextView(this);
        tv_Connection_Method_note.setTypeface(null, Typeface.NORMAL);
        tv_Connection_Method_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Connection_Method_note.setTextColor(Color.BLACK);
        tv_Connection_Method_note.setText(getResources().getString(R.string.connection_method_description));
        linLayout_Connection_Method.addView(tv_Connection_Method_note);

// ------------------------------------------------------------------------------------------------

// Fotobot's e-mail

// E-Mail Container
        linLayout_Fotobot_Email = new LinearLayout(this);
        linLayout_Fotobot_Email.setOrientation(LinearLayout.VERTICAL);
        linLayout_Fotobot_Email.setPadding(5, 9, 5, 9);
        linLayout_Fotobot_Email.setBackgroundColor(Color.rgb(208, 208, 208));

// Название
        TextView tv_Fotobot_Email = new TextView(this);
        tv_Fotobot_Email.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Fotobot_Email.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Fotobot_Email.setTextColor(Color.BLACK);
        tv_Fotobot_Email.setText(getResources().getString(R.string.Fotobot_email));
        linLayout_Fotobot_Email.addView(tv_Fotobot_Email);

// Почтовый адрес
        editText_Fotobot_Email = new EditText(this);
        editText_Fotobot_Email.setSingleLine(true);
        editText_Fotobot_Email.setText(fb.EMail_Sender);
        editText_Fotobot_Email.setTextColor(Color.rgb(50, 100, 150));
        linLayout_Fotobot_Email.addView(editText_Fotobot_Email);

// Заметка для почты Фотобота
        TextView tv_Fotobot_Email_note = new TextView(this);
        tv_Fotobot_Email_note.setTypeface(null, Typeface.NORMAL);
        tv_Fotobot_Email_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Fotobot_Email_note.setTextColor(Color.BLACK);
        tv_Fotobot_Email_note.setText(getResources().getString(R.string.Fotobot_email_description));
        linLayout_Fotobot_Email.addView(tv_Fotobot_Email_note);

// ------------------------------------------------------------------------------------------------

// Fotobot's password

// E-Mail Container
        linLayout_Fotobot_Password = new LinearLayout(this);
        linLayout_Fotobot_Password.setOrientation(LinearLayout.VERTICAL);
        //      RelativeLayout.LayoutParams lpView_email_passwd = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //      RelativeLayout.LayoutParams lpView_email_passwd_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Fotobot_Password.setPadding(5, 9, 5, 9);
        linLayout_Fotobot_Password.setBackgroundColor(Color.rgb(192, 192, 192));

// Название
        TextView tv_Fotobot_Password = new TextView(this);
        tv_Fotobot_Password.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Fotobot_Password.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Fotobot_Password.setTextColor(Color.BLACK);
        tv_Fotobot_Password.setText(getResources().getString(R.string.Fotobot_email_password));
        linLayout_Fotobot_Password.addView(tv_Fotobot_Password);

// Пароль
        editText_Fotobot_Password = new EditText(this);
        editText_Fotobot_Password.setSingleLine(true);
        editText_Fotobot_Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_Fotobot_Password.setText(fb.EMail_Sender_Password);
        editText_Fotobot_Password.setTextColor(Color.rgb(50, 100, 150));
        linLayout_Fotobot_Password.addView(editText_Fotobot_Password);

// Заметка для пароля
        TextView tv_Fotobot_Password_note = new TextView(this);
        tv_Fotobot_Password_note.setTypeface(null, Typeface.NORMAL);
        tv_Fotobot_Password_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Fotobot_Password_note.setTextColor(Color.BLACK);
        tv_Fotobot_Password_note.setText("Пароль.");
        linLayout_Fotobot_Password.addView(tv_Fotobot_Password_note);

// ------------------------------------------------------------------------------------------------

// Fotobot's recipient

// Recipient Container
        linLayout_Fotobot_Recipient = new LinearLayout(this);
        linLayout_Fotobot_Recipient.setOrientation(LinearLayout.VERTICAL);
        linLayout_Fotobot_Recipient.setPadding(5, 9, 5, 9);
        linLayout_Fotobot_Recipient.setBackgroundColor(Color.rgb(208, 208, 208));

// Название
        TextView tv_Fotobot_Recipient = new TextView(this);
        tv_Fotobot_Recipient.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Fotobot_Recipient.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Fotobot_Recipient.setTextColor(Color.BLACK);
        tv_Fotobot_Recipient.setText(getResources().getString(R.string.Recipient_email));
        linLayout_Fotobot_Recipient.addView(tv_Fotobot_Recipient);

// Почтовый адрес получателя
        editText_Fotobot_Recipient = new EditText(this);
        editText_Fotobot_Recipient.setSingleLine(true);
        editText_Fotobot_Recipient.setText(fb.EMail_Recepient);
        editText_Fotobot_Recipient.setTextColor(Color.rgb(50, 100, 150));
        linLayout_Fotobot_Recipient.addView(editText_Fotobot_Recipient);

// Заметка для почты Фотобота
        TextView tv_Fotobot_Recipient_note = new TextView(this);
        tv_Fotobot_Recipient_note.setTypeface(null, Typeface.NORMAL);
        tv_Fotobot_Recipient_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Fotobot_Recipient_note.setTextColor(Color.BLACK);
        tv_Fotobot_Recipient_note.setText("Получатель писем с фото");
        linLayout_Fotobot_Recipient.addView(tv_Fotobot_Recipient_note);

// ------------------------------------------------------------------------------------------------

// SMTP Host

// SMTPHost Container
        linLayout_SMTP_Host = new LinearLayout(this);
        linLayout_SMTP_Host.setOrientation(LinearLayout.VERTICAL);
        linLayout_SMTP_Host.setPadding(5, 9, 5, 9);
        linLayout_SMTP_Host.setBackgroundColor(Color.rgb(208, 208, 208));

// Название
        TextView tv_SMTP_Host = new TextView(this);
        tv_SMTP_Host.setTypeface(Typeface.DEFAULT_BOLD);
        tv_SMTP_Host.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_SMTP_Host.setTextColor(Color.BLACK);
        tv_SMTP_Host.setText(getResources().getString(R.string.email_server));
        linLayout_SMTP_Host.addView(tv_SMTP_Host);

// Адрес почтового сервера
        editText_SMTP_Host = new EditText(this);
        editText_SMTP_Host.setSingleLine(true);
        editText_SMTP_Host.setText(fb.SMTP_Host);
        editText_SMTP_Host.setTextColor(Color.rgb(50, 100, 150));
        linLayout_SMTP_Host.addView(editText_SMTP_Host);

// Заметка для SMTP Host
        TextView tv_SMTP_Host_note = new TextView(this);
        tv_SMTP_Host_note.setTypeface(null, Typeface.NORMAL);
        tv_SMTP_Host_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_SMTP_Host_note.setTextColor(Color.BLACK);
        tv_SMTP_Host_note.setText(getResources().getString(R.string.email_server_description));
        linLayout_SMTP_Host.addView(tv_SMTP_Host_note);

// ------------------------------------------------------------------------------------------------

// SMTP Port

// SMTP Port Container
        linLayout_SMTP_Port = new LinearLayout(this);
        linLayout_SMTP_Port.setOrientation(LinearLayout.VERTICAL);
        linLayout_SMTP_Port.setPadding(5, 9, 5, 9);
        linLayout_SMTP_Port.setBackgroundColor(Color.rgb(192, 192, 192));

// Название
        TextView tv_SMTP_Port = new TextView(this);
        tv_SMTP_Port.setTypeface(Typeface.DEFAULT_BOLD);
        tv_SMTP_Port.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_SMTP_Port.setTextColor(Color.BLACK);
        tv_SMTP_Port.setText(getResources().getString(R.string.email_server_port));
        linLayout_SMTP_Port.addView(tv_SMTP_Port);

// Порт почтового сервера
        editText_SMTP_Port = new EditText(this);
        editText_SMTP_Port.setSingleLine(true);
        editText_SMTP_Port.setText(fb.SMTP_Port);
        editText_SMTP_Port.setTextColor(Color.rgb(50, 100, 150));
        linLayout_SMTP_Port.addView(editText_SMTP_Port);

// Заметка для SMTP Port
        TextView tv_SMTP_Port_note = new TextView(this);
        tv_SMTP_Port_note.setTypeface(null, Typeface.NORMAL);
        tv_SMTP_Port_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_SMTP_Port_note.setTextColor(Color.BLACK);
        tv_SMTP_Port_note.setText(getResources().getString(R.string.email_server_port_description));
        linLayout_SMTP_Port.addView(tv_SMTP_Port_note);

// Second Container (Horizontal LinearLayout)
        LinearLayout linLayout2 = new LinearLayout(this);
        linLayout2.setOrientation(LinearLayout.VERTICAL);
        linLayout2.setPadding(5, 9, 5, 9);
//        LinearLayout.LayoutParams lpView2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        LinearLayout.LayoutParams lpViewbutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Password Container
        LinearLayout linLayout_email_password = new LinearLayout(this);
        linLayout_email_password.setOrientation(LinearLayout.VERTICAL);

// E-Mail Container2
        LinearLayout linLayout_email_recepient = new LinearLayout(this);
        linLayout_email_recepient.setOrientation(LinearLayout.VERTICAL);

// ------------------------------------------------------------------------------------------------

// Check Web Page

// Chec Web Page Container
        RelativeLayout linLayout_Check_Web_Page = new RelativeLayout(this);
        RelativeLayout.LayoutParams lpView_Check_Web_Page = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_Check_Web_Page_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Check_Web_Page.setPadding(5, 9, 5, 9);
        linLayout_Check_Web_Page.setBackgroundColor(Color.rgb(192, 192, 192));

// Пояснение контейнер
        LinearLayout linLayout_Check_Web_Page_note = new LinearLayout(this);
        linLayout_Check_Web_Page_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Check_Web_Page_note.setPadding(5, 9, 5, 9);
        linLayout_Check_Web_Page_note.setBackgroundColor(Color.rgb(192, 192, 192));

// Название
        TextView tv_Check_Web_Page = new TextView(this);
        tv_Check_Web_Page.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Check_Web_Page.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Check_Web_Page.setTextColor(Color.BLACK);
        tv_Check_Web_Page.setText(getResources().getString(R.string.Check_Web_Page));
        tv_Check_Web_Page.setMinimumWidth((screenWidth - padding) / 100 * 60);
        tv_Check_Web_Page.setLayoutParams(lpView_Check_Web_Page);

        lpView_Check_Web_Page.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Check_Web_Page.getId());
        tv_Check_Web_Page.setLayoutParams(lpView_Check_Web_Page);
        linLayout_Check_Web_Page.addView(tv_Check_Web_Page);

// Адрес сервера для проверки связи
        editText_Check_Web_Page = new EditText(this);
        editText_Check_Web_Page.setSingleLine(true);
        editText_Check_Web_Page.setText(fb.check_web_page);
        editText_Check_Web_Page.setTextColor(Color.rgb(50, 100, 150));
        lpView_Check_Web_Page_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, editText_Check_Web_Page.getId());
        editText_Check_Web_Page.setLayoutParams(lpView_Check_Web_Page_m);
        linLayout_Check_Web_Page.addView(editText_Check_Web_Page);

// Заметка для Check Web Page
        TextView tv_Check_Web_Page_note = new TextView(this);
        tv_Check_Web_Page_note.setTypeface(null, Typeface.NORMAL);
        tv_Check_Web_Page_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Check_Web_Page_note.setTextColor(Color.BLACK);
        tv_Check_Web_Page_note.setText(getResources().getString(R.string.Check_Web_Page_description));
        tv_Check_Web_Page_note.setLayoutParams(lpView);
        tv_Check_Web_Page_note.setPadding(5, 9, 5, 9);
        linLayout_Check_Web_Page_note.addView(tv_Check_Web_Page_note);
// ------------------------------------------------------------------------------------------------

// Network Up Delay

// Network Up Delay Container
        LinearLayout linLayout_Network_Up_Delay = new LinearLayout(this);
        linLayout_Network_Up_Delay.setPadding(5, 9, 5, 9);
        linLayout_Network_Up_Delay.setBackgroundColor(Color.rgb(192, 192, 192));

// Название
        TextView tv_Network_Up_Delay = new TextView(this);
        tv_Network_Up_Delay.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Network_Up_Delay.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Network_Up_Delay.setTextColor(Color.BLACK);
        tv_Network_Up_Delay.setText(getResources().getString(R.string.network_up_delay));
        linLayout_Network_Up_Delay.addView(tv_Network_Up_Delay);

// Network Up Delay
        editText_Network_Up_Delay = new EditText(this);
        //  editText_Network_Up_Delay.setLayoutParams(lpView_et);
        editText_Network_Up_Delay.setSingleLine(true);
        editText_Network_Up_Delay.setText(Integer.toString(fb.network_up_delay));
        editText_Network_Up_Delay.setTextColor(Color.rgb(50, 100, 150));
        linLayout_Network_Up_Delay.addView(editText_Network_Up_Delay);

// Заметка для Network Up Delay
        TextView tv_Network_Up_Delay_note = new TextView(this);
        tv_Network_Up_Delay_note.setTypeface(null, Typeface.NORMAL);
        tv_Network_Up_Delay_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Network_Up_Delay_note.setTextColor(Color.BLACK);
        tv_Network_Up_Delay_note.setText(getResources().getString(R.string.network_up_delay_description));

// ------------------------------------------------------------------------------------------------

// Network Status

// Контейнер для метода
        LinearLayout linLayout_Net_Stat = new LinearLayout(this);
        linLayout_Net_Stat.setOrientation(LinearLayout.VERTICAL);
        linLayout_Net_Stat.setPadding(5, 9, 5, 9);
        linLayout_Net_Stat.setBackgroundColor(Color.rgb(192, 192, 192));

// Название
        TextView tv_Net_Stat = new TextView(this);
        tv_Net_Stat.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Net_Stat.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Net_Stat.setTextColor(Color.BLACK);
        tv_Net_Stat.setText(getResources().getString(R.string.network));
        tv_Net_Stat.setTypeface(Typeface.DEFAULT_BOLD);
        linLayout_Net_Stat.addView(tv_Net_Stat);

// Список
        spinnerArray_ppm = new ArrayList<String>();
        spinnerArray_ppm.add("on");
        spinnerArray_ppm.add("off");

        spinner_ppm = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter_ppm = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerArray_ppm);
        spinner_ppm.setAdapter(spinnerArrayAdapter_ppm);
        if (fb.network) {
            spinner_ppm.setSelection(getIndex(spinner_ppm, "on"));
        } else {
            spinner_ppm.setSelection(getIndex(spinner_ppm, "off"));
        }

        spinner_ppm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {

                if (spinnerArray_ppm.get(i) == "on") {
                    linLayout_Channels.setVisibility(View.VISIBLE);
                    linLayout_Connection_Method.setVisibility(View.VISIBLE);
                    linLayout_Fotobot_Email.setVisibility(View.VISIBLE);
                    linLayout_Fotobot_Password.setVisibility(View.VISIBLE);
                    linLayout_SMTP_Host.setVisibility(View.VISIBLE);
                    linLayout_SMTP_Port.setVisibility(View.VISIBLE);
                    linLayout_Fotobot_Recipient.setVisibility(View.VISIBLE);
                } else {
                    linLayout_Channels.setVisibility(View.GONE);
                    linLayout_Connection_Method.setVisibility(View.GONE);
                    linLayout_Fotobot_Email.setVisibility(View.GONE);
                    linLayout_Fotobot_Password.setVisibility(View.GONE);
                    linLayout_SMTP_Host.setVisibility(View.GONE);
                    linLayout_SMTP_Port.setVisibility(View.GONE);
                    linLayout_Fotobot_Recipient.setVisibility(View.GONE);
                }

            }

            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        linLayout_Net_Stat.addView(spinner_ppm);

// Заметка для метода
        TextView tv_Net_Stat_note = new TextView(this);
        tv_Net_Stat_note.setTypeface(null, Typeface.NORMAL);
        tv_Net_Stat_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Net_Stat_note.setTextColor(Color.BLACK);
        tv_Net_Stat_note.setText(getResources().getString(R.string.network_description));
        linLayout_Net_Stat.addView(tv_Net_Stat_note);

// ------------------------------------------------------------------------------------------------

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

                editor.putString("Network_Channel", spinner_Channels.getSelectedItem().toString());
                editor.putString("Network_Connection_Method", spinner_Connection_Method.getSelectedItem().toString());
                editor.putString("EMail_Sender", editText_Fotobot_Email.getText().toString());
                editor.putString("EMail_Sender_Password", editText_Fotobot_Password.getText().toString());
                editor.putString("SMTP_Host", editText_SMTP_Host.getText().toString());
                editor.putString("SMTP_Port", editText_SMTP_Port.getText().toString());
                editor.putString("EMail_Recepient", editText_Fotobot_Recipient.getText().toString());
                editor.putString("Check_Web_Page", editText_Check_Web_Page.getText().toString());
                editor.putInt("Network_Up_Delay", Integer.parseInt(editText_Network_Up_Delay.getText().toString()));

                if (spinner_ppm.getSelectedItem().toString().contains("on")) {
                    editor.putBoolean("Network", true);
                } else {
                    editor.putBoolean("Network", false);
                    if (Build.VERSION.SDK_INT <= 21) {
                        fb.CloseInternetConnection();
                    }

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
                intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        linLayout_Buttons.addView(btn, lpViewbutton1);
        linLayout_Buttons.addView(btn_mp, lpViewbutton2);

        FullFrame.addView(linLayout_Net_Stat);

        if (Build.VERSION.SDK_INT <= 21) {
            FullFrame.addView(linLayout_Channels);
            FullFrame.addView(linLayout_Connection_Method);
        }

        FullFrame.addView(linLayout_Fotobot_Email);
        FullFrame.addView(linLayout_Fotobot_Password);
        FullFrame.addView(linLayout_SMTP_Host);
        FullFrame.addView(linLayout_SMTP_Port);
        FullFrame.addView(linLayout_Fotobot_Recipient);
        FullFrame.addView(linLayout_Buttons);

        ScrollView m_Scroll = new ScrollView(this);
        m_Scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        m_Scroll.addView(FullFrame, new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));

        setContentView(m_Scroll);

    }

    public void Apply(View v) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_connect_method);
        String spinner_value = spinner.getSelectedItem().toString();

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

    }

    protected void onPause() {
        super.onPause();
        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.LoadSettings();
        spinner_Channels.setSelection(getIndex(spinner_Channels, fb.Network_Channel));
        spinner_Connection_Method.setSelection(getIndex(spinner_Connection_Method, fb.Network_Connection_Method));
        editText_Fotobot_Email.setText(fb.EMail_Sender);
        editText_Fotobot_Password.setText(fb.EMail_Sender_Password);
        editText_Fotobot_Recipient.setText(fb.EMail_Recepient);
        editText_SMTP_Port.setText(fb.SMTP_Port);
        editText_SMTP_Host.setText(fb.SMTP_Host);
    }

    protected void onRestart() {
        super.onRestart();

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

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
}
