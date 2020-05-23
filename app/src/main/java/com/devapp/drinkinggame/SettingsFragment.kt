package com.devapp.drinkinggame

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager


class SettingsFragment : PreferenceFragment() {

    companion object {
        const val PREFERENCE_FEATURE_SOUND_FX = "prefSound"
        const val PREFERENCE_FEATURE_DARK_MODE = "prefDark"
    }

    private var isDarkModeFeatureEnabled = false

    private lateinit var preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initPreferences()

        val view: View? = super.onCreateView(inflater, container, savedInstanceState)
        if(isDarkModeFeatureEnabled){
            view!!.setBackgroundColor(Color.parseColor("#212121"))

        }else{
            view!!.setBackgroundColor(resources.getColor(android.R.color.white))

        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings);

//        initPreferences()


        this.preferenceChangeListener  = OnSharedPreferenceChangeListener { prefs, key ->


            if(PREFERENCE_FEATURE_SOUND_FX == key){
                val soundPreference = findPreference(key)
                if(prefs.getBoolean(key,false)){
                    soundPreference.summary = "Enabled"
                }else{
                    soundPreference.summary = "Disabled"
                }
            }

            if(PREFERENCE_FEATURE_DARK_MODE == key){
                val darkPreference = findPreference(key)
                if(prefs.getBoolean(key,false)){
                    darkPreference.summary = "Enabled"
                }else{
                    darkPreference.summary = "Disabled"
                }

                forceReStartToApplyStyle()
            }
        }
    }

    private fun forceReStartToApplyStyle(){
        val intent = Intent(activity, SettingsActivity::class.java)
        activity.finish()
        activity.startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener)

        val darkPreference = findPreference(PREFERENCE_FEATURE_DARK_MODE)

        val sharePreferenceDarkMode =  preferenceScreen.sharedPreferences.getBoolean(PREFERENCE_FEATURE_DARK_MODE, false)
        if(sharePreferenceDarkMode){
            darkPreference.summary = "Enabled"
        }else{
            darkPreference.summary = "Disabled"
        }

        val soundPreference = findPreference(PREFERENCE_FEATURE_SOUND_FX)
        val sharePreferenceSound =  preferenceScreen.sharedPreferences.getBoolean(PREFERENCE_FEATURE_SOUND_FX, false)

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
    private fun initPreferences(){

//        val sharedPreferences = activity.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)

        isDarkModeFeatureEnabled = sharedPreferences.getBoolean(PREFERENCE_FEATURE_DARK_MODE, false)
    }

}