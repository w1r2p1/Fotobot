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
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.os.Environment.getExternalStoragePublicDirectory;

//import android.content.Loader;
//import android.provider.Browser;
//import java.util.logging.Logger;

/**
 * <h1>FotoBot</h1>
 * Умеет делать фото и отправлять на почту.
 * Это глобальный класс, объект данного класса будет виден во всех активити. Инициализируется через Manifest.
 * Все переменные с которыми работают методы других классов собраны здесь.
 */
public class FotoBot extends Application {

    public static final String MSG_PASS = "pass";
    public static final String MSG_FAIL = "fail";
    public static final String MSG_INFO = "info";
    final String LOG_TAG = "FotoBot";
    public int numberOfCameras = 1;

    public boolean sound_mute = false;

    public boolean back_camera = true;
    public boolean front_camera = false;
    public String versionName = "";
    /**
     * Будет использоваться в теме письма. Если Fotobot установлен на несколько телефонов,
     * то эта переменная поможет отсортировать письма.
     */
    public String Camera_Name = "default";
    /**
     * Если приложение запускается впервые на телефоне, то выскочит вступительная информация
     */
    public boolean launched_first_time = true;
    /**
     * Интервал фотографирования (в секундах)
     */
    public int Photo_Frequency = 300;
    /**
     * Нужно ли использовать Wi-Fi для выхода в Internet
     */
    public boolean Use_WiFi;
    /**
     * Нужно ли использовать 3G (2G) для выхода в Internet
     */
    public boolean Use_Mobile_Data;
    public boolean useMail = false;
    public boolean useFTP = false;
    /**
     * Делать фото со вспышкой
     */
    public boolean Use_Flash;
    /**
     * делать фото фронтальной камерой
     */
    public boolean make_photo_fc = false;
    /**
     * делать фото камерой на задней панели телефона
     */
    public boolean make_photo_bc = true;
    /**
     * делать видео фронтальной камерой
     */
    public boolean make_video_fc = true;
    /**
     * делать видео камерой на задней панели телефона
     */
    public boolean make_video_bc = true;
    /**
     * уровент заряда аккумулятора в %
     */
    public int battery_level;
    /**
     * заряжается ли от сети
     */
    public boolean isCharging_before = false;
    public boolean isCharging = false;
    public boolean isCharging_sms = false;
    public boolean sms_voltage_alert = false;
    public String sms_voltage_alert_number = "+11234567890";
    /**
     * Степень JPEG сжатия
     */
    public int JPEG_Compression = 90;
    /**
     * Коэффициент масштабирования фото (1/4,1/2,1)
     */
    public String Image_Scale = "1/4";
    /**
     * Номер изображения в текущей сессии
     */
    public int Image_Index = 0;
    /**
     * Размер изображения в пикселях по умолчанию для камеры на задней панели телефона
     */
    public String Image_Size = "1024x768";
    /**
     * Размер изображения в пикселях по умолчанию для фронтальой камеры
     */
    public String fc_Image_Size = "320x240";
    /**
     * Размер изображения в килобайтах
     */
    public int File_Size;
    /**
     * вспомогательная почта для отправки писем
     */
    public String EMail_Sender = "user@gmail.com";
    /**
     * пароль для вспомогательной почты
     */
    public String EMail_Sender_Password = "passwd";
    /**
     * кому отправлять письма с фотками
     */
    public String EMail_Recepient = "recipient@mail.ru";
    /**
     * почтовый сервер
     */
    public String SMTP_Host = "smtp.gmail.com";
    /**
     * порт для отправки писем не почтовом сервере
     */
    public String SMTP_Port = "465";
    public String Network_Channel = "Both";
    /**
     * Соединятся с Internet один раз (Method1) или на каждом шаге (Method2)
     */
    public String Network_Connection_Method = "Method 1";
    /**
     * Метод обработки фото (Hardaware или Software)
     */
    public String Photo_Post_Processing_Method = "Software";
    public int GSM_Signal = 0;
    public boolean bc_video_profile_initialized = false;
    /**
     * время записи видео
     */
    public int video_recording_time = 5;
    public boolean fc_video_profile_initialized = false;
    public String bc_current_video_profile = "QUALITY_LOW";
    public String fc_current_video_profile = "QUALITY_LOW";
    public int process_delay = 3;
    public int status = 1;
    public SurfaceHolder holder;
    public String str = "";
    public Handler h;
    public String Top;
    public boolean frame_delay = false;
    /**
     * Размер шрифта в настройках (sp)
     */
    public int Config_Font_Size = 20;
    /**
     * Размер шрифта в логе (sp)
     */
    public int Log_Font_Size = 14;
    public int Working_Area_Height = 240;
    public int menuheight;
    public String Image_Name;
    public String fc_Image_Name;

