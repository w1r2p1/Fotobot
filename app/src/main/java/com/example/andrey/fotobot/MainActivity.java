package com.example.andrey.fotobot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private DropboxAPI<AndroidAuthSession> dropbox;
    private final static String FILE_DIR = "/MySampleFolder/";
    private final static String DROPBOX_NAME = "YetAnotherUploader";
    private final static String ACCESS_KEY = "pbzt6tzpran9oil";
    private final static String ACCESS_SECRET = "pk53kqbm581o64z";

    int n;
    FotoBot fb;
    String log;

    //a variable to store a reference to the Image View at the main.xml file
    private ImageView iv_image;
    //a variable to store a reference to the Surface View at the main.xml file
    private SurfaceView sv;

    //a bitmap to display the captured image
    private Bitmap bmp;

    //Camera variables
    //a surface holder
    private SurfaceHolder sHolder;
    //a variable to control the camera
    private Camera mCamera;
    //the camera parameters
    private Camera.Parameters parameters;

    Handler.Callback hc = new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String reportDate = dateformat.format(today);

            String message = (String) msg.obj; //Extract the string from the Message
            log = reportDate + ":    " + message + "\n" + log;

            tvInfo.setTextSize(9);
            tvInfo.setTypeface(Typeface.SANS_SERIF);
            tvInfo.setTextColor(Color.rgb(5,5,5));

            tvInfo.setText(log);

            final FotoBot fb = (FotoBot) getApplicationContext();
            Log.d(LOG_TAG, "Handler.Callback(): fb.getstatus()" + fb.getstatus());
            n = msg.what;
            if (msg.what == STATUS_STOPPED) btnStart.setText("Play");

            if (fb.getstatus() == 3) {
                btnStart = (Button) findViewById(R.id.play);
                btnStop = (Button) findViewById(R.id.stop);
                //     Log.d(LOG_TAG, "Handler.Callback() if: fb.getstatus()" + fb.getstatus());
                btnStart.setText("Start");
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnConfig.setEnabled(true);
                btnStart.postInvalidate();
                btnStop.postInvalidate();
            }

            return false;
        }
    };

    final int STATUS_STARTED = 111;
    final int STATUS_WORKING = 222;
    final int STATUS_STOPPED = 333;
    boolean STOP_FOTOBOT = false;

    final String LOG_TAG = "Logs";
    Button btnStart;
    Button btnStop;
    Button btnConfig;
    Handler h;
    TextView tvInfo;
    TextView text;
    Intent intent;
    String str1 = "Fotobot str to file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    //    super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FotoBot fb = (FotoBot) getApplicationContext();

        btnStart = (Button) findViewById(R.id.play);
        btnConfig = (Button) findViewById(R.id.config);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        text = (TextView) findViewById(R.id.textView);

        //get the Image View at the main.xml file
        iv_image = (ImageView) findViewById(R.id.imageView);

        //get the Surface View at the main.xml file
        sv = (SurfaceView) findViewById(R.id.surfaceView);

        //Get a surface
        sHolder = sv.getHolder();

        //add the callback interface methods defined below as the Surface View callbacks
        sHolder.addCallback(this);

        //tells Android that this surface will have its data constantly replaced
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);



        intent = new Intent(MainActivity.this, Status.class);
        log = "\n\n\n\n\nФотобот приветствует Вас!";
        //    tvInfo.setTextSize(20);
        //    tvInfo.setTypeface(Typeface.SANS_SERIF);
        //    tvInfo.setTextColor(Color.BLUE);
        tvInfo.setText(log);

        h = new Handler(hc);

        if (fb.isExternalStorageWritable()) {
            fb.SendMessage(h, "Внешняя SD карта доступна для чтения и записи");
        } else {
            fb.SendMessage(h, "Внешняя SD карта не доступна для записи");
        }

        fb.SendMessage(h, "getExternalStorageDirectory()" + Environment.getExternalStorageDirectory().toString());
      //  fb.SendMessage(h, "EXTERNAL_STORAGE" + System.getenv("EXTERNAL_STORAGE"));
      //  fb.SendMessage(h, "SECONDARY_STORAGE" + System.getenv("SECONDARY_STORAGE"));
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MainActivity: onDestroy");
    }

    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "MainActivity: onPause");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "MainActivity: onRestart");
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "MainActivity: onRestoreInstanceState");
    }

    protected void onResume() {
        final FotoBot fb = (FotoBot) getApplicationContext();
        Log.d(LOG_TAG, "MainActivity: onResume");
        Log.d(LOG_TAG, "MainActivity: fb.getstatus()" + fb.getstatus());
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        super.onResume();

        h = new Handler(hc);
        btnStart = (Button) findViewById(R.id.play);
        btnStop = (Button) findViewById(R.id.stop);

        if (fb.getstatus() == 1) {
            btnStart.setText("Start");
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
        }

        if (fb.getstatus() == 2) {
            btnStart.setText("Start");
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
        }

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "MainActivity: onSaveInstanceState");
    }

    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "MainActivity: onStart");
    }

    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "MainActivity: onStop");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user clicks the Status button
     */
    public void showStatus(View view) {
        Intent intent = new Intent(this, Status.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Settings button
     */
    public void showSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void startFotobot(View v) {
        final FotoBot fb = (FotoBot) getApplicationContext();


        h = new Handler(hc);


        switch (v.getId()) {
            case R.id.play:
                btnStart.setEnabled(false);
                btnStart.setText("Start");
                btnStop = (Button) findViewById(R.id.stop);
                btnStop.setEnabled(true);
                btnConfig.setEnabled(false);

                Thread t = new Thread(new Runnable() {
                    public void run() {

                        fb.SendMessage(h, "Фотобот начинает свою работу");

                        for (int i = 1; i <= 1000; i++) {

                            fb.MakeInternetConnection(getApplicationContext(), h);

                            if (fb.getstatus() == 3) {
                                fb.SendMessage(h, "Фотобот остановлен");
                                return;
                            }

                            fb.fbpause(h, 5);
                            fb.SendMessage(h, "сделано фото" + Integer.toString(i));

                            try {
                                FileOutputStream fileout = openFileOutput("/sdcard0/mytextfile.txt", MODE_PRIVATE | MODE_APPEND);
                                OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                                outputWriter.write("Закачан файл:" + Integer.toString(i) + "\n");
                                outputWriter.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
// Mail
/*                            Mail m = new Mail("fotobotmail@gmail.com", "fotobotmailpasswd");

                            String[] toArr = {"digibolt@mail.ru"};
                            m.setTo(toArr);
                            m.setFrom("voran.mail@gmail.com");
                            m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
                            m.setBody("Email body.");

                            try {
                                m.addAttachment("/storage/sdcard0/CAM01165.jpg");

                                if(m.send()) {
                                  //  Toast.makeText(MailApp.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                                    fb.SendMessage(h, "Email was sent successfully.");
                                } else {
                                   // Toast.makeText(MailApp.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                                    fb.SendMessage(h, "Email was not sent.");
                                }
                            } catch(Exception e) {
                                //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                                Log.e("MailApp", "Could not send email", e);
                            }
*/

fb.SendMail(h, "/storage/sdcard0/CAM01165.jpg");










                        fb.CloseInternetConnection(getApplicationContext(), h);

                            fb.fbpause(h, 15);

                        }
                    }

                }
                );

                t.start();
                fb.setstatus(2);
                Log.d(LOG_TAG, "startFotobot: fb.getstatus()" + fb.getstatus());
                break;

            default:
                break;
        }
    }

    public void stopFotobot(View v) {
        h = new Handler(hc);
        final FotoBot fb = (FotoBot) getApplicationContext();
        fb.CloseInternetConnection(getApplicationContext(), h);

        Log.d(LOG_TAG, "stopFotobot: fb.getstatus()" + fb.getstatus());
        fb.setstatus(3);
        Log.d(LOG_TAG, "stopFotobot: STOP_FOTOBOT" + STOP_FOTOBOT);

        btnStart.setText("Play");
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnConfig.setEnabled(true);

    }

    public void fbpause(int delay) {
        String message;

        for (int i = 1; i <= delay; i++) {
            Message msg = Message.obtain(); // Creates an new Message instance
            message = ". . . . . " + Integer.toString(i);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            msg.obj = message; // Put the string into Message, into "obj" field.
            msg.setTarget(h); // Set the Handler
            //   msg.sendToTarget(); //Send the message
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {
        //get camera parameters
        parameters = mCamera.getParameters();

        //set camera parameters
        mCamera.setParameters(parameters);
        mCamera.startPreview();

        //sets what code should be executed after the picture is taken
        Camera.PictureCallback mCall = new Camera.PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] data, Camera camera)
            {
                //decode the data obtained by the camera into a Bitmap
                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                //set the iv_image
                // iv_image.setImageBitmap(bmp);

                String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                Log.d(LOG_TAG, "fullPath: " + fullPath);
                try {
                    File dir = new File(fullPath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    OutputStream fOut = null;
                    File file = new File(fullPath, "takepicturewithoutpreview.jpg");
                    file.createNewFile();
                    fOut = new FileOutputStream(file);

                    Bitmap bmp_m = bmp.createScaledBitmap(bmp, 640,
                            480, false);
// 100 means no compression, the lower you go, the stronger the compression
                    bmp_m.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
                    fOut.flush();
                    fOut.close();



                } catch (Exception e) {
                    Log.e("saveToExternalStorage()", e.getMessage());
                }


            }


        };

        mCamera.takePicture(null, null, mCall);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // The Surface has been created, acquire the camera and tell it where
        // to draw the preview.
        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(holder);

        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //stop the preview
        mCamera.stopPreview();
        //release the camera
        mCamera.release();
        //unbind the camera from this object
        mCamera = null;
    }
}
