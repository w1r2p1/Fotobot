package com.example.andrey.fotobot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
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
    Spinner spinner_Channels;
    Spinner spinner_Connection_Method;
    EditText editText_Fotobot_Email;
    EditText editText_Fotobot_Password;
    EditText editText_Fotobot_Recipient;
    EditText editText_SMTP_Host;
    private CheckBox check_box_flash;
    private EditText edit_text_jpeg_compression;
    private int screenWidth, screenHeight;
    private int padding = 5;
    final String LOG_NETWORK_ACTIVITY = "Logs";

    String[] connect_methods = {"Wi-Fi", "Mobile Data", "Both"};



    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_DeviceDefault_Light);
        super.onCreate(savedInstanceState);

        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.LoadData();
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

// Main Container (Vertical LinearLayout)
// Главный контейнер внутри которого вся раскладка
        LinearLayout FullFrame = new LinearLayout(this);
        FullFrame.setOrientation(LinearLayout.VERTICAL);
        FullFrame.setPadding(5, padding, 0, 0);

// ------------------------------------------------------------------------------------------------

// Каналы связи

// Контейнер для каналов связи
        LinearLayout linLayout_Channels = new LinearLayout(this);
        linLayout_Channels.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Пояснение контейнер
        LinearLayout linLayout_Channels_note = new LinearLayout(this);
        linLayout_Channels_note.setOrientation(LinearLayout.HORIZONTAL);

// Контейнер для разделителя
        LinearLayout linLayout_Channels_divider = new LinearLayout(this);
        linLayout_Channels_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Channels_divider.setPadding(5, 15, 5, 15);

// Название
        TextView tv_Channels = new TextView(this);
        tv_Channels.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Channels.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Channels.setTextColor(Color.BLACK);
        tv_Channels.setText("Каналы связи");
        tv_Channels.setWidth((screenWidth - padding) / 100 * 70);
        tv_Channels.setLayoutParams(lpView);
        linLayout_Channels.addView(tv_Channels);

// Список
        ArrayList<String> spinnerArray_Channels = new ArrayList<String>();
        spinnerArray_Channels.add("Mobile Data");
        spinnerArray_Channels.add("Wi-Fi");
        spinnerArray_Channels.add("Both");

        spinner_Channels = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray_Channels);
        spinner_Channels.setAdapter(spinnerArrayAdapter1);
        spinner_Channels.setSelection(getIndex(spinner_Channels, fb.Network_Channel));
        spinner_Channels.setMinimumWidth((screenWidth - padding) / 100 * 30);
        linLayout_Channels.addView(spinner_Channels);

// Заметка для каналов связи
        TextView tv_Channels_note = new TextView(this);
        tv_Channels_note.setTypeface(null, Typeface.ITALIC);
        tv_Channels_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Channels_note.setTextColor(Color.BLACK);
        tv_Channels_note.setText("Выберите способ подключения к Internet (мобильные данные, wi-fi или оба).");
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_Channels_note.setLayoutParams(lpView);
        tv_Channels_note.setTextColor(Color.GRAY);
        tv_Channels_note.setPadding(5, 15, 5, 15);
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
        LinearLayout linLayout_Connection_Method = new LinearLayout(this);
        linLayout_Connection_Method.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView_Flash = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lpView_et_Flash = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// Пояснение контейнер
        LinearLayout linLayout_Connection_Method_note = new LinearLayout(this);
        linLayout_Connection_Method_note.setOrientation(LinearLayout.HORIZONTAL);

// Контейнер для разделителя
        LinearLayout linLayout_Connection_Method_divider = new LinearLayout(this);
        linLayout_Connection_Method_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Connection_Method_divider.setPadding(5, 15, 5, 15);

// Название
        TextView tv_Connection_Method = new TextView(this);
        tv_Connection_Method.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Connection_Method.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Connection_Method.setTextColor(Color.BLACK);
        tv_Connection_Method.setText("Метод подключения");
        tv_Connection_Method.setWidth((screenWidth - padding) / 100 * 70);
        tv_Connection_Method.setLayoutParams(lpView_Flash);
        linLayout_Connection_Method.addView(tv_Connection_Method);

