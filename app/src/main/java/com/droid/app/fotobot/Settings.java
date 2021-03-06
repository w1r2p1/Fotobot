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

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TextView;

public class Settings extends TabActivity {
    final String LOG_TAG = "Logs";
    private TabHost mTabHost;

    private void setupTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FotoBot fb = (FotoBot) getApplicationContext();
// construct the tabhost
        setContentView(R.layout.main);

        setupTabHost();

// Главная
        View view = LayoutInflater.from(mTabHost.getContext()).inflate(R.layout.tabs_bg, null);
        TextView tv = (TextView) view.findViewById(R.id.tabsText);

        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setTextColor(Color.WHITE);

        tv.setText(getResources().getString(R.string.settings_tab_1));

        tv.setPadding(5, 15, 5, 15);

        fb.menuheight = (int) pxFromDp(getApplicationContext(), getTextViewHeight(tv));

        Log.d(LOG_TAG, "menuheight: " + fb.menuheight);
        Intent intent = new Intent(this, Tab_Main_Activity.class);
        TabHost.TabSpec spec = mTabHost.newTabSpec("Tab1").setIndicator(view).setContent(intent);

        mTabHost.addTab(spec);
// Сеть
        View view2 = LayoutInflater.from(mTabHost.getContext()).inflate(R.layout.tabs_bg, null);
        TextView tv2 = (TextView) view2.findViewById(R.id.tabsText);

        tv2.setTypeface(Typeface.DEFAULT_BOLD);
        tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv2.setTextColor(Color.WHITE);

        tv2.setText(getResources().getString(R.string.settings_tab_2));
        intent = new Intent(this, Tab_Network_Activity.class);
        TabHost.TabSpec spec2 = mTabHost.newTabSpec("Tab2").setIndicator(view2).setContent(intent);
        mTabHost.addTab(spec2);
//Фото
        View view3 = LayoutInflater.from(mTabHost.getContext()).inflate(R.layout.tabs_bg, null);
        TextView tv3 = (TextView) view3.findViewById(R.id.tabsText);

        tv3.setTypeface(Typeface.DEFAULT_BOLD);
        tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv3.setTextColor(Color.WHITE);

        tv3.setText(getResources().getString(R.string.settings_tab_3));
        intent = new Intent(this, Tab_Foto_Activity.class);
        TabHost.TabSpec spec3 = mTabHost.newTabSpec("Tab3").setIndicator(view3).setContent(intent);
        mTabHost.addTab(spec3);

//Видео
        View view4 = LayoutInflater.from(mTabHost.getContext()).inflate(R.layout.tabs_bg, null);
        TextView tv4 = (TextView) view4.findViewById(R.id.tabsText);

        tv4.setTypeface(Typeface.DEFAULT_BOLD);
        tv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv4.setTextColor(Color.WHITE);

        tv4.setText(getResources().getString(R.string.settings_tab_4));
        intent = new Intent(this, Tab_Video_Activity.class);
        TabHost.TabSpec spec4 = mTabHost.newTabSpec("Tab4").setIndicator(view4).setContent(intent);
        mTabHost.addTab(spec4);

    }

    /**
     * Get the TextView height before the TextView will render
     * @param textView the TextView to measure
     * @return the height of the textView
     */
    public static int getTextViewHeight(TextView textView) {
        WindowManager wm =
                (WindowManager) textView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int deviceWidth;

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Point size = new Point();
            display.getSize(size);
            deviceWidth = size.x;
        } else {
            deviceWidth = display.getWidth();
        }

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }
    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