    //   public SurfaceHolder sHolder = null;
    public String bc_Image_Name;
    public String fc_Video_Name;
    public String bc_Video_Name;
    public boolean bc_image_ftp_upload = true;
    public boolean fc_image_ftp_upload = true;
    public boolean bc_video_ftp_upload = true;
    public boolean fc_video_ftp_upload = true;
    public boolean bc_image_attach = true;
    public boolean fc_image_attach = true;
    public boolean bc_video_attach = true;
    public boolean fc_video_attach = true;
    public boolean bc_image_delete = false;
    public boolean fc_image_delete = false;
    public boolean bc_video_delete = false;
    public boolean fc_video_delete = false;
    public String Image_Name_Full_Path;
    public String fc_Image_Name_Full_Path;
    public String bc_Image_Name_Full_Path;
    public String fc_Video_Name_Full_Path;
    public String bc_Video_Name_Full_Path;
    public String Attached_Info_Detailisation = "Normal";
    public Boolean show_start_tip = true;
    /**
     * Длина лога в главном окне
     */
    public int loglength = 1024;
    /**
     * Длина лога в файле
     */
    public int floglength = 1024;
    /**
     * Размера лога для команды logcat
     */
    public int log_size = 50;
    /**
     * присылать фото на почту или не присылать
     */
    public Boolean network = false;
    public String Upload_Method = "FTP";
    public Boolean Method1_activated = false;
    public boolean attach_log = false;
    public String FTP_server = "";
    public String FTP_port = "21";
    public String FTP_username = "";
    public String FTP_password = "";
    public Integer sms_number_of_strings;
    public String sms_sender_num;
    public String sms_passwd = "passwd";
    public String sms_incoming_passwd = "";
    public Boolean sms_status = false;
    public Boolean sms_net_status = false;
    public Boolean sms_email_status = false;
    public Boolean sms_media_status = false;
    public Boolean sms_help = false;
    public Boolean sms_update_db = false;
    public Boolean sms_check_file = false;
    public String storage_type;
    public String work_dir = "";
    public Boolean delete_foto = false;
    /**
     * Время (сек) необходимое на поднятие сетевого интерфейса
     */
    public int network_up_delay = 15;
    /**
     * Частота с которой Fotobot обращается к камере и стартует preview,
     * необходима для того, чтобы приложение не было выброшено из памяти
     */
    public int wake_up_interval = 60;
    public boolean thread_stopped = false;
    /**
     * Camera properties
     */
    public String Camera_Properties;
    public int bc_video_width = 320;
    public int bc_video_height = 240;
    /**
     * FC Camera properties
     */
    public String fc_Camera_Properties;
    public Boolean advanced_settings = false;
    public Boolean autofocus = false;
    public Boolean use_autofocus = true;
    public int time_for_focusing = 1;
    /**
     * Память в телефоне
     */
    public String freeMemory;
    public String totalMemory;
    public String usedMemory;
    /**
     * Логфайл
     */
    public int log_line_number = 150;
    /**
     * Строка на экран
     */
    public String log = "";
    public boolean clean_log = false;
    /**
     * Длительность отправки предыдущего письма
     */
    public long email_sending_time;
    /**
     * Паузы для fbpause
     */
    public int short_pause = 1;
    public int long_pause = 5;
    public String msg_status = "";
    public Boolean Tab_Main_Activity_activated = false;
    public Boolean Tab_Network_Activity_activated = false;
    public Boolean Tab_Foto_Activity_activated = false;
    public Boolean Tab_Video_Activity_activated = false;
    public boolean automatic_mode = false;
    Camera camera = null;
    /**
     * id фронтальной камеры
     */
    int fcId = -1;
    /**
     * id камеры на задней панели
     */
    int bcId = -1;
    /**
     * доступные разрешения (в пикселах) для камеры
     */
    List<Camera.Size> camera_resolutions;

//    public Boolean success_message = false;
//    public Boolean debug_message = false;
//    public Boolean error_message = false;
    /**
     * доступные разрешения (в пикселах) для фронтальной камеры
     */
    List<Camera.Size> fc_camera_resolutions;
    /**
     * доступные видеопрофили для камеры
     */
    List<String> bc_video_profile = new ArrayList<String>();
    /**
     * доступные видеопрофили для фронтальной камеры
     */
    List<String> fc_video_profile = new ArrayList<String>();
    List<String> sms = new ArrayList<String>();
    List<String> powerinfo = new ArrayList<String>();
    /**
     * Температура аккумулятора
     */
    float battery_temperature;

    /**
     * Возвращает текущее состояние FotoBot'а, сейчас не пользуюсь этим
     *
     * @return
     */
    public int getstatus() {

        return status;
    }

    /**
     * Устанавливает статус
     *
     * @param fb_status
     */
    public void setstatus(int fb_status) {

        status = fb_status;
    }

    public String getstr() {

        return str;
    }

    /**
     * В конструкторе проводим инициализацию объекта посредством считывания всех свойств из SharedPreferences.
     */
    public void FotoBot() {

        show_start_tip = true;
    }

    /**
     * isOnline - Check if there is a NetworkConnection
     *
     * @return boolean
     */
    public boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        fbpause(h, 3);

        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * FotoBot умеет самостоятельно подключаться к Internet для отправки фото на почту
     *
     * @param context
     * @param h
     * @return
     */

    private boolean enable_WiFi() {

        try {

            WiFi wf = new WiFi();
            SendMessage(getResources().getString(R.string.turning_on_wifi), MSG_INFO);
            wf.setWiFiEnabled(getApplicationContext(), true);
            fbpause(h, network_up_delay);

            return true;

        } catch (Exception e) {
            SendMessage("Couldn't turn on Wi-Fi", MSG_FAIL);
            e.printStackTrace();

        }

        return false;

    }