// Список
        ArrayList<String> spinnerArray_Connection_Method = new ArrayList<String>();
        spinnerArray_Connection_Method.add("В начале работы");
        spinnerArray_Connection_Method.add("На каждом шаге");

        spinner_Connection_Method = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray_Connection_Method);
        spinner_Connection_Method.setAdapter(spinnerArrayAdapter2);
        spinner_Connection_Method.setSelection(getIndex(spinner_Connection_Method, fb.Network_Connection_Method));
        spinner_Connection_Method.setMinimumWidth((screenWidth - padding) / 100 * 30);
        linLayout_Connection_Method.addView(spinner_Connection_Method);

// Заметка для каналов связи
        TextView tv_Connection_Method_note = new TextView(this);
        tv_Connection_Method_note.setTypeface(null, Typeface.ITALIC);
        tv_Connection_Method_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Connection_Method_note.setTextColor(Color.BLACK);
        tv_Connection_Method_note.setText("Выберите режим установки связи с Internet. В начале работы - связь будет установлена сразу и не будет отключаться от сети." +
                "На кажом шаге - соедение с Internet будет устонавливаться перед тем, как сделать фото, затем фотобот отключится от Internet.");
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_Connection_Method_note.setLayoutParams(lpView);
        tv_Connection_Method_note.setTextColor(Color.GRAY);
        tv_Connection_Method_note.setPadding(5, 15, 5, 15);
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
        LinearLayout linLayout_Fotobot_Email = new LinearLayout(this);
        linLayout_Fotobot_Email.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView_email = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Fotobot_Email.setPadding(5, 15, 5, 15);

// Пояснение контейнер
        LinearLayout linLayout_Fotobot_Email_note = new LinearLayout(this);
        linLayout_Fotobot_Email_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Fotobot_Email_note.setPadding(5, 15, 5, 15);

// Контейнер для разделителя
        LinearLayout linLayout_Fotobot_Email_divider = new LinearLayout(this);
        linLayout_Fotobot_Email_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Fotobot_Email_divider.setPadding(5, 15, 5, 15);

// Название
        TextView tv_Fotobot_Email = new TextView(this);
        tv_Fotobot_Email.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Fotobot_Email.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Fotobot_Email.setTextColor(Color.BLACK);
        tv_Fotobot_Email.setText("Почтовый адрес\n Фотобота\n");
        tv_Fotobot_Email.setMinimumWidth((screenWidth - padding) / 100 * 50);
        tv_Fotobot_Email.setLayoutParams(lpView_email);
        linLayout_Fotobot_Email.addView(tv_Fotobot_Email);

// Почтовый адрес
        editText_Fotobot_Email = new EditText(this);
        editText_Fotobot_Email.setLayoutParams(lpView_et);
        editText_Fotobot_Email.setSingleLine(true);
        editText_Fotobot_Email.setText(fb.EMail_Sender);
        ViewGroup.LayoutParams lp = editText_Fotobot_Email.getLayoutParams();
        editText_Fotobot_Email.setWidth((screenWidth - padding) - ((screenWidth - padding) / 100 * 50));
        // lp.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 50);
        editText_Fotobot_Email.setLayoutParams(lpView_email);
        editText_Fotobot_Email.setGravity(Gravity.RIGHT);
        linLayout_Fotobot_Email.addView(editText_Fotobot_Email);

// Заметка для почты Фотобота
        TextView tv_Fotobot_Email_note = new TextView(this);
        tv_Fotobot_Email_note.setTypeface(null, Typeface.ITALIC);
        tv_Fotobot_Email_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Fotobot_Email_note.setTextColor(Color.BLACK);
        tv_Fotobot_Email_note.setText("Почтовый адрес с которого Фотобот будет рассылать письма с фото.");
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_Fotobot_Email_note.setLayoutParams(lpView);
        tv_Fotobot_Email_note.setTextColor(Color.GRAY);
        tv_Fotobot_Email_note.setPadding(5, 15, 5, 15);
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
        LinearLayout linLayout_Fotobot_Password = new LinearLayout(this);
        linLayout_Fotobot_Email.setOrientation(LinearLayout.HORIZONTAL);
//        LinearLayout.LayoutParams lpView_email = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Fotobot_Password.setPadding(5, 15, 5, 15);

// Пояснение контейнер
        LinearLayout linLayout_Fotobot_Password_note = new LinearLayout(this);
        linLayout_Fotobot_Password_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Fotobot_Password_note.setPadding(5, 15, 5, 15);

// Контейнер для разделителя
        LinearLayout linLayout_Fotobot_Password_divider = new LinearLayout(this);
        linLayout_Fotobot_Password_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Fotobot_Password_divider.setPadding(5, 15, 5, 15);

