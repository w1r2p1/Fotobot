package com.droid.app.fotobot;

/**
 * Created by voran on 6/29/16.
 */
import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {

    public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);

    }



}