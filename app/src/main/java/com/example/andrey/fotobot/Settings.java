package com.example.andrey.fotobot;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class Settings extends TabActivity {

    private TabHost mTabHost;

    private void setupTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
    }


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// construct the tabhost
        setContentView(R.layout.main);

        setupTabHost();

// Главная
        View view = LayoutInflater.from(mTabHost.getContext()).inflate(R.layout.tabs_bg, null);
        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        tv.setText("Главная");
        Intent intent = new Intent(this, Tab_Main_Activity.class);
        TabHost.TabSpec spec = mTabHost.newTabSpec("Tab1").setIndicator(view).setContent(intent);
        mTabHost.addTab(spec);
// Сеть
        View view2 = LayoutInflater.from(mTabHost.getContext()).inflate(R.layout.tabs_bg, null);
        TextView tv2 = (TextView) view2.findViewById(R.id.tabsText);
        tv2.setText("Сеть");
        intent = new Intent(this, Tab_Network_Activity.class);
        TabHost.TabSpec spec2 = mTabHost.newTabSpec("Tab2").setIndicator(view2).setContent(intent);
        mTabHost.addTab(spec2);
//Фото
        View view3 = LayoutInflater.from(mTabHost.getContext()).inflate(R.layout.tabs_bg, null);
        TextView tv3 = (TextView) view3.findViewById(R.id.tabsText);
        tv3.setText("Фото");
        intent = new Intent(this, Tab_Foto_Activity.class);
        TabHost.TabSpec spec3 = mTabHost.newTabSpec("Tab3").setIndicator(view3).setContent(intent);
        mTabHost.addTab(spec3);

    }

}
