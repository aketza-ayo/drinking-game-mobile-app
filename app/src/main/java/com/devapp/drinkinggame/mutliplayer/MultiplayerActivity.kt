package com.devapp.drinkinggame.mutliplayer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.devapp.drinkinggame.R


class MultiplayerActivity : AppCompatActivity(){

    private var isDarkModeFeatureEnabled = false
    private var gameSession = ""

    private lateinit var buttonShare: Button
    private lateinit var buttonGenerate: Button

    companion object {
        private const val PREFERENCE_FEATURE_DARK_MODE = "prefDark"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title){
            "foo"-> "to be added"
            else -> {
                finish()
            }

        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPreferences()

        if (isDarkModeFeatureEnabled) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }
        setContentView(R.layout.activity_multiplayer)

        initToolbar()
        addCloseArrowIconInToolbar()
        replaceTitleFromToolbar()

        initButtons()
    }

    private fun initButtons(){
        buttonGenerate = this.findViewById<Button>(R.id.buttonGenerate)
        buttonGenerate.isEnabled = true
        buttonGenerate.setOnClickListener {
            generateGameSession()
        }

        buttonShare = this.findViewById<Button>(R.id.buttonShare)
        buttonShare.setOnClickListener{
            shareGameSession()
        }
        buttonShare.isEnabled = false
    }

    private fun shareGameSession(){
        val whatsAppIntent = Intent(Intent.ACTION_SEND)
        whatsAppIntent.type = "text/plain"
        whatsAppIntent.setPackage("com.whatsapp")
        whatsAppIntent.putExtra(Intent.EXTRA_TEXT, "Join game $gameSession")
        try {
            this.startActivity(whatsAppIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, "WhatsApp have not been installed.", Toast.LENGTH_SHORT).show()
        }
    }

    public fun generateGameSession(){
        gameSession = "TRYUS23467-12345-AABB"
        Toast.makeText(this, gameSession, Toast.LENGTH_LONG).show()

        val textGenerate = this.findViewById<TextView>(R.id.textGenerate)
        textGenerate.text = gameSession

        buttonShare.isEnabled = true
    }

    private fun replaceTitleFromToolbar(){
        this.title = getString(R.string.action_multiplayer)
    }

    private fun addCloseArrowIconInToolbar(){
        val toolbar = this.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)

    }

    private fun initToolbar(){
        val toolbar = this.findViewById<Toolbar>(R.id.toolbar)

        if(isDarkModeFeatureEnabled){
            toolbar.setBackgroundColor(Color.parseColor("#000000"))
        }else{
            toolbar.setBackgroundColor(Color.parseColor("#3700B3"))
        }

        setSupportActionBar(toolbar)
    }

    private fun initPreferences(){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        isDarkModeFeatureEnabled = sharedPreferences.getBoolean(MultiplayerActivity.PREFERENCE_FEATURE_DARK_MODE, false)
    }





}