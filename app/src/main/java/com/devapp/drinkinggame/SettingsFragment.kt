package com.devapp.drinkinggame

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceFragment
import android.widget.Toast


class SettingsFragment : PreferenceFragment() {

    companion object {
        const val PREFERENCE_SOUND_FX = "switch_sound"
        const val PREFERENCE_NIGHT_MODE = "switch_night"
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
                Toast.makeText(activity, "Not yet implemented", Toast.LENGTH_SHORT).show()
            }

            if(PREFERENCE_NIGHT_MODE == key){
                val nightPreference = findPreference(key)
                if(prefs.getBoolean(key,false)){
                    nightPreference.summary = "Enabled"
                }else{
                    nightPreference.summary = "Disabled"
                }
                Toast.makeText(activity, "Not yet implemented", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)

        val nightPreference = findPreference(PREFERENCE_NIGHT_MODE)

        val sharePreferenceNightMode =  preferenceScreen.sharedPreferences.getBoolean(PREFERENCE_NIGHT_MODE, false)
        if(sharePreferenceNightMode){
            nightPreference.summary = "Enabled"
        }else{
            nightPreference.summary = "Disabled"
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