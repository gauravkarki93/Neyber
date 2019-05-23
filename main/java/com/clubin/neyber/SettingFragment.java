package com.clubin.neyber;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by GAURAV on 21-07-2015.
 */
public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference);
    }
}
