package com.devapp.drinkinggame

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager

class SettingsActivity:  AppCompatActivity() {

    companion object {
        private const val PREFERENCE_FEATURE_DARK_MODE = "prefDark"
    }

    private var isDarkModeFeatureEnabled = false

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title){
            "Save"-> Toast.makeText(this, "to be implemented", Toast.LENGTH_SHORT).show()
            else -> {
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPreferences()

        if(isDarkModeFeatureEnabled){
            setTheme(R.style.DarkTheme)
        }else{
            setTheme(R.style.AppTheme)
        }

        setContentView(R.layout.activity_settings)

        initToolbar()
        addCloseArrowIconInToolbar()

        replaceTitleFromToolbar()

        openSettingsFragment()

    }

    private fun replaceTitleFromToolbar(){
        this.title = getString(R.string.action_settings)
    }

    private fun initToolbar(){
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        if(isDarkModeFeatureEnabled){
            toolbar.setBackgroundColor(Color.parseColor("#000000"))
        }else{
            toolbar.setBackgroundColor(Color.parseColor("#3700B3"))
        }
        setSupportActionBar(toolbar)
    }

    private fun addCloseArrowIconInToolbar(){
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
    }

    private fun  openSettingsFragment(){
//        val fragment: SettingsFragment? = supportFragmentManager.findFragmentByTag("SETTINGS_FRAGMENT") as SettingsFragment?
//
//        if(fragment == null) {
//            var fragment = SettingsFragment()
//            var fragmentManager = supportFragmentManager
//            var transaction = fragmentManager.beginTransaction()
//
//
//            transaction.addToBackStack("SETTINGS_FRAGMENT")
//            transaction.add(R.id.fragment_setting_container, fragment, "SETTINGS_FRAGMENT").commit()
//        }

        fragmentManager.beginTransaction().replace(R.id.fragment_setting_container, SettingsFragment()).commit()

    }

    private fun initPreferences(){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        isDarkModeFeatureEnabled = sharedPreferences.getBoolean(PREFERENCE_FEATURE_DARK_MODE, false)
    }
}