package com.devapp.drinkinggame

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class RulesActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_rules, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title){
           "Done"-> Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
            else -> finish()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        displayToolbar()
        addCloseIconInToolbar()
        removeTitleFromToolbar()
    }

    private fun displayToolbar(){
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun addCloseIconInToolbar(){
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_close)

    }

    private fun removeTitleFromToolbar(){
        this.title = ""
    }

    private fun openMainActivity(){
        finish()
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
    }
}
