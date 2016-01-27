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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    Button btn, btn_mp;
    String[] connect_methods = {"Wi-Fi", "Mobile Data", "Both"};
    private CheckBox check_box_flash;
    private EditText edit_text_jpeg_compression;
    private int screenWidth, screenHeight;
    private int padding = 5;

    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_DeviceDefault_Light);
        super.onCreate(savedInstanceState);

        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.LoadData();

       // Logger fblogger = Logger.getLogger(FotoBot.class.getName());
        fb.logger.fine("Tab_Network_Activity");


        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

// Main Container (Vertical LinearLayout)
// Главный контейнер внутри которого вся раскладка
        LinearLayout FullFrame = new LinearLayout(this);
        FullFrame.setOrientation(LinearLayout.VERTICAL);
        FullFrame.setPadding(5, 5, 5, 5);
        FullFrame.setBackgroundColor(Color.rgb(192, 192, 192));
        FullFrame.setMinimumHeight(fb.Working_Area_Height - fb.menuheight);
        // FullFrame.setBackgroundColor(Color.WHITE);

// ------------------------------------------------------------------------------------------------

// Каналы связи

// Контейнер для каналов связи
        RelativeLayout linLayout_Channels = new RelativeLayout(this);
        //    linLayout_Channels.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams lpView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView2m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams lpView_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Channels.setBackgroundColor(Color.rgb(192, 192, 192));

// Пояснение контейнер
        LinearLayout linLayout_Channels_note = new LinearLayout(this);
        linLayout_Channels_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Channels_note.setBackgroundColor(Color.rgb(192, 192, 192));

// Контейнер для разделителя
        LinearLayout linLayout_Channels_divider = new LinearLayout(this);
        linLayout_Channels_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Channels_divider.setPadding(5, 9, 5, 9);

// Название
        TextView tv_Channels = new TextView(this);
        tv_Channels.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Channels.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Channels.setTextColor(Color.BLACK);
        tv_Channels.setText(getResources().getString(R.string.Internet_connection_channel));
       // tv_Channels.setWidth((screenWidth - padding) / 100 * 80);
    //    tv_Channels.setLayoutParams(lpView);

        lpView2m.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Channels.getId());
        lpView2m.width = (screenWidth - padding) / 100 * 60;
        tv_Channels.setLayoutParams(lpView2m);
        linLayout_Channels.addView(tv_Channels);

// Список
        ArrayList<String> spinnerArray_Channels = new ArrayList<String>();
        spinnerArray_Channels.add("Mobile Data");
        spinnerArray_Channels.add("Wi-Fi");
        spinnerArray_Channels.add("Both");

        spinner_Channels = new Spinner(this);
        //ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray_Channels);
        // ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout., spinnerArray_Channels);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerArray_Channels);
        spinner_Channels.setAdapter(spinnerArrayAdapter1);
        spinner_Channels.setSelection(getIndex(spinner_Channels, fb.Network_Channel));
       // spinner_Channels.setMinimumWidth((screenWidth - padding) / 100 * 1);

        lpView1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, spinner_Channels.getId());
        lpView1.width = (screenWidth - padding) / 100 * 40;
        spinner_Channels.setLayoutParams(lpView1);
        linLayout_Channels.addView(spinner_Channels);

// Заметка для каналов связи
        TextView tv_Channels_note = new TextView(this);
        tv_Channels_note.setTypeface(null, Typeface.NORMAL);
        tv_Channels_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Channels_note.setTextColor(Color.BLACK);
        tv_Channels_note.setText(getResources().getString(R.string.Internet_connection_channel_description));
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_Channels_note.setLayoutParams(lpView);
        tv_Channels_note.setPadding(5, 9, 5, 9);
        linLayout_Channels_note.addView(tv_Channels_note);

// Разделитель
        View line = new View(this);
        line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line.setBackgroundColor(Color.rgb(210, 210, 210));
        line.getLayoutParams().height = 3;
        linLayout_Channels_divider.addView(line);

// ------------------------------------------------------------------------------------------------

// Метод подключения

// Контейнер для метода подключения
        RelativeLayout linLayout_Connection_Method = new RelativeLayout(this);
        //  linLayout_Connection_Method.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView_Flash = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et_Flash = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams lpView_m1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_m2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        linLayout_Connection_Method.setBackgroundColor(Color.rgb(192, 192, 192));

// Пояснение контейнер
        LinearLayout linLayout_Connection_Method_note = new LinearLayout(this);
        linLayout_Connection_Method_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Connection_Method_note.setBackgroundColor(Color.rgb(192, 192, 192));

