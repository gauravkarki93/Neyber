package com.clubin.neyber;

import android.app.Activity;
import android.os.Bundle;


public class BaseActivity extends Activity {
    public static boolean appInFront;

    @Override
    protected void onResume() {
        super.onResume();
        appInFront = true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        appInFront = false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
