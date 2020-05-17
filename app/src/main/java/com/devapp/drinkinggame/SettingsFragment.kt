package com.devapp.drinkinggame

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceFragment
import android.widget.Toast


class SettingsFragment : PreferenceFragment() {

    companion object {
        const val PREFERENCE_SOUND_FX = "prefSound"
        const val PREFERENCE_DARK_MODE = "prefDark"
    }

    private lateinit var preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings);

        this.preferenceChangeListener  = OnSharedPreferenceChangeListener { prefs, key ->
            if(PREFERENCE_SOUND_FX == key){
                val soundPreference = findPreference(key)
                if(prefs.getBoolean(key,false)){
                    soundPreference.summary = "Enabled"
                }else{
                    soundPreference.summary = "Disabled"
                }
            }

            if(PREFERENCE_DARK_MODE == key){
                val darkPreference = findPreference(key)
                if(prefs.getBoolean(key,false)){
                    darkPreference.summary = "Enabled"
                    Toast.makeText(activity, "Not yet implemented", Toast.LENGTH_SHORT).show()
                }else{
                    darkPreference.summary = "Disabled"
                    Toast.makeText(activity, "Not yet implemented", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)

        val darkPreference = findPreference(PREFERENCE_DARK_MODE)

        val sharePreferenceDarkMode =  preferenceScreen.sharedPreferences.getBoolean(PREFERENCE_DARK_MODE, false)
        if(sharePreferenceDarkMode){
            darkPreference.summary = "Enabled"
        }else{
            darkPreference.summary = "Disabled"
        }

        val soundPreference = findPreference(PREFERENCE_SOUND_FX)
        val sharePreferenceSound =  preferenceScreen.sharedPreferences.getBoolean(PREFERENCE_SOUND_FX, false)

        if(sharePreferenceSound){
            soundPreference.summary = "Enabled"
        }else{
            soundPreference.summary = "Disabled"
        }
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)

    }




}