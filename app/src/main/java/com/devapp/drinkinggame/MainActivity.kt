package com.devapp.drinkinggame

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log.d
import android.view.*
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.preference.PreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


class MainActivity : AppCompatActivity(), RightFragment.OnFragmentInteractionListener, GestureDetector.OnGestureListener {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var database = DatabaseHelper.getInstance(this)

    private var mutableDeck = CardsData().getAllCards()
    private var currentCardItem = CardItem("Back",R.drawable.card_back_blue,"Draw a card to continue...")
    private lateinit var switch: Switch

    private lateinit var fragmentContainer: FrameLayout
    private lateinit var gestureDetector: GestureDetector
    private var x1: Float = 0.0F
    private var x2: Float = 0.0F
    private var y1: Float = 0.0F
    private var y2: Float = 0.0F
    private var MIN_DISTANCE = 150

    private var historicalDeck = arrayListOf<CardItem>()

    private var isHistoricalFeatureEnabled = true
    private var isReturnCardFeatureEnabled = true
    private var isSoundFxFeatureEnabled = false

    private lateinit var textToSpeech: TextToSpeech


    companion object {
        private const val PREFERENCE_FEATURE_HISTORICAL = "prefHistorical"
        private const val PREFERENCE_FEATURE_RETURN_CARD = "prefReturnCard"
        private const val PREFERENCE_FEATURE_SOUND_FX= "prefSound"
        private const val MIN_DISTANCE = 150
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_rules_management-> openRulesActivity()
            R.id.action_settings -> openFragmentActivity()
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
                textTip.text = currentCardItem.rule
                Toast.makeText(applicationContext,"Default Rules",Toast.LENGTH_SHORT).show()
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        initPreferences()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        PreferenceManager.setDefaultValues(this, R.xml.settings, false)
        initPreferences()

        viewDeck.setOnClickListener {

            if(isFragmentVisible()){
               closeFragment()
            }else {
                unfoldCard()
            }
        }

        viewDeck.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                when(event!!.action){
                    MotionEvent.ACTION_DOWN -> {
                        x1 = event.x
                        y1 = event.y
                    }

                    MotionEvent.ACTION_UP -> {

                        x2 = event.x
                        y2 = event.y

                        var valueX = x2 - x1
                        var valueY = y2 - y1

                        if (Math.abs(valueX) > MIN_DISTANCE) {

                            if (x2 > x1) {
                                d("drinking-game","right swipe" )
//                                Toast.makeText(applicationContext, "right swipe", Toast.LENGTH_SHORT).show()
                                closeFragment()
                            } else {
                                d("drinking-game","left swipe" )
//                                Toast.makeText(applicationContext, "left swipe", Toast.LENGTH_SHORT).show()
                                if(isHistoricalFeatureEnabled){
                                    openFragment()
                                }
                            }

                        } else if (Math.abs(valueY) > MIN_DISTANCE) {

                            if (y2 > y1) {
                                d("drinking-game","bottom swipe" )
//                                Toast.makeText(applicationContext, "bottom swipe", Toast.LENGTH_SHORT).show()


                            } else {
                                d("drinking-game","top swipe" )
//                                Toast.makeText(applicationContext, "Top swipe", Toast.LENGTH_SHORT).show()

                            }
                        }else{
                            viewDeck.performClick()
                        }
                    }
                }
                return true
            }
        })

        initTextToSpeech()

        fragmentContainer = findViewById(R.id.fragment_container)
