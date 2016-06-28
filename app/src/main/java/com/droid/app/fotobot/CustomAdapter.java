package com.droid.app.fotobot;

/**
 * Created by voran on 6/28/16.
 */
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/***** Adapter class extends with ArrayAdapter ******/
public class CustomAdapter extends ArrayAdapter<String>{

    private Activity activity;
    private ArrayList data;
    public Resources res;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/

    public CustomAdapter(Context context, int textViewResourceId,
                         ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
    }

/*
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            if (position % 2 == 0) { // we're on an even row
                view.setBackgroundColor(5);
            } else {
                view.setBackgroundColor(1);
            }
            return view;
        }
*/
}
