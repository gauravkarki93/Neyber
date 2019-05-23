package com.clubin.neyber;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;


public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        final ActionBar actionBar;
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();
    }


}