//        this.gestureDetector = GestureDetector(viewDeck.context.applicationContext, this);
    }

    private fun initTextToSpeech(){
        textToSpeech = TextToSpeech(this, OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result: Int = textToSpeech.setLanguage(Locale.ENGLISH)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    d("drinking-game","Language not supported" )
                }
            } else {
                d("drinking-game","Initialization failed" )
            }
        })
    }

    private fun speakIfEnabled(text: String){
        if(isSoundFxFeatureEnabled) {
            textToSpeech.setSpeechRate(1F)
            textToSpeech.setPitch(1F)

            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    private fun unfoldCard(){
        if (mutableDeck.isEmpty()) {
            mutableDeck = CardsData().getAllCards()

            displayCardInViewAndToolbar(
                CardItem("back_red", R.drawable.ic_1h_small, "Click to play again"))

            textCardCounter.text = "Cards left: ${mutableDeck.size}"
            textTip.text = "Click to play again"

            historicalDeck.clear()
            d("drinking-game", "Size of the deck: ${mutableDeck.size}")
            return
        }

        var unfoldedCard = unfoldCardFromDeckAndDisplayInformation()

        addCurrentCardIntoHistoricalList(unfoldedCard)
    }

    private fun isFragmentVisible():Boolean{

        val rightFragment: RightFragment? = supportFragmentManager.findFragmentByTag("RIGHT_FRAGMENT") as RightFragment?

        if (rightFragment != null && rightFragment.isVisible()) {
            return true
        }

        return false
    }

    private fun showCustomRules(): String {

        val customRules = database.getCustomRules()

        if (customRules.count != 0) {
            while (customRules.moveToNext()) {
                var ruleNameId = customRules.getString(1).substring(5)

                if (ruleNameId in currentCardItem.name!!) {
                    return customRules.getString(2)
                }
            }
        }
        return "Draw a card to continue..."
    }

    private fun openFragmentActivity(){
        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
//        intent.putExtra("IS_CUSTOM_RULE_ON", switch.isChecked)
        startActivity(intent)
    }

    private fun openRulesActivity(){
        val intent = Intent(this@MainActivity, RulesActivity::class.java)
        intent.putExtra("IS_CUSTOM_RULE_ON", switch.isChecked)
        startActivity(intent)
    }

    private fun unfoldCardFromDeckAndDisplayInformation(): CardItem{
        currentCardItem = mutableDeck.shuffled().take(1)[0]
        mutableDeck.remove(currentCardItem)

        d("drinking-game","size of the deck: ${mutableDeck.size} and unfolded card is: $currentCardItem" )
        displayCardInViewAndToolbar(currentCardItem)

        displayDeckCounter(mutableDeck)

        return currentCardItem
    }

    private fun displayDeckCounter(deck: MutableSet<CardItem> ){
        textCardCounter.text = "Cards left: ${deck.size}"
    }

    private fun addCardBackIntoDeck(cardItem: CardItem){
        mutableDeck.add(cardItem)
    }

    private fun addCurrentCardIntoHistoricalList(currentCardItem: CardItem){
        historicalDeck.add(0,currentCardItem)
    }

    private fun displayCardInViewAndToolbar(cardItem: CardItem){
        when (cardItem.name) {
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
            textTip.text = cardItem.rule
        }

        speakIfEnabled(textTip.text.toString())

    }

    private fun openFragment(){
        val fragment: RightFragment? = supportFragmentManager.findFragmentByTag("RIGHT_FRAGMENT") as RightFragment?

        val bundle = Bundle()
        bundle.putParcelableArrayList("historicalDeck", historicalDeck)
        bundle.putBoolean("isReturnCardFeatureEnabled", isReturnCardFeatureEnabled)

        if(fragment == null) {
            var fragment = RightFragment.newInstance()
            var fragmentManager = supportFragmentManager
            var transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_right
            )
            fragment.arguments = bundle
            transaction.addToBackStack("RIGHT_FRAGMENT")
            transaction.add(R.id.fragment_container, fragment, "RIGHT_FRAGMENT").commit()
        }else{
            var fragmentManager = supportFragmentManager
            var transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_right
            )
            fragment.arguments = bundle
            transaction.replace(R.id.fragment_container, fragment, "RIGHT_FRAGMENT");
        }
    }

    private fun closeFragment(){
        val fragment: RightFragment? = supportFragmentManager.findFragmentByTag("RIGHT_FRAGMENT") as RightFragment?

        if(fragment != null){
            onBackPressed()
        }
    }

    override fun onFragmentInteraction(cardItemDeleted : CardItem) {
        addCardBackIntoDeck(cardItemDeleted)
        displayDeckCounter(mutableDeck)
        deleteFromHistoricalList(cardItemDeleted)

//        onBackPressed()
    }

    private fun deleteFromHistoricalList(cardItem: CardItem){
        historicalDeck.remove(cardItem)
//            Toast.makeText(this, cardItem.name + " true", Toast.LENGTH_SHORT).show()

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
            }

            MotionEvent.ACTION_UP -> {

                x2 = event.x
                y2 = event.y

                var valueX = x2 - x1
                var valueY = y2 - y1

                if (Math.abs(valueX) > MIN_DISTANCE) {

                    if (x2 > x1) {
                        d("drinking-game","right swipe" )
//                        Toast.makeText(this, "right swipe", Toast.LENGTH_SHORT).show()
                        closeFragment()
                    } else {
                        d("drinking-game","left swipe" )
//                        Toast.makeText(this, "left swipe", Toast.LENGTH_SHORT).show()
                        if(isHistoricalFeatureEnabled){
                            openFragment()
                        }
                    }

                } else if (Math.abs(valueY) > MIN_DISTANCE) {

                    if (y2 > y1) {
                        d("drinking-game","bottom swipe" )
//                        Toast.makeText(this, "bottom swipe", Toast.LENGTH_SHORT).show()


                    } else {
                        d("drinking-game","top swipe" )
//                        Toast.makeText(this, "Top swipe", Toast.LENGTH_SHORT).show()

                    }
                }else{
                    viewDeck.performClick()
                }
            }
        }
//        return super.onTouchEvent(event)
        return true
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true

    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return true

    }


    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return true

    }

    override fun onLongPress(e: MotionEvent?) {

    }

    private fun initPreferences(){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        isHistoricalFeatureEnabled = sharedPreferences.getBoolean(PREFERENCE_FEATURE_HISTORICAL, true)
        isReturnCardFeatureEnabled = sharedPreferences.getBoolean(PREFERENCE_FEATURE_RETURN_CARD, true)
        isSoundFxFeatureEnabled = sharedPreferences.getBoolean(PREFERENCE_FEATURE_SOUND_FX, false)
    }

    override fun onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop()
            textToSpeech.shutdown()
        }

        super.onDestroy()
    }
}