// Название
        TextView tv_Fotobot_Password = new TextView(this);
        tv_Fotobot_Password.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Fotobot_Password.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Fotobot_Password.setTextColor(Color.BLACK);
        tv_Fotobot_Password.setText("Пароль к почтовому\n ящику Фотобота\n");
        tv_Fotobot_Password.setMinimumWidth((screenWidth - padding) / 100 * 50);
        tv_Fotobot_Password.setLayoutParams(lpView_email);
        linLayout_Fotobot_Password.addView(tv_Fotobot_Password);

// Пароль
        editText_Fotobot_Password = new EditText(this);
        editText_Fotobot_Password.setLayoutParams(lpView_et);
        editText_Fotobot_Password.setSingleLine(true);
        editText_Fotobot_Password.setText(fb.EMail_Sender_Password);
        ViewGroup.LayoutParams lp1 = editText_Fotobot_Email.getLayoutParams();
        editText_Fotobot_Password.setWidth((screenWidth - padding) - ((screenWidth - padding) / 100 * 50));
        // lp.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 50);
        editText_Fotobot_Password.setLayoutParams(lpView_email);
        editText_Fotobot_Password.setGravity(Gravity.RIGHT);
        linLayout_Fotobot_Password.addView(editText_Fotobot_Password);

// Заметка для пароля
        TextView tv_Fotobot_Password_note = new TextView(this);
        tv_Fotobot_Password_note.setTypeface(null, Typeface.ITALIC);
        tv_Fotobot_Password_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Fotobot_Password_note.setTextColor(Color.BLACK);
        tv_Fotobot_Password_note.setText("Пароль.");
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_Fotobot_Password_note.setLayoutParams(lpView);
        tv_Fotobot_Password_note.setTextColor(Color.GRAY);
        tv_Fotobot_Password_note.setPadding(5, 15, 5, 15);
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
        LinearLayout linLayout_Fotobot_Recipient = new LinearLayout(this);
        linLayout_Fotobot_Recipient.setOrientation(LinearLayout.HORIZONTAL);
//        LinearLayout.LayoutParams lpView_email = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout_Fotobot_Recipient.setPadding(5, 15, 5, 15);

// Пояснение контейнер
        LinearLayout linLayout_Fotobot_Recipient_note = new LinearLayout(this);
        linLayout_Fotobot_Recipient_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Fotobot_Recipient_note.setPadding(5, 15, 5, 15);

// Контейнер для разделителя
        LinearLayout linLayout_Fotobot_Recipient_divider = new LinearLayout(this);
        linLayout_Fotobot_Recipient_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_Fotobot_Recipient_divider.setPadding(5, 15, 5, 15);

// Название
        TextView tv_Fotobot_Recipient = new TextView(this);
        tv_Fotobot_Recipient.setTypeface(Typeface.DEFAULT_BOLD);
        tv_Fotobot_Recipient.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_Fotobot_Recipient.setTextColor(Color.BLACK);
        tv_Fotobot_Recipient.setText("Почтовый адрес\n получателя\n");
        tv_Fotobot_Recipient.setMinimumWidth((screenWidth - padding) / 100 * 50);
        tv_Fotobot_Recipient.setLayoutParams(lpView_email);
        linLayout_Fotobot_Recipient.addView(tv_Fotobot_Recipient);

// Почтовый адрес получателя
        editText_Fotobot_Recipient = new EditText(this);
        editText_Fotobot_Recipient.setLayoutParams(lpView_et);
        editText_Fotobot_Recipient.setSingleLine(true);
        editText_Fotobot_Recipient.setText(fb.EMail_Sender);
        //ViewGroup.LayoutParams lp = editText_Fotobot_Email.getLayoutParams();
        editText_Fotobot_Recipient.setWidth((screenWidth - padding) - ((screenWidth - padding) / 100 * 50));
        // lp.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 50);
        editText_Fotobot_Recipient.setLayoutParams(lpView_email);
        editText_Fotobot_Recipient.setGravity(Gravity.RIGHT);
        linLayout_Fotobot_Recipient.addView(editText_Fotobot_Recipient);