// Контейнер для разделителя
        LinearLayout linLayout_Connection_Method_divider = new LinearLayout(this);
        linLayout_Connection_Method_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Connection_Method_divider.setPadding(5, 9, 5, 9);

// Название
        TextView tv_Connection_Method = new TextView(this);
        tv_Connection_Method.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Connection_Method.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Connection_Method.setTextColor(Color.BLACK);
        tv_Connection_Method.setText(getResources().getString(R.string.connection_method));
      //  tv_Connection_Method.setWidth((screenWidth - padding) / 100 * 70);
      //  tv_Connection_Method.setLayoutParams(lpView_Flash);

        lpView2m.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Connection_Method.getId());
        lpView2m.width = (screenWidth - padding) / 100 * 60;
        tv_Connection_Method.setLayoutParams(lpView2m);
        linLayout_Connection_Method.addView(tv_Connection_Method);

// Список
        ArrayList<String> spinnerArray_Connection_Method = new ArrayList<String>();
        spinnerArray_Connection_Method.add("Method 1");
        spinnerArray_Connection_Method.add("Method 2");

        spinner_Connection_Method = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerArray_Connection_Method);
        spinner_Connection_Method.setAdapter(spinnerArrayAdapter2);
        spinner_Connection_Method.setSelection(getIndex(spinner_Connection_Method, fb.Network_Connection_Method));
     //   spinner_Connection_Method.setMinimumWidth((screenWidth - padding) / 100 * 30);

        lpView1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, spinner_Connection_Method.getId());
        lpView1.width = (screenWidth - padding) / 100 * 40;
        spinner_Connection_Method.setLayoutParams(lpView1);
        linLayout_Connection_Method.addView(spinner_Connection_Method);

// Заметка для каналов связи
        TextView tv_Connection_Method_note = new TextView(this);
        tv_Connection_Method_note.setTypeface(null, Typeface.NORMAL);
        tv_Connection_Method_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Connection_Method_note.setTextColor(Color.BLACK);
        tv_Connection_Method_note.setText(getResources().getString(R.string.connection_method_description));
        tv_Connection_Method_note.setLayoutParams(lpView);
        tv_Connection_Method_note.setPadding(5, 9, 5, 9);
        linLayout_Connection_Method_note.addView(tv_Connection_Method_note);

// Разделитель
        View line_Connection_Method = new View(this);
        line_Connection_Method.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line_Connection_Method.setBackgroundColor(Color.rgb(210, 210, 210));
        line_Connection_Method.getLayoutParams().height = 3;
        linLayout_Connection_Method_divider.addView(line_Connection_Method);

// ------------------------------------------------------------------------------------------------

// Fotobot's e-mail

// E-Mail Container
        RelativeLayout linLayout_Fotobot_Email = new RelativeLayout(this);
        //   linLayout_Fotobot_Email.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams lpView_email = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_email_m1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Fotobot_Email.setPadding(5, 9, 5, 9);
        linLayout_Fotobot_Email.setBackgroundColor(Color.rgb(192, 192, 192));

// Пояснение контейнер
        LinearLayout linLayout_Fotobot_Email_note = new LinearLayout(this);
        linLayout_Fotobot_Email_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Fotobot_Email_note.setPadding(5, 9, 5, 9);
        linLayout_Fotobot_Email_note.setBackgroundColor(Color.rgb(192, 192, 192));

// Контейнер для разделителя
        LinearLayout linLayout_Fotobot_Email_divider = new LinearLayout(this);
        linLayout_Fotobot_Email_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Fotobot_Email_divider.setPadding(5, 9, 5, 9);

// Название
        TextView tv_Fotobot_Email = new TextView(this);
        tv_Fotobot_Email.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Fotobot_Email.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Fotobot_Email.setTextColor(Color.BLACK);
        tv_Fotobot_Email.setText(getResources().getString(R.string.Fotobot_email));
        tv_Fotobot_Email.setMinimumWidth((screenWidth - padding) / 100 * 60);
        tv_Fotobot_Email.setLayoutParams(lpView_email);

        lpView_email.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Fotobot_Email.getId());
        tv_Fotobot_Email.setLayoutParams(lpView_email);
        linLayout_Fotobot_Email.addView(tv_Fotobot_Email);

