package com.devapp.drinkinggame

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    var mutableDeck = CardsData().getAllCards()
    private  var showLogOnlyOnce = true

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        viewDeck.setOnClickListener {

            if(mutableDeck.isEmpty()){
                mutableDeck = CardsData().getAllCards()
                displayCardInViewAndToolbarIfNeded(Card("back_red","card_back_red.png","Click to play again"))
                textCardCounter.setText("Cards left: ${mutableDeck.size}")
                textTip.setText("Click to play again")

                d("drinking-game","Size of the deck: ${mutableDeck.size}" )
                return@setOnClickListener
            }

            unfoldCardFromDeckAndDisplayInformation()
        }
    }

    private fun unfoldCardFromDeckAndDisplayInformation(){
        var card = mutableDeck.shuffled().take(1).get(0)
        mutableDeck.remove(card)

        d("drinking-game","size of the deck: ${mutableDeck.size} and unfolded card is: $card" )
        displayCardInViewAndToolbarIfNeded(card)
        textCardCounter.setText("Cards left: ${mutableDeck.size}")

        if("Drinking Game" == toolbar.title){
            textTip.setText(card.tooltip)
        }
    }

    private fun getPhoneHeight(): Int{

        var displayMetrics = DisplayMetrics();
        windowManager.defaultDisplay.getMetrics(displayMetrics);

        var width = displayMetrics.widthPixels;
        var height = displayMetrics.heightPixels;


        if(showLogOnlyOnce) {
            d("drinking-game", "Display metrics {width: $width , height: $height}")
            showLogOnlyOnce = false
        }
        return height
    }

    private fun displayCardInViewAndToolbarIfNeded(card: Card){
        when (card.name) {
             "AC"-> viewDeck.setImageResource(R.drawable.ac)
             "AD"-> viewDeck.setImageResource(R.drawable.ad)
             "AH"-> viewDeck.setImageResource(R.drawable.ah)
             "AS"-> viewDeck.setImageResource(R.drawable.`as`)
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

        if(getPhoneHeight() <= 1800){
            toolbar.setTitle(card.tooltip)
        }
    }
}