// Заметка для почты Фотобота
        TextView tv_Fotobot_Recipient_note = new TextView(this);
        tv_Fotobot_Recipient_note.setTypeface(null, Typeface.ITALIC);
        tv_Fotobot_Recipient_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_Fotobot_Recipient_note.setTextColor(Color.BLACK);
        tv_Fotobot_Recipient_note.setText("Получатель писем с фото");
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_Fotobot_Recipient_note.setLayoutParams(lpView);
        tv_Fotobot_Recipient_note.setTextColor(Color.GRAY);
        tv_Fotobot_Recipient_note.setPadding(5, 15, 5, 15);
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
        LinearLayout linLayout_SMTP_Host = new LinearLayout(this);
        linLayout_SMTP_Host.setOrientation(LinearLayout.HORIZONTAL);
//        LinearLayout.LayoutParams lpView_email = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayout_SMTP_Host.setPadding(5, 15, 5, 15);

// Пояснение контейнер
        LinearLayout linLayout_SMTP_Host_note = new LinearLayout(this);
        linLayout_SMTP_Host_note.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_SMTP_Host_note.setPadding(5, 15, 5, 15);

// Контейнер для разделителя
        LinearLayout linLayout_SMTP_Host_divider = new LinearLayout(this);
        linLayout_SMTP_Host_divider.setOrientation(LinearLayout.HORIZONTAL);
        linLayout_SMTP_Host_divider.setPadding(5, 15, 5, 15);

// Название
        TextView tv_SMTP_Host = new TextView(this);
        tv_SMTP_Host.setTypeface(Typeface.DEFAULT_BOLD);
        tv_SMTP_Host.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size);
        tv_SMTP_Host.setTextColor(Color.BLACK);
        tv_SMTP_Host.setText("Адрес почтового\n сервера\n");
        tv_SMTP_Host.setMinimumWidth((screenWidth - padding) / 100 * 50);
        tv_SMTP_Host.setLayoutParams(lpView_email);
        linLayout_SMTP_Host.addView(tv_SMTP_Host);

// Адрес почтового сервера
        editText_SMTP_Host = new EditText(this);
        editText_SMTP_Host.setLayoutParams(lpView_et);
        editText_SMTP_Host.setSingleLine(true);
        editText_SMTP_Host.setText(fb.EMail_Sender);
        //ViewGroup.LayoutParams lp = editText_Fotobot_Email.getLayoutParams();
        editText_SMTP_Host.setWidth((screenWidth - padding) - ((screenWidth - padding) / 100 * 50));
        // lp.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 50);
        editText_SMTP_Host.setLayoutParams(lpView_email);
        editText_SMTP_Host.setGravity(Gravity.RIGHT);
        linLayout_SMTP_Host.addView(editText_SMTP_Host);

// Заметка для SMTP Host
        TextView tv_SMTP_Host_note = new TextView(this);
        tv_SMTP_Host_note.setTypeface(null, Typeface.ITALIC);
        tv_SMTP_Host_note.setTextSize(TypedValue.COMPLEX_UNIT_SP, fb.Config_Font_Size - 2);
        tv_SMTP_Host_note.setTextColor(Color.BLACK);
        tv_SMTP_Host_note.setText("Получатель писем с фото");
        // tv_Channels_notes.setWidth((screenWidth - padding) / 100 * 99);
        tv_SMTP_Host_note.setLayoutParams(lpView);
        tv_SMTP_Host_note.setTextColor(Color.GRAY);
        tv_SMTP_Host_note.setPadding(5, 15, 5, 15);
        linLayout_SMTP_Host_note.addView(tv_SMTP_Host_note);

// Разделитель
        View line_SMTP_Host = new View(this);
        line_SMTP_Host.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1));
        line_SMTP_Host.setBackgroundColor(Color.rgb(210, 210, 210));
        line_SMTP_Host.getLayoutParams().height = 3;
        linLayout_SMTP_Host_divider.addView(line_SMTP_Host);



















// Second Container (Horizontal LinearLayout)
        LinearLayout linLayout2 = new LinearLayout(this);
        linLayout2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpView2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams lpViewbutton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

//        linLayout2.setBackgroundColor(Color.parseColor("#0000ff"));
        linLayout2.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        linLayout2.setLayoutParams(lpView2);



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







// TextView4
   //     TextView tv_email_password = new TextView(this);
   //     tv_email_password.setTypeface(Typeface.DEFAULT_BOLD);
   //     tv_email_password.setTextSize(14);
   //     tv_email_password.setTextColor(Color.BLACK);
   //     tv_email_password.setText("e-mail passswd");
   //     tv_email_password.setMinimumWidth((screenWidth - padding) / 100 * 50);
   //     tv_email_password.setLayoutParams(lpView_email);
   //     linLayout_email_password.addView(tv_email_password);