// Почтовый адрес
        editText_Fotobot_Email = new EditText(this);
        editText_Fotobot_Email.setLayoutParams(lpView_et);
        editText_Fotobot_Email.setSingleLine(true);
        editText_Fotobot_Email.setText(fb.EMail_Sender);
        editText_Fotobot_Email.setTextColor(Color.rgb(50, 100, 150));
        ViewGroup.LayoutParams lp = editText_Fotobot_Email.getLayoutParams();
        editText_Fotobot_Email.setWidth((screenWidth - padding)/100*40);
        editText_Fotobot_Email.setLayoutParams(lpView_email);
        editText_Fotobot_Email.setGravity(Gravity.RIGHT);

        lpView_email_m1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, editText_Fotobot_Email.getId());
        editText_Fotobot_Email.setLayoutParams(lpView_email_m1);
        linLayout_Fotobot_Email.addView(editText_Fotobot_Email);

// Заметка для почты Фотобота
        TextView tv_Fotobot_Email_note = new TextView(this);
        tv_Fotobot_Email_note.setTypeface(null, Typeface.NORMAL);
        tv_Fotobot_Email_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Fotobot_Email_note.setTextColor(Color.BLACK);
        tv_Fotobot_Email_note.setText(getResources().getString(R.string.Fotobot_email_description));
        tv_Fotobot_Email_note.setLayoutParams(lpView);
        tv_Fotobot_Email_note.setPadding(5, 9, 5, 9);
        linLayout_Fotobot_Email_note.addView(tv_Fotobot_Email_note);

// Разделитель
        View line_Fotobot_Email = new View(this);
        line_Fotobot_Email.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line_Fotobot_Email.setBackgroundColor(Color.rgb(210, 210, 210));
        line_Fotobot_Email.getLayoutParams().height = 3;
        linLayout_Fotobot_Email_divider.addView(line_Fotobot_Email);

// ------------------------------------------------------------------------------------------------

// Fotobot's password

// E-Mail Container
        RelativeLayout linLayout_Fotobot_Password = new RelativeLayout(this);
        //   linLayout_Fotobot_Email.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams lpView_email_passwd = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_email_passwd_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Fotobot_Password.setPadding(5, 9, 5, 9);
        linLayout_Fotobot_Password.setBackgroundColor(Color.rgb(192, 192, 192));

// Пояснение контейнер
        LinearLayout linLayout_Fotobot_Password_note = new LinearLayout(this);
        linLayout_Fotobot_Password_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Fotobot_Password_note.setPadding(5, 9, 5, 9);
        linLayout_Fotobot_Password_note.setBackgroundColor(Color.rgb(192, 192, 192));

// Контейнер для разделителя
        LinearLayout linLayout_Fotobot_Password_divider = new LinearLayout(this);
        linLayout_Fotobot_Password_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Fotobot_Password_divider.setPadding(5, 9, 5, 9);

// Название
        TextView tv_Fotobot_Password = new TextView(this);
        tv_Fotobot_Password.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Fotobot_Password.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Fotobot_Password.setTextColor(Color.BLACK);
        tv_Fotobot_Password.setText(getResources().getString(R.string.Fotobot_email_password));
        tv_Fotobot_Password.setMinimumWidth((screenWidth - padding) / 100 * 60);
        tv_Fotobot_Password.setLayoutParams(lpView_email);

        lpView_email_passwd.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Fotobot_Password.getId());
        tv_Fotobot_Password.setLayoutParams(lpView_email_passwd);
        linLayout_Fotobot_Password.addView(tv_Fotobot_Password);

// Пароль
        editText_Fotobot_Password = new EditText(this);
        editText_Fotobot_Password.setLayoutParams(lpView_et);
        editText_Fotobot_Password.setSingleLine(true);

        editText_Fotobot_Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText_Fotobot_Password.setText(fb.EMail_Sender_Password);

        editText_Fotobot_Password.setTextColor(Color.rgb(50, 100, 150));
        ViewGroup.LayoutParams lp1 = editText_Fotobot_Email.getLayoutParams();
        editText_Fotobot_Password.setWidth((screenWidth - padding)/100*40);
        editText_Fotobot_Password.setLayoutParams(lpView_email);
        editText_Fotobot_Password.setGravity(Gravity.RIGHT);

        lpView_email_passwd_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, editText_Fotobot_Password.getId());
        editText_Fotobot_Password.setLayoutParams(lpView_email_passwd_m);
        linLayout_Fotobot_Password.addView(editText_Fotobot_Password);

