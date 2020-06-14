package com.devapp.drinkinggame

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.preference.PreferenceManager


class SettingsFragment : PreferenceFragment() {

    companion object {
        const val PREFERENCE_FEATURE_SOUND_FX = "prefSound"
        const val PREFERENCE_FEATURE_DARK_MODE = "prefDark"
    }

    private var revealEasterEgg: Int = 5
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

        addPreferencesFromResource(R.xml.settings)

//        initPreferences()

        findPreference("appDeveloper").onPreferenceClickListener =
            android.preference.Preference.OnPreferenceClickListener { preference: android.preference.Preference? ->

                if(revealEasterEgg == 5){
                    Toast.makeText(activity, resources.getString(R.string.keep_going),Toast.LENGTH_SHORT).show()
                }

                if(revealEasterEgg == 3){
                    Toast.makeText(activity,resources.getString(R.string.almost_there),Toast.LENGTH_SHORT).show()
                }

                if(revealEasterEgg <= 0) {
                    revealEasterEgg = 5
//                    val intent= activity.packageManager.getLaunchIntentForPackage("com.devapp.drinkinggame.AndroidLauncher")
                    val intent = Intent(activity, AndroidLauncher::class.java)
                    if (intent != null && isDarkModeFeatureEnabled) {
                        startActivity(intent)
                    } else {
                        triggerMurcielagoAnimation()
                    }
                }else{
                    revealEasterEgg--
                }

                true
            }

        this.preferenceChangeListener  = OnSharedPreferenceChangeListener { prefs, key ->


            if(PREFERENCE_FEATURE_SOUND_FX == key){
                val soundPreference = findPreference(key)
                if(prefs.getBoolean(key,false)){
                    soundPreference.summary = resources.getString(R.string.enabled)
                }else{
                    soundPreference.summary = resources.getString(R.string.disabled)
                }
            }

            if(PREFERENCE_FEATURE_DARK_MODE == key){
                val darkPreference = findPreference(key)
                if(prefs.getBoolean(key,false)){
                    darkPreference.summary = resources.getString(R.string.enabled)
                }else{
                    darkPreference.summary = resources.getString(R.string.disabled)
                }

                forceReStartToApplyStyle()
            }
        }
    }

    private fun triggerMurcielagoAnimation(){
        Log.d(Constants.APP_NAME, "triggerMurcielagoAnimation() -> showing animation")

        var dialog = MurcielagoDialog.getInstace()
        dialog?.show(fragmentManager,"Murcielago's Easter Egg")
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
            darkPreference.summary = resources.getString(R.string.enabled)
        }else{
            darkPreference.summary = resources.getString(R.string.disabled)
        }

        val soundPreference = findPreference(PREFERENCE_FEATURE_SOUND_FX)
        val sharePreferenceSound =  preferenceScreen.sharedPreferences.getBoolean(PREFERENCE_FEATURE_SOUND_FX, false)

        if(sharePreferenceSound){
            soundPreference.summary = resources.getString(R.string.enabled)
        }else{
            soundPreference.summary = resources.getString(R.string.disabled)
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