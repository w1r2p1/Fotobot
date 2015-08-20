package com.example.andrey.fotobot;

import android.app.Application;

/**
 * Created by admin on 8/19/2015.
 */
public class FotoBot extends Application {
    public int Update;
    public int status=1;

    public int getstatus() {

        return status;
    }

    public void setstatus(int fb_status) {

        status = fb_status;
    }
    public void FotoBot () {
        Update = 300;
    }

    public void Init (){

    }

    public void WriteData () {

    }
}