// Заметка для пароля
        TextView tv_Fotobot_Password_note = new TextView(this);
        tv_Fotobot_Password_note.setTypeface(null, Typeface.NORMAL);
        tv_Fotobot_Password_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Fotobot_Password_note.setTextColor(Color.BLACK);
        tv_Fotobot_Password_note.setText("Пароль.");
        tv_Fotobot_Password_note.setLayoutParams(lpView);
        tv_Fotobot_Password_note.setPadding(5, 9, 5, 9);
        linLayout_Fotobot_Password_note.addView(tv_Fotobot_Password_note);

// Разделитель
        View line_Fotobot_Password = new View(this);
        line_Fotobot_Password.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line_Fotobot_Password.setBackgroundColor(Color.rgb(210, 210, 210));
        line_Fotobot_Password.getLayoutParams().height = 3;
        linLayout_Fotobot_Password_divider.addView(line_Fotobot_Password);

// ------------------------------------------------------------------------------------------------

// Fotobot's recipient

// Recipient Container
        RelativeLayout linLayout_Fotobot_Recipient = new RelativeLayout(this);
        //   linLayout_Fotobot_Recipient.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams lpView_recipient = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_recipient_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Fotobot_Recipient.setPadding(5, 9, 5, 9);
        linLayout_Fotobot_Recipient.setBackgroundColor(Color.rgb(192, 192, 192));

// Пояснение контейнер
        LinearLayout linLayout_Fotobot_Recipient_note = new LinearLayout(this);
        linLayout_Fotobot_Recipient_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Fotobot_Recipient_note.setPadding(5, 9, 5, 9);
        linLayout_Fotobot_Recipient_note.setBackgroundColor(Color.rgb(192, 192, 192));

// Контейнер для разделителя
        LinearLayout linLayout_Fotobot_Recipient_divider = new LinearLayout(this);
        linLayout_Fotobot_Recipient_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Fotobot_Recipient_divider.setPadding(5, 9, 5, 9);

// Название
        TextView tv_Fotobot_Recipient = new TextView(this);
        tv_Fotobot_Recipient.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Fotobot_Recipient.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Fotobot_Recipient.setTextColor(Color.BLACK);
        tv_Fotobot_Recipient.setText(getResources().getString(R.string.Recipient_email));
        tv_Fotobot_Recipient.setMinimumWidth((screenWidth - padding) / 100 * 60);
        tv_Fotobot_Recipient.setLayoutParams(lpView_email);

        lpView_recipient.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_Fotobot_Recipient.getId());
        tv_Fotobot_Recipient.setLayoutParams(lpView_recipient);
        linLayout_Fotobot_Recipient.addView(tv_Fotobot_Recipient);

// Почтовый адрес получателя
        editText_Fotobot_Recipient = new EditText(this);
        editText_Fotobot_Recipient.setLayoutParams(lpView_et);
        editText_Fotobot_Recipient.setSingleLine(true);
        editText_Fotobot_Recipient.setText(fb.EMail_Recepient);
        editText_Fotobot_Recipient.setTextColor(Color.rgb(50, 100, 150));
        editText_Fotobot_Recipient.setWidth((screenWidth - padding)/100*40);
        editText_Fotobot_Recipient.setLayoutParams(lpView_email);
        editText_Fotobot_Recipient.setGravity(Gravity.RIGHT);

        lpView_recipient_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, editText_Fotobot_Recipient.getId());
        editText_Fotobot_Recipient.setLayoutParams(lpView_recipient_m);
        linLayout_Fotobot_Recipient.addView(editText_Fotobot_Recipient);

// Заметка для почты Фотобота
        TextView tv_Fotobot_Recipient_note = new TextView(this);
        tv_Fotobot_Recipient_note.setTypeface(null, Typeface.NORMAL);
        tv_Fotobot_Recipient_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Fotobot_Recipient_note.setTextColor(Color.BLACK);
        tv_Fotobot_Recipient_note.setText("Получатель писем с фото");
        tv_Fotobot_Recipient_note.setLayoutParams(lpView);
        tv_Fotobot_Recipient_note.setPadding(5, 9, 5, 9);
        linLayout_Fotobot_Recipient_note.addView(tv_Fotobot_Recipient_note);

// Разделитель
        View line_Fotobot_Recipient = new View(this);
        line_Fotobot_Recipient.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line_Fotobot_Recipient.setBackgroundColor(Color.rgb(210, 210, 210));
        line_Fotobot_Recipient.getLayoutParams().height = 3;
        linLayout_Fotobot_Recipient_divider.addView(line_Fotobot_Recipient);

// ------------------------------------------------------------------------------------------------

// SMTP Host