// Password
     //   editText_email_password = new EditText(this);
     //   editText_email_password.setLayoutParams(lpView_et);
    //    editText_email_password.setSingleLine(true);
    //    editText_email_password.setText(fb.EMail_Sender_Password);
      //  ViewGroup.LayoutParams lp_password = editText_email_password.getLayoutParams();
       // editText_email_password.setWidth((screenWidth - padding) - ((screenWidth - padding) / 100 * 50));
      //  editText_email_password.setLayoutParams(lpView_email_password);
      //  editText_email_password.setGravity(Gravity.RIGHT);
      //  linLayout_email_password.addView(editText_email_password);

        // TextView4
 //       TextView tv_email_recepient = new TextView(this);
 //       tv_email_recepient.setTypeface(Typeface.DEFAULT_BOLD);
 //       tv_email_recepient.setTextSize(14);
 //       tv_email_recepient.setTextColor(Color.BLACK);
 //       tv_email_recepient.setText("e-mail получателя");
 //       tv_email_recepient.setMinimumWidth((screenWidth - padding) / 100 * 50);
 //       tv_email_recepient.setLayoutParams(lpView_email);
 //       linLayout_email_recepient.addView(tv_email_recepient);

// EditText
     //   editText_email_recepient = new EditText(this);
     //   editText_email_recepient.setLayoutParams(lpView_et);
     //   editText_email_recepient.setSingleLine(true);
     //   editText_email_recepient.setText(fb.EMail_Recepient);
     //   ViewGroup.LayoutParams lp_recepient = editText_email_recepient.getLayoutParams();
     //   editText_email_recepient.setWidth((screenWidth - padding) - ((screenWidth - padding) / 100 * 50));
    // //   lp_recepient.width = (screenWidth - padding) - ((screenWidth - padding) / 100 * 50);
       // editText_email_recepient.setLayoutParams(lpView_email_recepient);
       // editText_email_recepient.setGravity(Gravity.RIGHT);
       // linLayout_email_recepient.addView(editText_email_recepient);


// Apply Button
        Button btn = new Button(this);
        btn.setText("Применить");
        btn.setGravity(Gravity.CENTER);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("Network_Channel", spinner_Channels.getSelectedItem().toString());
                editor.putString("Network_Connection_Method", spinner_Connection_Method.getSelectedItem().toString());

                editor.putString("EMail_Sender", editText_Fotobot_Email.getText().toString());
                editor.putString("EMail_Sender_Password", editText_Fotobot_Password.getText().toString());
                editor.putString("EMail_Recepient", editText_Fotobot_Recipient.getText().toString());
                editor.putString("SMTP_Host", editText_Fotobot_Recipient.getText().toString());
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

        if ( Build.VERSION.SDK_INT <= 21 ) {

            FullFrame.addView(linLayout_Channels);
            FullFrame.addView(linLayout_Channels_note);
            FullFrame.addView(linLayout_Channels_divider);

            FullFrame.addView(linLayout_Connection_Method);
            FullFrame.addView(linLayout_Connection_Method_note);
            FullFrame.addView(linLayout_Connection_Method_divider);

        }

        FullFrame.addView(linLayout_Fotobot_Email);
        FullFrame.addView(linLayout_Fotobot_Email_note);
        FullFrame.addView(linLayout_Fotobot_Email_divider);

        FullFrame.addView(linLayout_Fotobot_Password);
        FullFrame.addView(linLayout_Fotobot_Password_note);
        FullFrame.addView(linLayout_Fotobot_Password_divider);

        FullFrame.addView(linLayout_Fotobot_Recipient);
        FullFrame.addView(linLayout_Fotobot_Recipient_divider);

        FullFrame.addView(linLayout_SMTP_Host);
        FullFrame.addView(linLayout_SMTP_Host_note);
        FullFrame.addView(linLayout_SMTP_Host_divider);

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
        spinner_Channels.setSelection(getIndex(spinner_Channels, fb.Network_Channel));
        spinner_Connection_Method.setSelection(getIndex(spinner_Connection_Method, fb.Network_Connection_Method));
        editText_Fotobot_Email.setText(fb.EMail_Sender);
        editText_Fotobot_Password.setText(fb.EMail_Sender_Password);
        editText_Fotobot_Recipient.setText(fb.EMail_Recepient);

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
