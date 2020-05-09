package com.devapp.drinkinggame

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var database = DatabaseHelper.getInstance(this)

    private var mutableDeck = CardsData().getAllCards()
    private lateinit var currentCard: Card
    private lateinit var switch: Switch


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_rules_management-> openRulesActivity()
            else ->  super.onOptionsItemSelected(item)
        }

        return true
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        var item = menu.findItem(R.id.main_switch)
        val view: View = MenuItemCompat.getActionView(item)
        switch = view.findViewById<View>(R.id.switchRule) as Switch

        switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(switch.isChecked){
                textTip.text = showCustomRules()
                Toast.makeText(applicationContext, "Custom Rules", Toast.LENGTH_SHORT).show()
            }else{
                textTip.text = currentCard.rule
                Toast.makeText(applicationContext,"Default Rules",Toast.LENGTH_SHORT).show()
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        viewDeck.setOnClickListener {

            if(mutableDeck.isEmpty()){
                mutableDeck = CardsData().getAllCards()
                displayCardInViewAndToolbar(Card("back_red","card_back_red.png","Click to play again"))
                textCardCounter.text = "Cards left: ${mutableDeck.size}"
                textTip.text = "Click to play again"

                d("drinking-game","Size of the deck: ${mutableDeck.size}" )
                return@setOnClickListener
            }

            unfoldCardFromDeckAndDisplayInformation()
        }
    }

    private fun showCustomRules(): String {

        val customRules = database.getCustomRules()

        if (customRules.count != 0) {
            while (customRules.moveToNext()) {
                var ruleNameId = customRules.getString(1).substring(5)

                if (ruleNameId in currentCard.name) {
                    return customRules.getString(2)
                }
            }
        }
        return "Oops!"
    }

    private fun openRulesActivity(){
        val intent = Intent(this@MainActivity, RulesActivity::class.java)
        intent.putExtra("IS_CUSTOM_RULE_ON", switch.isChecked)
        startActivity(intent)
    }

    private fun unfoldCardFromDeckAndDisplayInformation(){
        currentCard = mutableDeck.shuffled().take(1).get(0)
        mutableDeck.remove(currentCard)

        d("drinking-game","size of the deck: ${mutableDeck.size} and unfolded card is: $currentCard" )
        displayCardInViewAndToolbar(currentCard)
        textCardCounter.text = "Cards left: ${mutableDeck.size}"
    }

    private fun displayCardInViewAndToolbar(card: Card){
        when (card.name) {
             "1C"-> viewDeck.setImageResource(R.drawable.ac)
             "1D"-> viewDeck.setImageResource(R.drawable.ad)
             "1H"-> viewDeck.setImageResource(R.drawable.ah)
             "1S"-> viewDeck.setImageResource(R.drawable.`as`)
             "2C"-> viewDeck.setImageResource(R.drawable.twoc)
             "2D"-> viewDeck.setImageResource(R.drawable.twod)
             "2H"-> viewDeck.setImageResource(R.drawable.twoh)
             "2S"-> viewDeck.setImageResource(R.drawable.twos)
             "3C"-> viewDeck.setImageResource(R.drawable.threec)
             "3D"-> viewDeck.setImageResource(R.drawable.threed)
             "3H"-> viewDeck.setImageResource(R.drawable.threeh)
             "3S"-> viewDeck.setImageResource(R.drawable.threes)
             "4C"-> viewDeck.setImageResource(R.drawable.fourc)
             "4D"-> viewDeck.setImageResource(R.drawable.fourd)
             "4H"-> viewDeck.setImageResource(R.drawable.fourh)
             "4S"-> viewDeck.setImageResource(R.drawable.fours)
             "5C"-> viewDeck.setImageResource(R.drawable.fivec)
             "5D"-> viewDeck.setImageResource(R.drawable.fived)
             "5H"-> viewDeck.setImageResource(R.drawable.fiveh)
             "5S"-> viewDeck.setImageResource(R.drawable.fives)
             "6C"-> viewDeck.setImageResource(R.drawable.sixc)
             "6D"-> viewDeck.setImageResource(R.drawable.sixd)
             "6H"-> viewDeck.setImageResource(R.drawable.sixh)
             "6S"-> viewDeck.setImageResource(R.drawable.sixs)
             "7C"-> viewDeck.setImageResource(R.drawable.sevenc)
             "7D"-> viewDeck.setImageResource(R.drawable.sevend)
             "7H"-> viewDeck.setImageResource(R.drawable.sevenh)
             "7S"-> viewDeck.setImageResource(R.drawable.sevens)
             "8C"-> viewDeck.setImageResource(R.drawable.eightc)
             "8D"-> viewDeck.setImageResource(R.drawable.eightd)
             "8H"-> viewDeck.setImageResource(R.drawable.eighth)
             "8S"-> viewDeck.setImageResource(R.drawable.eights)
             "9C"-> viewDeck.setImageResource(R.drawable.ninec)
             "9D"-> viewDeck.setImageResource(R.drawable.nined)
             "9H"-> viewDeck.setImageResource(R.drawable.nineh)
             "9S"-> viewDeck.setImageResource(R.drawable.nines)
             "10C"-> viewDeck.setImageResource(R.drawable.tenc)
             "10D"-> viewDeck.setImageResource(R.drawable.tend)
             "10H"-> viewDeck.setImageResource(R.drawable.tenh)
             "10S"-> viewDeck.setImageResource(R.drawable.tens)
             "JC"-> viewDeck.setImageResource(R.drawable.jc)
             "JD"-> viewDeck.setImageResource(R.drawable.jd)
             "JH"-> viewDeck.setImageResource(R.drawable.jh)
             "JS"-> viewDeck.setImageResource(R.drawable.js)
             "QC"-> viewDeck.setImageResource(R.drawable.qc)
             "QD"-> viewDeck.setImageResource(R.drawable.qd)
             "QH"-> viewDeck.setImageResource(R.drawable.qh)
             "QS"-> viewDeck.setImageResource(R.drawable.qs)
             "KC"-> viewDeck.setImageResource(R.drawable.kc)
             "KD"-> viewDeck.setImageResource(R.drawable.kd)
             "KH"-> viewDeck.setImageResource(R.drawable.kh)
             "KS"-> viewDeck.setImageResource(R.drawable.ks)

            else ->  viewDeck.setImageResource(R.drawable.card_back_red)

        }

        if(switch.isChecked){
            textTip.text = showCustomRules()
        }else{
            textTip.text = card.rule
        }

    }
}