// SMTPHost Container
        RelativeLayout linLayout_SMTP_Host = new RelativeLayout(this);
        //  linLayout_SMTP_Host.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams lpView_smtp_host = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_smtp_host_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linLayout_SMTP_Host.setPadding(5, 9, 5, 9);
        linLayout_SMTP_Host.setBackgroundColor(Color.rgb(192, 192, 192));

// Пояснение контейнер
        LinearLayout linLayout_SMTP_Host_note = new LinearLayout(this);
        linLayout_SMTP_Host_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_SMTP_Host_note.setPadding(5, 9, 5, 9);
        linLayout_SMTP_Host_note.setBackgroundColor(Color.rgb(192, 192, 192));

// Контейнер для разделителя
        LinearLayout linLayout_SMTP_Host_divider = new LinearLayout(this);
        linLayout_SMTP_Host_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_SMTP_Host_divider.setPadding(5, 9, 5, 9);

// Название
        TextView tv_SMTP_Host = new TextView(this);
        tv_SMTP_Host.setTypeface(Typeface.DEFAULT_BOLD);
        tv_SMTP_Host.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_SMTP_Host.setTextColor(Color.BLACK);
        tv_SMTP_Host.setText(getResources().getString(R.string.Fotobot_email));
        tv_SMTP_Host.setMinimumWidth((screenWidth - padding) / 100 * 60);
        tv_SMTP_Host.setLayoutParams(lpView_email);

        lpView_smtp_host.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_SMTP_Host.getId());
        tv_SMTP_Host.setLayoutParams(lpView_smtp_host);
        linLayout_SMTP_Host.addView(tv_SMTP_Host);

// Адрес почтового сервера
        editText_SMTP_Host = new EditText(this);
        editText_SMTP_Host.setLayoutParams(lpView_et);
        editText_SMTP_Host.setSingleLine(true);
        editText_SMTP_Host.setText(fb.SMTP_Host);
        editText_SMTP_Host.setTextColor(Color.rgb(50, 100, 150));
        editText_SMTP_Host.setWidth((screenWidth - padding)/100*40);
        editText_SMTP_Host.setLayoutParams(lpView_email);
        editText_SMTP_Host.setGravity(Gravity.RIGHT);

        lpView_smtp_host_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, editText_SMTP_Host.getId());
        editText_SMTP_Host.setLayoutParams(lpView_smtp_host_m);
        linLayout_SMTP_Host.addView(editText_SMTP_Host);

// Заметка для SMTP Host
        TextView tv_SMTP_Host_note = new TextView(this);
        tv_SMTP_Host_note.setTypeface(null, Typeface.NORMAL);
        tv_SMTP_Host_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_SMTP_Host_note.setTextColor(Color.BLACK);
        tv_SMTP_Host_note.setText(getResources().getString(R.string.email_server_description));
        tv_SMTP_Host_note.setLayoutParams(lpView);
        tv_SMTP_Host_note.setPadding(5, 9, 5, 9);
        linLayout_SMTP_Host_note.addView(tv_SMTP_Host_note);

// Разделитель
        View line_SMTP_Host = new View(this);
        line_SMTP_Host.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line_SMTP_Host.setBackgroundColor(Color.rgb(210, 210, 210));
        line_SMTP_Host.getLayoutParams().height = 3;
        linLayout_SMTP_Host_divider.addView(line_SMTP_Host);

// ------------------------------------------------------------------------------------------------

// SMTP Port

// SMTP Port Container
        RelativeLayout linLayout_SMTP_Port = new RelativeLayout(this);
        //  linLayout_SMTP_Port.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams lpView_smtp_port = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lpView_smtp_port_m = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        linLayout_SMTP_Port.setPadding(5, 9, 5, 9);
        linLayout_SMTP_Port.setBackgroundColor(Color.rgb(192, 192, 192));

// Пояснение контейнер
        LinearLayout linLayout_SMTP_Port_note = new LinearLayout(this);
        linLayout_SMTP_Port_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_SMTP_Port_note.setPadding(5, 9, 5, 9);
        linLayout_SMTP_Port_note.setBackgroundColor(Color.rgb(192, 192, 192));

// Контейнер для разделителя
        LinearLayout linLayout_SMTP_Port_divider = new LinearLayout(this);
        linLayout_SMTP_Port_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_SMTP_Port_divider.setPadding(5, 9, 5, 9);