    private boolean enable_MobileData() {

        try {

            MobileData md = new MobileData();
            SendMessage(h, getResources().getString(R.string.turning_on_mobiledata));
            md.setMobileDataEnabled(getApplicationContext(), true);
            fbpause(h, network_up_delay);

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public boolean MakeInternetConnection() {

        if (isOnline()) {
            SendMessage(getResources().getString(R.string.Internet_connection_is_already_created), MSG_PASS);
            return true;
        }

        int connect_attempt;

        fbpause(h, 1);

        for (connect_attempt = 0; connect_attempt < 3; connect_attempt++) {

            SendMessage(getResources().getString(R.string.connection_attempt) + " " + (connect_attempt + 1));

            fbpause(h, 3);

            if (Network_Channel.contains("Wi-Fi")) {
                SendMessage(getResources().getString(R.string.connection_channel_wifi), MSG_PASS);
                enable_WiFi();
            }

            if (Network_Channel.contains("Mobile Data")) {
                SendMessage(getResources().getString(R.string.connection_channel_mobiledata), MSG_PASS);
                enable_MobileData();
            }

            if (Network_Channel.contains("Both")) {
                SendMessage(getResources().getString(R.string.connection_channel_wifimobiledata));
                if (enable_WiFi()) {
                    SendMessage(getResources().getString(R.string.connection_channel_wifimobiledata_wifi));
                } else if (enable_MobileData()) {
                    SendMessage(getResources().getString(R.string.connection_channel_wifimobiledata_mobiledata));
                }

            }

            if (isOnline()) {
                SendMessage(getResources().getString(R.string.Internet_connection), MSG_PASS);
                return true;
            }

            SendMessage(getResources().getString(R.string.pause_between_connections) + " 15 sec", MSG_INFO);

            fbpause(h, 15);

        }

        if (connect_attempt == 2) {
            SendMessage("Exiting without connecting to Internet, photo will be taken in offline mode.");
        }

        return false;

    }

    /**
     * FotoBot может самостоятельно отключаться от Internet
     *
     * @param context
     * @param h
     */
    public void CloseInternetConnection() {

        try {
            MobileData md = new MobileData();
//Sony Xperia error
// http://stackoverflow.com/questions/29340150/android-l-5-x-turn-on-off-mobile-data-programmatically
            md.setMobileDataEnabled(getApplicationContext(), false);
        } catch (Exception e) {
            SendMessage("Couldn't turn off Mobile Data.", MSG_FAIL);
        }

        try {
            WiFi wf = new WiFi();
            wf.setWiFiEnabled(getApplicationContext(), false);
            SendMessage(getResources().getString(R.string.Internet_connection_is_closed), MSG_PASS);
        } catch (Exception e) {
            SendMessage("Couldn't turn off WiFi.");
        }
    }

    /**
     * Делаем паузу и печатаем на экран точки, чтобы было понятно, что программа не зависла
     *
     * @param h
     * @param delay
     */
    public void fbpause(final Handler h, final int delay) {

        if (delay > 3) {
            SendMessage(".", MSG_INFO);
        }

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");

        wakeLock.acquire();

        Thread thread = new Thread() {
            public void run() {

                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                        "MyWakelockTag");

                wakeLock.acquire();

                int n = 5;

                if (delay >= wake_up_interval) {
                    n = (int) (delay - ((delay / wake_up_interval) * long_pause * 2));
                }

                for (int i = 1; i <= n; i++) {

                    if (i%3 == 0) {
                        if ( isCharging_before != isCharging) {

                            if (isCharging ) {
                                SendMessage(getResources().getString(R.string.voltage_restored));
                                if (isCharging_sms) sendSMS(sms_voltage_alert_number, getResources().getString(R.string.voltage_restored));
                            } else {
                                SendMessage(getResources().getString(R.string.no_voltage));
                                if (isCharging_sms) sendSMS(sms_voltage_alert_number, getResources().getString(R.string.no_voltage));
                            }

                            isCharging_before = isCharging;

                        }
                    }

                    Message msg = Message.obtain(); // Creates an new Message instance

                    // we make 1 sec for each n
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (getstatus() == 3) {
                        return;
                    }

                    if (i % 5 == 0 && sms_check_file) {
// checking for sms file each 5 seconds during big pause between photos
                        File sms_file = null;

//                        sms_file = new File((getApplicationContext().getFilesDir().toString() + "/sms.txt"));
                        sms_file = new File( work_dir + "/sms.txt");

                        if (sms_file.isFile()) {

                            sms = file2array(sms_file.toString());

                            sms_getdata();

                            try {
                                TimeUnit.SECONDS.sleep(3);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            for (int ind = 0; ind < sms.size(); ind++) {
                                SendMessage(sms.get(ind).toString(), MSG_INFO);
                            }

                            SendMessage("SMS:", MSG_INFO);

                            sms_file.delete();

                            sms.clear();

                            sms_update_db = true;

                            if (sms_status && sms_incoming_passwd.equals(sms_passwd)) {
                                sendSMS(sms_sender_num,
                                        "Bat: " + battery_level + "%\n" +
                                                "Upd: " + Photo_Frequency + "sec\n" +
                                                "Flash: " + Use_Flash + "\n" +
                                                "Net: " + network
                                );
                                sms_status = false;
                            }

                            if (sms_net_status && sms_incoming_passwd.equals(sms_passwd)) {
                                sendSMS(sms_sender_num,
                                        "Net: " + network + "\n" +
                                                "Ch: " + Network_Channel + "\n" +
                                                "Method: " + Network_Connection_Method + "\n" +
                                                "Mail: " + EMail_Recepient + "\n"
                                );
                                sms_net_status = false;
                            }

                            if (sms_email_status && sms_incoming_passwd.equals(sms_passwd)) {
                                sendSMS(sms_sender_num,
                                        "Mail: " + EMail_Recepient + "\n" +
                                                "FMail: " + EMail_Sender + "\n" +
                                                "FMailP: " + EMail_Sender_Password + "\n" +
                                                "SMTPHost: " + SMTP_Host + "\n" +
                                                "SMTPPort: " + SMTP_Port + "\n"
                                );
                                sms_email_status = false;
                            }


                            if (sms_media_status && sms_incoming_passwd.equals(sms_passwd)) {
                                sendSMS(sms_sender_num,
                                        "Flash: " + Use_Flash + "\n" +
                                                "JPEG: " + JPEG_Compression + "\n" +
                                                "Fc: " + make_photo_fc + "\n" +
                                                "Bc: " + make_photo_bc + "\n"
                                );
                                sms_media_status = false;
                            }

                            if (sms_incoming_passwd.equals(sms_passwd) && sms_help) {
                                sendSMS(sms_sender_num, sms_commands_list());
                                sms_help = false;
                            }

                            // drop password before next usage
                            sms_incoming_passwd = "";

                            return;

                        } else {

                        }

                    }

                    if (i % wake_up_interval == 0 && frame_delay) {

                        String cmd = null;

                        try {

                            if (Build.VERSION.SDK_INT <= 12) {
                                cmd = "logcat -v long -d -f " + work_dir + "/logfile.txt" + " Logs:* FotoBot:* *:S";
                            } else {
                                cmd = "logcat -v long -d -f " + work_dir + "/logfile.txt";
                            }
                            Runtime.getRuntime().exec(cmd);

                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("LOG_TAG", "logcat2file doesn't work");
                        }

                        try {
                            TimeUnit.SECONDS.sleep(short_pause);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // если размер лога превышает log_size, то чистим его
                        File logcat_file;
                        logcat_file = new File(work_dir + "/logfile.txt");

                        boolean fileExists = logcat_file.isFile();

                        if (fileExists) {

                            if (logcat_file.length() / 1000 > log_size) {
                                clearLog();
                            }

                        } else {
                            SendMessage("logfile.txt doesn't exist.");
                        }

                        SendMessage(".", MSG_INFO);

                        if (camera == null) {
                            Log.d(LOG_TAG, "camera == null");
                            SendMessage("..", MSG_INFO);
                            try {
                                camera = Camera.open();
                                SendMessage(".", MSG_INFO);
                                Log.d(LOG_TAG, "Camera has been successfully opened");
                            } catch (Exception e) {
                                SendMessage("...", MSG_INFO);
                                Log.d(LOG_TAG, "Problem with camera opening");
                                e.printStackTrace();
                            }
                        }

                        try {
                            camera.setPreviewDisplay(holder);
                            camera.startPreview();
                            SendMessage(".", MSG_INFO);
                            Log.d(LOG_TAG, "Preview started");
                        } catch (Exception e) {
                            e.printStackTrace();
                            SendMessage("...", MSG_INFO);
                            Log.d(LOG_TAG, "Problem with starting of preview");
                        }

                        try {
                            TimeUnit.SECONDS.sleep(long_pause);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (camera != null) {
                            Log.d(LOG_TAG, "Camera is busy");
                            try {
                                camera.stopPreview();
                                camera.release();
                                camera = null;
                                Log.d(LOG_TAG, "Camera unlocked");
                            } catch (Exception e) {
                                SendMessage("...", MSG_INFO);
                            }

                        }

                        try {
                            TimeUnit.SECONDS.sleep(long_pause);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }

        };
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        wakeLock.release();

    }

    /**
     * Печатаем сообщение на экран
     *
     * @param h   handler, который ловит сообщения
     * @param str текст сообщения
     */
    public void SendMessage(Handler h, String str) {

        Message msg = Message.obtain(); // Creates an new Message instance
        msg.obj = str; // Put the string into Message, into "obj" field.
        msg.setTarget(h); // Set the Handler
        msg.sendToTarget(); //Send the message

    }

    /**
     * Печатаем сообщение на экран
     *
     * @param str текст сообщения
     */
    public void SendMessage(String str) {

        Message msg = Message.obtain(); // Creates an new Message instance
        msg.obj = str; // Put the string into Message, into "obj" field.
        msg.setTarget(h); // Set the Handler
        msg.sendToTarget(); //Send the message

    }

    /**
     * Печатаем сообщение на экран
     *
     * @param str текст сообщения
     * @param status статус сообщения
     */
    public void SendMessage(String str, String status) {

        Message msg = Message.obtain(); // Creates an new Message instance
        msg.obj = str; // Put the string into Message, into "obj" field.
        msg_status = status;
        msg.setTarget(h); // Set the Handler
        msg.sendToTarget(); //Send the message

    }

    /**
     * Данный метод позволяет отправить письма с аттачем
     *
     * @param h
     * @param str
     */
    public void SendMail(Handler h, String str, String fc_str, String bc_video, String fc_video) {

//        final FotoBot fb = (FotoBot) getApplicationContext();

        Mail m = new Mail(getApplicationContext(), EMail_Sender, EMail_Sender_Password, SMTP_Host, SMTP_Port);

        String[] toArr = {EMail_Recepient};

        String s = "Debug-infos:";
        s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        s += "\n Device: " + android.os.Build.DEVICE;
        s += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";

        s += "\n RELEASE: " + android.os.Build.VERSION.RELEASE;
        s += "\n BRAND: " + android.os.Build.BRAND;
        s += "\n DISPLAY: " + android.os.Build.DISPLAY;
        s += "\n CPU_ABI: " + android.os.Build.CPU_ABI;
        s += "\n CPU_ABI2: " + android.os.Build.CPU_ABI2;
        s += "\n UNKNOWN: " + android.os.Build.UNKNOWN;
        s += "\n HARDWARE: " + android.os.Build.HARDWARE;
        s += "\n Build ID: " + android.os.Build.ID;
        s += "\n MANUFACTURER: " + android.os.Build.MANUFACTURER;
        s += "\n SERIAL: " + android.os.Build.SERIAL;
        s += "\n USER: " + android.os.Build.USER;
        s += "\n HOST: " + android.os.Build.HOST;

        m.setTo(toArr);
        m.setFrom(EMail_Sender);
        m.setSubject("Fotobot v" + versionName + " " + Camera_Name);

        String email_body = "";


        email_body = "Fotobot v" + versionName + "\n" +
                "---------------------------------------------\n" +
                "Camera Name" + ": " + Camera_Name + "\n" +
                getResources().getString(R.string.battery_charge) + ": " + battery_level + "%" + "\n" +
                getResources().getString(R.string.battery_temperature) + ": " + battery_temperature + "C" + "\n";

        if (Attached_Info_Detailisation.equals("Normal") || Attached_Info_Detailisation.equals("Detailed")) {

            email_body = email_body + getResources().getString(R.string.gsm) + ": " + GSM_Signal + "ASU    " + (2.0 * GSM_Signal - 113) + "dBm" + "\n" +
                    "-50 -82 dbm   -   very good" + "\n" +
                    "-83 -86 dbm   -   good" + "\n" +
                    "-87 -91 dbm   -   normal" + "\n" +
                    "-92 -95 dbm   -   bad" + "\n" +
                    "-96 -100 dbm   -  almost no signal" + "\n" +
                    "---------------------------------------------\n" +
                    "Image Index:" + Image_Index + "\n" +
                    "---------------------------------------------\n" +
                    getResources().getString(R.string.phone_memory) + ":" + "\n" +
                    "totalMemory: " + totalMemory + "\n" +
                    "usedMemory: " + usedMemory + "\n" +
                    "freeMemory: " + freeMemory + "\n" +
                    "---------------------------------------------\n" +
                    getResources().getString(R.string.email_sending_time) + ": " + email_sending_time + "\n" +
                    "---------------------------------------------\n" +
                    getResources().getString(R.string.Fotobot_settings) + ":\n" +
                    "Network_Channel: " + Network_Channel + "\n" +
                    "Network_Connection_Method: " + Network_Connection_Method + "\n" +
                    "Use_Flash: " + Use_Flash + "\n" +
                    "JPEG_Compression: " + JPEG_Compression + "\n" +
                    "Photo_Frequency: " + Photo_Frequency + "\n" +
                    "process_delay: " + process_delay + "\n" +
                    "Image_Scale: " + Image_Scale + "\n" +
                    "Image_Size: " + Image_Size + "\n" +
                    "EMail_Sender: " + EMail_Sender + "\n" +
                    "EMail_Sender_Password: *********" + "\n" +
                    "EMail_Recepient: " + EMail_Recepient + "\n" +
                    "Log_Font_Size: " + Log_Font_Size + "\n" +
                    "Config_Font_Size: " + Config_Font_Size + "\n" +
                    "Photo_Post_Processing_Method: " + Photo_Post_Processing_Method + "\n" +
                    "SMTP_Host: " + SMTP_Host + "\n" +
                    "SMTP_Port: " + SMTP_Port + "\n" +
                    "Log length: " + loglength + "\n" +
                    "FLog length: " + floglength + "\n" +
                    "wake_up_interval: " + wake_up_interval + "\n" +
                    "---------------------------------------------\n" +
                    getResources().getString(R.string.hardware_info) + ":\n" +
                    "Android: " + Build.VERSION.SDK_INT + "\n" +
                    s + "\n" +
                    "---------------------------------------------\n" +
                    "Available SMS commands: " + "\n" +
                    sms_commands_list() + "\n";
            ;
            if (Attached_Info_Detailisation.equals("Detailed")) {
                email_body = email_body + "\n\n\nActive tasks:\n" + Top + "\n\n\nBack Camera Properties:\n" + Camera_Properties +
                        "\n\n\nFront Camera Properties:\n" + fc_Camera_Properties;
            }

        }

        m.setBody(email_body);

        File attach_file;

        if (make_photo_bc && bc_image_attach) {

            attach_file = new File(str);
            boolean fileExists = attach_file.isFile();

            if (fileExists) {

            } else {
                SendMessage("ERROR: image doesn't exist for attaching to email.",MSG_FAIL);
            }
        }

        if (make_video_bc && bc_video_attach) {

            attach_file = new File(bc_video);
            boolean fileExists = attach_file.isFile();

            if (fileExists) {

            } else {
                SendMessage("ERROR: video " + bc_video + " doesn't exist for attaching to email.", MSG_FAIL);
            }
        }



        if (front_camera && make_photo_fc && fc_image_attach) {

            attach_file = new File(fc_str);
            boolean fc_fileExists = attach_file.isFile();

            if (front_camera && fc_fileExists && make_photo_fc) {

            } else {
                SendMessage("ERROR: front camera image doesn't exist for attaching to email.", MSG_FAIL);
            }
        }

        if (front_camera && make_video_fc && fc_video_attach) {

            attach_file = new File(fc_video);
            boolean fc_fileExists = attach_file.isFile();

            if (front_camera && fc_fileExists && make_photo_fc) {

            } else {
                SendMessage("ERROR: video " + fc_video + " doesn't exist for attaching to email.",MSG_FAIL);
            }
        }


        if (attach_log) {
            attach_file = new File((work_dir + "/logfile.txt"));
            boolean fileExists = attach_file.isFile();

            if (fileExists) {

            } else {
                SendMessage("ERROR: log doesn't exist for attaching to email.",MSG_FAIL);
            }

        }
        try {

            if (make_photo_bc && bc_image_attach) {
                m.addAttachment(str);
            }

            if (front_camera && make_photo_fc && fc_image_attach) {
                m.addAttachment(fc_str);
            }

            if (make_video_bc && bc_image_attach) {
                m.addAttachment(bc_video);
            }

            if (front_camera && make_video_fc && fc_image_attach) {
                m.addAttachment(fc_video);
            }

            if (attach_log) {
                m.addAttachment(work_dir + "/logfile.txt");
            }
            fbpause(h, process_delay);

            if (m.send()) {

                SendMessage(getResources().getString(R.string.foto_sent), MSG_PASS);

                SaveSettings();

            } else {
                SendMessage("ERROR: письмо не было отправлено", MSG_FAIL);
            }
        } catch (Exception e) {
            SendMessage("Could not send email",MSG_FAIL);
            Log.e("MailApp", "Could not send email", e);
        }

    }

    /**
     * На внешнюю карту можно записывать файлы?
     *
     * @return
     */
    public boolean isExternalStorageWritable() {

        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public File getFolder() {
        return getExternalStoragePublicDirectory(null);
    }

    /**
     * Инициализируем глобальные переменные значениями из SharedPreferences
     */
    public void LoadSettings() {
        /******* Create SharedPreferences *******/

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        sound_mute = pref.getBoolean("Sound_Mute", false);
        Network_Channel = pref.getString("Network_Channel", "Both");
        Network_Connection_Method = pref.getString("Network_Connection_Method", "Method 1");
        Use_WiFi = pref.getBoolean("Use_WiFi", true);         // getting boolean
        Use_Mobile_Data = pref.getBoolean("Use_Mobile_Data", true);         // getting boolean
        Use_Flash = pref.getBoolean("Use_Flash", false);
        make_photo_fc = pref.getBoolean("Use_Fc", false);
        make_photo_bc = pref.getBoolean("Use_Bc", true);
        bc_image_attach = pref.getBoolean("Bc_Image_Attach", true);
        fc_image_attach = pref.getBoolean("Fc_Image_Attach", true);
        bc_video_attach = pref.getBoolean("Bc_Video_Attach", true);
        fc_video_attach = pref.getBoolean("Fc_Video_Attach", true);
        bc_image_delete = pref.getBoolean("Bc_Image_Delete", false);
        fc_image_delete = pref.getBoolean("Fc_Image_Delete", false);
        bc_video_delete = pref.getBoolean("Bc_Video_Delete", false);
        fc_video_delete = pref.getBoolean("Fc_Video_Delete", false);
        make_video_fc = pref.getBoolean("Make_Video_Fc", false);
        make_video_bc = pref.getBoolean("Make_Video_Bc", false);
        JPEG_Compression = pref.getInt("JPEG_Compression", 50);
        use_autofocus = pref.getBoolean("Use_Autofocus", true);
        time_for_focusing = pref.getInt("Time_For_Focusing", 1);
        Camera_Name = pref.getString("Camera_Name", "default");
        Photo_Frequency = pref.getInt("Photo_Frequency", 300);
        video_recording_time = pref.getInt("Video_Recording_Time", 5);
        bc_current_video_profile = pref.getString("Bc_Current_Video_Profile", "QUALITY_LOW");
        fc_current_video_profile = pref.getString("Fc_Current_Video_Profile", "QUALITY_LOW");
        process_delay = pref.getInt("process_delay", 5);
        Image_Scale = pref.getString("Image_Scale", "1");
        Image_Size = pref.getString("Image_Size", "1024x768");
        fc_Image_Size = pref.getString("fc_Image_Size", "320x240");
        EMail_Sender = pref.getString("EMail_Sender", "fotobot@gmail.com");
        EMail_Sender_Password = pref.getString("EMail_Sender_Password", "passwd");
        EMail_Recepient = pref.getString("EMail_Recepient", "recipient@mail.ru");
        Log_Font_Size = pref.getInt("Log_Font_Size", 12);
        Config_Font_Size = pref.getInt("Config_Font_Size", 14);
        Photo_Post_Processing_Method = pref.getString("Photo_Post_Processing_Method", "Software");
        show_start_tip = pref.getBoolean("Show_Start_Tip", true);
        SMTP_Host = pref.getString("SMTP_Host", "smtp.gmail.com");
        SMTP_Port = pref.getString("SMTP_Port", "465");
        loglength = pref.getInt("Log_Length", 1024);
        log_line_number = pref.getInt("FLog_Length", 150);
        wake_up_interval = pref.getInt("Wake_Up_Interval", 60);
        network_up_delay = pref.getInt("Network_Up_Delay", 15);
        attach_log = pref.getBoolean("Attach_Log", false);
        Attached_Info_Detailisation = pref.getString("Attached_Info_Detailisation", "Normal");
        log_size = pref.getInt("Log_Size", 50);
        work_dir = pref.getString("Work_Dir", "/data/data/com.droid.app.fotobot/files");
        storage_type = pref.getString("Storage_Type", "Internal");
        network = pref.getBoolean("Network", false);
        launched_first_time = pref.getBoolean("Launched_First_Time", true);
        sms_passwd = pref.getString("SMS_Password", "passwd");
        advanced_settings = pref.getBoolean("Advanced_Settings", false);
        automatic_mode = pref.getBoolean("Automatic_Mode", false);
        delete_foto = pref.getBoolean("Delete_Foto", false);
        isCharging_sms = pref.getBoolean("IsCharging_SMS", false);
        sms_voltage_alert = pref.getBoolean("SMS_Voltage_Alert", false);
        sms_voltage_alert_number = pref.getString("SMS_Voltage_Alert_Nmuber", "+11234567890");
        bc_image_ftp_upload = pref.getBoolean("Bc_Image_FTP_Upload", true);
        fc_image_ftp_upload = pref.getBoolean("Fc_Image_FTP_Upload", true);
        bc_video_ftp_upload = pref.getBoolean("Bc_Video_FTP_Upload", true);
        fc_video_ftp_upload = pref.getBoolean("Fc_Video_FTP_Upload", true);
        FTP_server = pref.getString("FTP_Server","ftp.server.com");
        FTP_port = pref.getString("FTP_Port","21");
        FTP_username = pref.getString("FTP_Username","login");
        FTP_password = pref.getString("FTP_Password","passwd");
        Upload_Method = pref.getString("Upload_Method", "FTP");
        useMail = pref.getBoolean("Use_Mail", false);
        useFTP = pref.getBoolean("Use_FTP", false);
    }

    public void SaveSettings() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("Sound_Mute", sound_mute);
        editor.putString("Camera_Name", Camera_Name);
        editor.putInt("Photo_Frequency", Photo_Frequency);
        editor.putInt("Video_Recording_Time", video_recording_time);
        editor.putString("Bc_Current_Video_Profile", bc_current_video_profile);
        editor.putString("Fc_Current_Video_Profile", fc_current_video_profile);
        editor.putInt("Wake_Up", wake_up_interval);
        editor.putInt("process_delay", process_delay);
        editor.putInt("Config_Font_Size", Config_Font_Size);
        editor.putInt("Log_Font_Size", Log_Font_Size);
        editor.putInt("Log_Length", loglength);
        editor.putInt("FLog_Length", log_line_number);
        editor.putString("Network_Channel", Network_Channel);
        editor.putString("Network_Connection_Method", Network_Connection_Method);
        editor.putString("EMail_Sender", EMail_Sender);
        editor.putString("EMail_Sender_Password", EMail_Sender_Password);
        editor.putString("SMTP_Host", SMTP_Host);
        editor.putString("SMTP_Port", SMTP_Port);
        editor.putString("EMail_Recepient", EMail_Recepient);
        editor.putString("FTP_Server", FTP_server);
        editor.putString("FTP_Port", FTP_port);
        editor.putString("FTP_Username", FTP_username);
        editor.putString("FTP_Password", FTP_password);
        editor.putBoolean("Show_Start_Tip", show_start_tip);
        editor.putInt("Network_Up_Delay", network_up_delay);
        editor.putString("Photo_Post_Processing_Method", Photo_Post_Processing_Method);
        editor.putInt("JPEG_Compression", JPEG_Compression);
        editor.putBoolean("Use_Autofocus", use_autofocus);
        editor.putInt("Time_For_Focusing", time_for_focusing);
        editor.putString("Image_Scale", Image_Scale);
        editor.putString("Image_Size", Image_Size);
        editor.putString("fc_Image_Size", fc_Image_Size);
        editor.putBoolean("Use_Flash", Use_Flash);
        editor.putBoolean("Use_Fc", make_photo_fc);
        editor.putBoolean("Use_Bc", make_photo_bc);
        editor.putBoolean("Make_Video_Fc", make_video_fc);
        editor.putBoolean("Make_Video_Bc", make_video_bc);
        editor.putBoolean("Bc_Image_Attach", bc_image_attach);
        editor.putBoolean("Fc_Image_Attach", fc_image_attach);
        editor.putBoolean("Bc_Video_Attach", bc_video_attach);
        editor.putBoolean("Fc_Video_Attach", fc_video_attach);
        editor.putBoolean("Bc_Image_FTP_Upload", bc_image_ftp_upload);
        editor.putBoolean("Fc_Image_FTP_Upload", fc_image_ftp_upload);
        editor.putBoolean("Bc_Video_FTP_Upload", bc_video_ftp_upload);
        editor.putBoolean("Fc_Video_FTP_Upload", fc_video_ftp_upload);
        editor.putBoolean("Bc_Image_Delete", bc_image_delete);
        editor.putBoolean("Fc_Image_Delete", fc_image_delete);
        editor.putBoolean("Bc_Video_Delete", bc_video_delete);
        editor.putBoolean("Fc_Video_Delete", fc_video_delete);
        editor.putBoolean("Attach_Log", attach_log);
        editor.putString("Attached_Info_Detailisation", Attached_Info_Detailisation);
        editor.putInt("Log_Size", log_size);
        editor.putString("Work_Dir", work_dir);
        editor.putString("SMS_Password", sms_passwd);
        editor.putBoolean("Advanced_Settings", advanced_settings);
        editor.putBoolean("Automatic_Mode", automatic_mode);
        editor.putString("Storage_Type", storage_type);
        editor.putBoolean("Network", network);
        editor.putBoolean("Launched_First_Time", launched_first_time);
        editor.putBoolean("Delete_Foto", delete_foto);
        editor.putBoolean("IsCharging_SMS", isCharging_sms);
        editor.putBoolean("SMS_Voltage_Alert", sms_voltage_alert);
        editor.putString("SMS_Voltage_Alert_Number", sms_voltage_alert_number);
        editor.putString("Upload_Method", Upload_Method);
        editor.putBoolean("Use_Mail", useMail);
        editor.putBoolean("Use_FTP", useFTP);
        editor.commit();

    }

    /**
     * Уровень заряда аккумулятора. Ничего не возвращает, а инициализирует глобальную переменную battery_level.
     */
    public void batteryLevel() {

        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                //context.unregisterReceiver(this);

                int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                battery_temperature = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)) / 10.0f;
                battery_level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    battery_level = (rawlevel * 100) / scale;
                }

             //   int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
              //  isPlugged = plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
             //   if (VERSION.SDK_INT > VERSION_CODES.JELLY_BEAN) {
             //       isPlugged = isPlugged || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
             //   }

            }
        };

        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);

    }

/*    public static boolean isPlugged(Context context) {
        boolean isPlugged= false;
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        isPlugged = plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
        if (VERSION.SDK_INT > VERSION_CODES.JELLY_BEAN) {
            isPlugged = isPlugged || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
        }
        return isPlugged;
    } */


    public void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    public List<String> file2array(String filename) {

        List<String> array= new ArrayList<String>();

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null;

        try {
            Integer i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                array.add(line);
                i++;
            }
            Log.d("sms", "i=" + i);
            sms_number_of_strings = i;
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return lines.toArray(new String[lines.size()]);
        return array;
    }

    public void sms_getdata() {

        sms_sender_num = sms.get(0);
        Log.d("sms", "sms_sender_num: " + sms_sender_num);

        for (String item : sms) {

            item.replace("\n", "");
            item.trim();

            String[] sms_word = item.split("\\s+");

            Log.d("sms", "sms_word: " + sms_word);

// standard SMS, Beeline Internet SMS
            if (sms_word[0].equals("passwd")) {
                if (sms_word.length > 1) {
                    sms_incoming_passwd = sms_word[1];
                }
                Log.d("sms", "sms_passwd: " + sms_incoming_passwd);
            }

// Tele2 Internet SMS
            if (sms_word.length > 2) {
                if (sms_word[1].equals("passwd")) {
                    sms_incoming_passwd = sms_word[2];
                }
                Log.d("sms", "sms_passwd: " + sms_incoming_passwd);
            }


            if (sms_word[0].equals("status")) {
                sms_status = true;
            }

            if (sms_word[0].equals("netstatus")) {
                sms_net_status = true;
            }

            if (sms_word[0].equals("emailstatus")) {
                sms_email_status = true;
            }

            if (sms_word[0].equals("mediastatus")) {
                sms_media_status = true;
            }

            if (sms_word[0].equals("update")) {
                if (sms_word.length > 1) {
                    Photo_Frequency = Integer.parseInt(sms_word[1]);
                    Log.d("sms", "Photo_Frequency: " + Photo_Frequency);
                }
            }

            if (sms_word[0].equals("log")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("on")) {
                        attach_log = true;
                        Log.d("sms", "attach_log: " + attach_log);
                        // if (sms_word.length > 2) {
                        //     log_size = Integer.parseInt(sms_word[2]);
                        // }
                    }
                    if (sms_word[1].contains("off")) {
                        attach_log = false;
                        Log.d("sms", "attach_log: " + attach_log);
                    }
                }
            }

            if (sms_word[0].equals("network")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("on")) {
                        network = true;
                        Log.d("sms", "network: " + network);
                    }
                    if (sms_word[1].contains("off")) {
                        network = false;
                        Log.d("sms", "network: " + network);
                    }
                }
            }

            if (sms_word[0].equals("jpg")) {
                if (sms_word.length > 1) {
                    JPEG_Compression = Integer.parseInt(sms_word[1]);
                    Log.d("sms", "JPEG_Compression: " + JPEG_Compression);
                }
            }

            if (sms_word[0].equals("flash")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("on")) {
                        Use_Flash = true;
                        Log.d("sms", "Use_Flash: " + Use_Flash);
                    }
                    if (sms_word[1].contains("off")) {
                        Use_Flash = false;
                        Log.d("sms", "Use_Flash: " + Use_Flash);
                    }
                }
            }

            if (sms_word[0].equals("netch")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("wf")) {
                        Network_Channel = "Wi-Fi";
                    }
                    if (sms_word[1].contains("md")) {
                        Network_Channel = "Mobile Data";
                    }
                }
            }

            if (sms_word[0].equals("netmethod")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("1")) {
                        Network_Connection_Method = "Method 1";
                    }
                    if (sms_word[1].contains("2")) {
                        Network_Connection_Method = "Method 2";
                    }
                }
            }

            if (sms_word[0].equals("transport")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("ftp")) {
                        Upload_Method = "FTP";
                        useFTP = true;
                        useMail = false;
                    }
                    if (sms_word[1].contains("email")) {
                        Upload_Method = "E-Mail";
                        useMail = true;
                        useFTP = false;
                    }
                }
            }

            if (sms_word[0].equals("ftpserver")) {
                if (sms_word.length > 1) {
                    FTP_server = sms_word[1];
                }
            }

            if (sms_word[0].equals("ftpport")) {
                if (sms_word.length > 1) {
                    FTP_port = sms_word[1];
                }
            }

            if (sms_word[0].equals("ftpuser")) {
                if (sms_word.length > 1) {
                    FTP_username = sms_word[1];
                }
            }

            if (sms_word[0].equals("ftppasswd")) {
                if (sms_word.length > 1) {
                    FTP_password = sms_word[1];
                }
            }

            if (sms_word[0].equals("email")) {
                if (sms_word.length > 1) {
                    EMail_Recepient = sms_word[1];
                }
            }

            if (sms_word[0].equals("emails")) {
                if (sms_word.length > 1) {
                    EMail_Sender = sms_word[1];
                }
            }

            if (sms_word[0].equals("emailsp")) {
                if (sms_word.length > 1) {
                    EMail_Sender_Password = sms_word[1];
                }
            }

            if (sms_word[0].equals("smtphost")) {
                if (sms_word.length > 1) {
                    SMTP_Host = sms_word[1];
                }
            }

            if (sms_word[0].equals("smtpport")) {
                if (sms_word.length > 1) {
                    SMTP_Port = sms_word[1];
                }
            }

            if (sms_word[0].equals("fc")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("on")) {
                        make_photo_fc = true;
                    }
                    if (sms_word[1].contains("off")) {
                        make_photo_fc = false;
                    }
                }
            }

            if (sms_word[0].equals("bc")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("on")) {
                        make_photo_bc = true;
                    }
                    if (sms_word[1].contains("off")) {
                        make_photo_bc = false;
                    }
                }
            }

            if (sms_word[0].equals("attachbci")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("on")) {
                        bc_image_attach = true;
                    }
                    if (sms_word[1].contains("off")) {
                        bc_image_attach = false;
                    }
                }
            }

            if (sms_word[0].equals("attachfci")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("on")) {
                        fc_image_attach = true;
                    }
                    if (sms_word[1].contains("off")) {
                        fc_image_attach = false;
                    }
                }
            }

            if (sms_word[0].equals("attachbcv")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("on")) {
                        bc_video_attach = true;
                    }
                    if (sms_word[1].contains("off")) {
                        bc_video_attach = false;
                    }
                }
            }

            if (sms_word[0].equals("attachfcv")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("on")) {
                        fc_video_attach = true;
                    }
                    if (sms_word[1].contains("off")) {
                        fc_video_attach = false;
                    }
                }
            }

            if (sms_word[0].equals("delbci")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("on")) {
                        bc_image_delete = true;
                    }
                    if (sms_word[1].contains("off")) {
                        bc_image_delete = false;
                    }
                }
            }

            if (sms_word[0].equals("delfci")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("on")) {
                        fc_image_delete = true;
                    }
                    if (sms_word[1].contains("off")) {
                        fc_image_delete = false;
                    }
                }
            }

            if (sms_word[0].equals("delbcv")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("on")) {
                        bc_video_delete = true;
                    }
                    if (sms_word[1].contains("off")) {
                        bc_video_delete = false;
                    }
                }
            }

            if (sms_word[0].equals("delfcv")) {
                if (sms_word.length > 1) {
                    if (sms_word[1].contains("on")) {
                        fc_video_delete = true;
                    }
                    if (sms_word[1].contains("off")) {
                        fc_video_delete = false;
                    }
                }
            }

        }
        Log.d("smsss", "sms_passwd: " + sms_passwd);
        Log.d("smsss", "sms_incoming_passwd: " + sms_incoming_passwd);

        if (sms_incoming_passwd.equals(sms_passwd)) {
            SendMessage("Пароль " + sms_incoming_passwd + " верный, записываем настройки", MSG_PASS);
            SaveSettings();
        } else {
            SendMessage("Пароль " + sms_incoming_passwd + " неверный", MSG_FAIL);
            LoadSettings();
        }

    }


    public void work_dir_init() {

        File sms_file = null;

//        sms_file = new File((getApplicationContext().getFilesDir().toString() + "/sms.txt"));
        sms_file = new File(work_dir + "/sms.txt");

        if (sms_file.isFile()) {

            sms_file.delete();
            Log.d(LOG_TAG, "SMS file has been deleted");
        }

        File logfile = new File(work_dir + "/logfile.txt");
        try {
            logfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void clearLog() {
        try {
            Process process = new ProcessBuilder()
                    .command("logcat", "-c")
                    .redirectErrorStream(true)
                    .start();
        } catch (IOException e) {
        }
    }

    public String set_default_storage() {

        String path;

        path = Environment.getExternalStorageDirectory() + File.separator + "Fotobot";

        File appDir = new File(path);

        if (!appDir.exists() && !appDir.isDirectory()) {
            // create empty directory
            if (appDir.mkdirs()) {
                Log.i("CreateDir", "App dir created");
            } else {
                SendMessage("Fotobot не может создать папку " + path, MSG_FAIL);
                Log.w("CreateDir", "Unable to create app dir!");
            }
        } else {
            Log.i("CreateDir", "App dir already exists");
        }

        File file = new File(path + File.separator + "file.txt");

        try {
            file.createNewFile();
        } catch (Exception e) {
            Log.d(LOG_TAG, e.toString());
        }

        OutputStream fo;

        byte[] data1 = {1, 1, 1};
//write the bytes in file
        if (file.exists()) {
            try {
                fo = new FileOutputStream(file);
                fo.write(data1);
                fo.close();
//deleting the file
                file.delete();



                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Work_Dir", work_dir);
                editor.commit();

                return path;

            } catch (Exception e) {
                Log.d(LOG_TAG, e.toString());
            }

        }

        return null;

    }

    public String sms_commands_list() {
        String list;

        list = getResources().getString(R.string.sms_cmd_list);

        return list;
    }

    public void FTPUpload(List<String> FTP_files){
        if (files_to_ftp(FTP_files)){
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SendMessage("Файлы загружены на FTP", MSG_PASS);
            return;
        } else {
            SendMessage("<br>Сделаем еще попытку через 15сек<br>", MSG_INFO);
            try {
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (files_to_ftp(FTP_files)) {
                SendMessage("files were uploaded to ftp from 2-nd attempt");
            }
            else {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SendMessage("FTP problems", MSG_FAIL);
            }
        }

    }

    public boolean files_to_ftp(List<String> FTP_files) {
        String server;
        int port;
        String user;
        String pass;

        String FTP_folder = "";

        if (FTP_server.contains("/")) {

            FTP_folder = FTP_server.substring(FTP_server.indexOf("/", 1));
            FTP_folder = FTP_folder.substring(1);

            server = FTP_server.substring(0, FTP_server.indexOf("/", 1));
        } else {
            server = FTP_server;
        }

        port = Integer.parseInt(FTP_port);
        user = FTP_username;
        pass = FTP_password;

        SendMessage("FTP user: " + "<br>" + user, MSG_PASS);
        SendMessage("FTP folder: " + "<br>" + FTP_folder, MSG_PASS);
        SendMessage("FTP server: " + "<br>" + server, MSG_PASS);

        FTPClient ftpClient = new FTPClient();

        try {
            int reply;

            ftpClient.connect(server, port);
            System.out.println("Connected to " + server + ".");
            System.out.print(ftpClient.getReplyString());
            reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.err.println("FTP server refused connection.");
                SendMessage("FTP отказал в соединении", MSG_FAIL);
                return false;
            }
        } catch (Exception e) {
        }

        try {
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SendMessage("Проблема с авторизацией на FTP сервере, проверьте правильно ли заполнены параметры FTP в настройках.", MSG_FAIL);
                return false;
            }
        } catch (Exception e) {

        }

        // chdir

        if (FTP_folder.length() > 1) {
            try {
                ftpClient.changeWorkingDirectory(FTP_folder);

                if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                    ftpClient.disconnect();
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.err.println("FTP chdir error");
                    SendMessage("FTP проблема с переходом в директорию <br>" + FTP_folder, MSG_FAIL);
                    return false;
                }

                SendMessage("FTP переход в <br>" + FTP_folder, MSG_PASS);
                System.out.println("Successfully changed working directory.");
            } catch (Exception e) {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                SendMessage("FTP проблема с переходом в директорию <br>" + FTP_folder, MSG_FAIL);
                System.out.println("Failed to change working directory.");
                return false;
            }
        }

        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        } catch (Exception e) {
            SendMessage("FTP перевод в BINARY_FILE_TYPE", MSG_FAIL);
            System.out.println("Failed to change to BINARY_FILE_TYPE.");
            return false;
        }

        // APPROACH #1: uploads first file using an InputStream

        for (String str : FTP_files) {
            File firstLocalFile = new File(str);

            String firstRemoteFile = firstLocalFile.getName();

            try {
            InputStream inputStream = new FileInputStream(firstLocalFile);

            SendMessage("Начинаем загрузку", MSG_PASS);
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();

            if (done) {
                SendMessage("Файл " + "<br>" + str + "<br>" +  " загружен", MSG_PASS);
            }
        } catch(IOException ex){
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            SendMessage("Проблема с загрузкой файла" + "<br>" + str + "<br>" +
                    ftpClient.getReplyCode() + "\n" +
                    ftpClient.getReplyString() + "\n" +
                    ex.getMessage() + "<br>" +
                    "Проверьте правильно ли заполнены параметры FTP в настройках.", MSG_FAIL);
            ex.printStackTrace();
        }

    }

            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                    SendMessage("FTP сессия закрыта", MSG_PASS);
                    fbpause(h, 3);
                    return true;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                SendMessage("FTP сессия закрыта", MSG_FAIL);
            }

            return false;

    }

    public void print_status(){

        if (network) {
            SendMessage("Send photos to Internet: Yes", MSG_INFO);

            if (Network_Connection_Method.equals("Method1")) {
                SendMessage("Connect once at the beginning", MSG_INFO);
            } else {
                SendMessage("Disconnect after photo uploading", MSG_INFO);

            }

        } else {
            SendMessage("Send photos to Internet: No", MSG_INFO);
        }

        SendMessage("Режим работы", MSG_INFO);
    }

}

/*
Загрузить в сеть новую ветку
git push -u origin branch

Восстановление ветки из сетевого репозитария
git fetch --all
git reset --hard origin/master
git pull origin master

git fetch downloads the latest from remote without trying to merge or rebase anything.

Then the git reset resets the master branch to what you just fetched. The --hard option changes all the files in your working tree to match the files in origin/master

Все ветки на локальном сервере
git branch

Посмотреть все ветки на удаленном сервере
git remote show origin
git branch -r

История коммитов
git log
git log --pretty=format:"%h %s" --graph

C:\Users\Andrey\AppData\Local\Android\sdk2\platform-tools\adb.exe shell
/home/voran/Android/Sdk/platform-tools/adb shell
$su
mount -o rw,remount rootfs /
chmod 777 /mnt/sdcard
exit
*/