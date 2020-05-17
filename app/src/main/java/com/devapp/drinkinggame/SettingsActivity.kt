package com.devapp.drinkinggame

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SettingsActivity:  AppCompatActivity() {

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title){
            "Save"-> Toast.makeText(this, "to be implemented", Toast.LENGTH_SHORT).show()
            else -> {
                finish()
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initToolbar()
        addCloseArrowIconInToolbar()
        openSettingsFragment()

        replaceTitleFromToolbar()

        openSettingsFragment()

    }

    private fun replaceTitleFromToolbar(){
        this.title = getString(R.string.action_settings)
    }

    private fun initToolbar(){
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
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
}