// Название
        TextView tv_SMTP_Port = new TextView(this);
        tv_SMTP_Port.setTypeface(Typeface.DEFAULT_BOLD);
        tv_SMTP_Port.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_SMTP_Port.setTextColor(Color.BLACK);
        tv_SMTP_Port.setText(getResources().getString(R.string.email_server_port));
        tv_SMTP_Port.setMinimumWidth((screenWidth - padding) / 100 * 80);
        tv_SMTP_Port.setLayoutParams(lpView_email);

        lpView_smtp_port.addRule(RelativeLayout.ALIGN_PARENT_LEFT, tv_SMTP_Port.getId());
        tv_SMTP_Port.setLayoutParams(lpView_smtp_port);
        linLayout_SMTP_Port.addView(tv_SMTP_Port);

// Порт почтового сервера
        editText_SMTP_Port = new EditText(this);
        editText_SMTP_Port.setLayoutParams(lpView_et);
        editText_SMTP_Port.setSingleLine(true);
        editText_SMTP_Port.setText(fb.SMTP_Port);
        editText_SMTP_Port.setTextColor(Color.rgb(50, 100, 150));
        editText_SMTP_Port.setWidth((screenWidth - padding)/100*20);
        editText_SMTP_Port.setLayoutParams(lpView_email);
        editText_SMTP_Port.setGravity(Gravity.RIGHT);

        lpView_smtp_port_m.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, editText_SMTP_Port.getId());
        editText_SMTP_Port.setLayoutParams(lpView_smtp_port_m);
        linLayout_SMTP_Port.addView(editText_SMTP_Port);

// Заметка для SMTP Port
        TextView tv_SMTP_Port_note = new TextView(this);
        tv_SMTP_Port_note.setTypeface(null, Typeface.NORMAL);
        tv_SMTP_Port_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_SMTP_Port_note.setTextColor(Color.BLACK);
        tv_SMTP_Port_note.setText(getResources().getString(R.string.email_server_port_description));
        tv_SMTP_Port_note.setLayoutParams(lpView);
        tv_SMTP_Port_note.setPadding(5, 9, 5, 9);
        linLayout_SMTP_Port_note.addView(tv_SMTP_Port_note);

// Разделитель
        View line_SMTP_Port = new View(this);
        line_SMTP_Port.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line_SMTP_Port.setBackgroundColor(Color.rgb(210, 210, 210));
        line_SMTP_Port.getLayoutParams().height = 3;
        linLayout_SMTP_Port_divider.addView(line_SMTP_Port);

// Second Container (Horizontal LinearLayout)
        LinearLayout linLayout2 = new LinearLayout(this);
        linLayout2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams lpViewbutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        linLayout2.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        linLayout2.setLayoutParams(lpView2);

// Password Container
        LinearLayout linLayout_email_password = new LinearLayout(this);
        linLayout_email_password.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView_email_password = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// E-Mail Container2
        LinearLayout linLayout_email_recepient = new LinearLayout(this);
        linLayout_email_recepient.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView_email_recepient = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

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
                editor.putString("EMail_Recepient", editText_Fotobot_Recipient.getText().toString());
                editor.putString("SMTP_Host", editText_SMTP_Host.getText().toString());
                editor.putString("SMTP_Port", editText_SMTP_Port.getText().toString());
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

        if (Build.VERSION.SDK_INT <= 21) {

            FullFrame.addView(linLayout_Channels);
            FullFrame.addView(linLayout_Channels_note);
            //  FullFrame.addView(linLayout_Channels_divider);

            FullFrame.addView(linLayout_Connection_Method);
            FullFrame.addView(linLayout_Connection_Method_note);
            //  FullFrame.addView(linLayout_Connection_Method_divider);

        }

        FullFrame.addView(linLayout_Fotobot_Email);
        FullFrame.addView(linLayout_Fotobot_Email_note);
        //  FullFrame.addView(linLayout_Fotobot_Email_divider);

        FullFrame.addView(linLayout_Fotobot_Password);
       // FullFrame.addView(linLayout_Fotobot_Password_note);
        //  FullFrame.addView(linLayout_Fotobot_Password_divider);

        FullFrame.addView(linLayout_Fotobot_Recipient);
        //  FullFrame.addView(linLayout_Fotobot_Recipient_divider);

        FullFrame.addView(linLayout_SMTP_Host);
        FullFrame.addView(linLayout_SMTP_Host_note);
        //   FullFrame.addView(linLayout_SMTP_Host_divider);

        FullFrame.addView(linLayout_SMTP_Port);
        FullFrame.addView(linLayout_SMTP_Port_note);

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
        fb.LoadData();
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
