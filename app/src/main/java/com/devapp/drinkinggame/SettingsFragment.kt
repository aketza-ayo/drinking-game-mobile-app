package com.devapp.drinkinggame

import android.os.Bundle
import android.preference.PreferenceFragment

class SettingsFragment : PreferenceFragment() {

//    override fun onCreatePreferences(savedInstanceState: Bundle? ,rootKey:  String?) {
//        // Load the preferences from an XML resource
//        setPreferencesFromResource(R.xml.settings, rootKey);
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings);
    }


//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        super.onCreateView(inflater, container, savedInstanceState);
//        var view = inflater.inflate(R.layout.activity_settings, container, false)
//
//        return view
//    }
}