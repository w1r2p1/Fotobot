package com.example.andrey.fotobot;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class Settings extends TabActivity {

    private TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

      tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;
        View view;
        tabHost.getTabWidget().setDividerDrawable(R.drawable.divider);
        view = createTabView(tabHost.getContext(), "Tab 1");
        intent = new Intent().setClass(this, SomeClass1.class);
        spec = tabHost.newTabSpec("tab1").setIndicator(view).setContent(intent);
        tabHost.addTab(spec);
  /*      intent = new Intent().setClass(this, SomeClass2.class);
        view = createTabView(tabHost.getContext(), "Tab 2");
        spec = tabHost.newTabSpec("tab2").setIndicator(view).setContent(intent);
        tabHost.addTab(spec);
        intent = new Intent().setClass(this, SomeClass3.class);
        view = createTabView(tabHost.getContext(), "Tab 3");
        spec = tabHost.newTabSpec("tab3").setIndicator(view).setContent(intent);
        tabHost.addTab(spec);
        */
    }



    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_bg, null);
        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        tv.setText(text);
        return view;
    }

}







/*public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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


    public void showMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
*/