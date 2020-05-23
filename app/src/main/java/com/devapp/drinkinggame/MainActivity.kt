package com.devapp.drinkinggame

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.preference.PreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.content_main.*
import java.lang.reflect.Type
import java.util.*


class MainActivity : AppCompatActivity(), RightFragment.OnFragmentInteractionListener {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var database = DatabaseHelper.getInstance(this)

    private var mutableDeck = CardsData().getAllCards()
    private var currentCardItem = CardItem("Back",R.drawable.card_back_blue,"Draw a card to continue...")
    private lateinit var switch: Switch

    private lateinit var fragmentContainer: FrameLayout
    private var x1: Float = 0.0F
    private var x2: Float = 0.0F
    private var y1: Float = 0.0F
    private var y2: Float = 0.0F

    private var historicalDeck = arrayListOf<CardItem>()

    private var isHistoricalFeatureEnabled = true
    private var isReturnCardFeatureEnabled = true
    private var isSoundFxFeatureEnabled = false
    private var isDarkModeFeatureEnabled = false

    private lateinit var textToSpeech: TextToSpeech


    companion object {
        private const val MIN_DISTANCE = 150
        private const val PREFERENCE_FEATURE_HISTORICAL = "prefHistorical"
        private const val PREFERENCE_FEATURE_RETURN_CARD = "prefReturnCard"
        private const val PREFERENCE_FEATURE_SOUND_FX = "prefSound"
        private const val PREFERENCE_FEATURE_DARK_MODE = "prefDark"

        private const val SHARED_HISTORICAL_DECK = "sharedHistoricalDeck"
        private const val SHARED_PLAYING_DECK = "sharedPlayingDeck"
        private const val SHARED_CURRENT_CARD = "sharedCurrentCard"
        private const val SHARED_SWITCH_MODE = "sharedSwitchMode"
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
            persistSwitchMode()
        })

        if(loadCurrentCard().name == "Back"){
            Log.d(Constants.APP_NAME, "onCreate() -> New Game deleting sharedPreferences...")
            freeSharedPreferences()
        }else{
            Log.d(Constants.APP_NAME, "onCreate() -> Existing Game load sharedPreferences...")
            displayCardInViewAndToolbar(loadCurrentCard())

            mutableDeck = loadPlayingDeck()
            historicalDeck = loadHistoricalDeck()
            displayDeckCounter(mutableDeck.size)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
//        initPreferences()

        if(isDarkModeFeatureEnabled){
            setTheme(R.style.DarkTheme)
        }else{
            setTheme(R.style.AppTheme)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        Log.d(Constants.APP_NAME,"===============================================================")
        initPreferences()

        if(isDarkModeFeatureEnabled){
            setTheme(R.style.DarkTheme)
        }else{
            setTheme(R.style.AppTheme)
        }

        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if(isDarkModeFeatureEnabled){
            toolbar.setBackgroundColor(Color.parseColor("#000000"))
        }else{
            toolbar.setBackgroundColor(Color.parseColor("#3700B3"))
        }
        setSupportActionBar(toolbar)
//        toolbar.setSubtitle("By Murcielago Dev Commits");

        Log.d(Constants.APP_NAME,"onCreate() -> ${loadCurrentCard().name }")


        PreferenceManager.setDefaultValues(this, R.xml.settings, false)

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
                                Log.d(Constants.APP_NAME,"onTouch() -> right swipe" )
                                closeFragment()
                            } else {
                                Log.d(Constants.APP_NAME,"onTouch() -> left swipe")
                                if(isHistoricalFeatureEnabled){
                                    openFragment()
                                }
                            }

                        } else if (Math.abs(valueY) > MIN_DISTANCE) {

                            if (y2 > y1) {
                                Log.d(Constants.APP_NAME,"onTouch() -> bottom swipe" )


                            } else {
                                Log.d(Constants.APP_NAME,"onTouch() -> top swipe" )

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

    }

    private fun initTextToSpeech(){
        textToSpeech = TextToSpeech(this, OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result: Int = textToSpeech.setLanguage(Locale.ENGLISH)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.d(Constants.APP_NAME,"initTextToSpeech() -> Language not supported" )
                }
            } else {
                Log.d(Constants.APP_NAME,"initTextToSpeech() -> Initialization failed" )
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
            persistPlayingDeck()

            displayCardInViewAndToolbar(
                CardItem("back_red", R.drawable.ic_1h_small, "Click to play again"))

            textCardCounter.text = "Cards left: ${mutableDeck.size}"
            textTip.text = "Click to play again"

            historicalDeck.clear()
            persistHistoricalDeck()
            Log.d(Constants.APP_NAME, "unfoldCard() -> Size of the deck: ${mutableDeck.size}")
            return
        }

        var unfoldedCard = unfoldCardFromDeckAndDisplayInformation()

        addCurrentCardIntoHistoricalList(unfoldedCard)
    }

    private fun isFragmentVisible():Boolean{

        val rightFragment: RightFragment? = supportFragmentManager.findFragmentByTag("RIGHT_FRAGMENT") as RightFragment?

        if (rightFragment != null && rightFragment.isVisible) {
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
        persistCurrentCard()
        mutableDeck.remove(currentCardItem)
        persistPlayingDeck()

        Log.d(Constants.APP_NAME,"unfoldCardFromDeckAndDisplayInformation() -> Deck: ${mutableDeck.size} histo: ${historicalDeck.size} current: ${currentCardItem.name}" )
        displayCardInViewAndToolbar(currentCardItem)

        displayDeckCounter(mutableDeck.size)

        return currentCardItem
    }

    private fun displayDeckCounter(size: Int ){
        textCardCounter.text = "Cards left: $size"
    }

    private fun addCardBackIntoDeck(cardItem: CardItem){
        mutableDeck.add(cardItem)
        persistPlayingDeck()
    }

    private fun addCurrentCardIntoHistoricalList(currentCardItem: CardItem){
        historicalDeck.add(0,currentCardItem)
        persistHistoricalDeck()
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

        switch.isChecked = loadSwitchMode()
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
//        bundle.putParcelableArrayList("historicalDeck", historicalDeck)
        bundle.putParcelableArrayList("historicalDeck", loadHistoricalDeck())
        bundle.putBoolean("isReturnCardFeatureEnabled", isReturnCardFeatureEnabled)
        Log.d(Constants.APP_NAME,"openFragment() -> Opened Right Fragment histo:${historicalDeck.size} loaded:${loadHistoricalDeck().size}")

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
            transaction.replace(R.id.fragment_container, fragment, "RIGHT_FRAGMENT")
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
        displayDeckCounter(mutableDeck.size)
        deleteFromHistoricalList(cardItemDeleted)
    }

    private fun deleteFromHistoricalList(cardItem: CardItem){

//        historicalDeck.remove(cardItem)

        var i = 0
        for(item in historicalDeck){
            if(cardItem.name == item.name){
                historicalDeck.removeAt(i)
                break
            }
            i++
        }

        persistHistoricalDeck()
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
                        Log.d(Constants.APP_NAME,"onTouchEvent() -> right swipe" )
//                        Toast.makeText(this, "right swipe", Toast.LENGTH_SHORT).show()
                        closeFragment()
                    } else {
                        Log.d(Constants.APP_NAME,"onTouchEvent() -> left swipe" )
//                        Toast.makeText(this, "left swipe", Toast.LENGTH_SHORT).show()
                        if(isHistoricalFeatureEnabled){
                            openFragment()
                        }
                    }

                } else if (Math.abs(valueY) > MIN_DISTANCE) {

                    if (y2 > y1) {
                        Log.d(Constants.APP_NAME,"onTouchEvent() -> bottom swipe" )
//                        Toast.makeText(this, "bottom swipe", Toast.LENGTH_SHORT).show()


                    } else {
                        Log.d(Constants.APP_NAME,"onTouchEvent() -> top swipe" )
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

    private fun initPreferences(){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        isHistoricalFeatureEnabled = sharedPreferences.getBoolean(PREFERENCE_FEATURE_HISTORICAL, true)
        isReturnCardFeatureEnabled = sharedPreferences.getBoolean(PREFERENCE_FEATURE_RETURN_CARD, true)
        isSoundFxFeatureEnabled = sharedPreferences.getBoolean(PREFERENCE_FEATURE_SOUND_FX, false)
        isDarkModeFeatureEnabled = sharedPreferences.getBoolean(PREFERENCE_FEATURE_DARK_MODE, false)
    }

    override fun onDestroy() {
        deleteCurrentCard()

        if(textToSpeech != null){
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }

    private fun freeSharedPreferences(){
        deleteHistoricalDeck()
        deletePlayingDeck()
        deleteSwitchMode()
    }

    private fun persistHistoricalDeck(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPreferences.edit()
        var gson = Gson()
        var json = gson.toJson(historicalDeck)
        editor.putString(SHARED_HISTORICAL_DECK, json)
        editor.apply()
        Log.d(Constants.APP_NAME, "persistHistoricalDeck() -> Persisting histo: ${historicalDeck.size} ")
    }

    private fun loadHistoricalDeck(): ArrayList<CardItem>{
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var gson = Gson()
        var json: String? = sharedPreferences.getString(SHARED_HISTORICAL_DECK,null)
            ?: return arrayListOf<CardItem>()
        val type: Type = object : TypeToken<ArrayList<CardItem?>?>() {}.type
        historicalDeck = gson.fromJson(json, type)

        if(historicalDeck == null){
            Log.d(Constants.APP_NAME, "loadHistoricalDeck() -> histo deck is empty")
            historicalDeck = arrayListOf<CardItem>()
        }

        return historicalDeck
    }

    private fun deleteHistoricalDeck(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPreferences.edit()
        editor.remove(SHARED_HISTORICAL_DECK)
        editor.commit()
        Log.d(Constants.APP_NAME, "deleteHistoricalDeck() -> Deleting history deck...")
    }

    private fun persistPlayingDeck(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPreferences.edit()
        var gson = Gson()
        var json = gson.toJson(mutableDeck)
        editor.putString(SHARED_PLAYING_DECK, json)
        editor.apply()
        Log.d(Constants.APP_NAME, "persistPlayingDeck() -> Persisting playing deck: ${mutableDeck.size} ")

    }

    private fun loadPlayingDeck(): MutableSet<CardItem>{
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var gson = Gson()
        var json: String? = sharedPreferences.getString(SHARED_PLAYING_DECK,null)
        val type: Type = object : TypeToken<MutableSet<CardItem?>?>() {}.type
        mutableDeck = gson.fromJson(json, type)
        if(mutableDeck == null){
            Log.d(Constants.APP_NAME, "loadPlayingDeck() -> Loading playing deck with cards")
            mutableDeck = CardsData().getAllCards()
        }

        return mutableDeck
    }

    private fun deletePlayingDeck(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPreferences.edit()
        editor.remove(SHARED_PLAYING_DECK)
        editor.commit()
        Log.d(Constants.APP_NAME, "deletePlayingDeck() -> Deleting playing deck...")
    }

    private fun persistCurrentCard(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPreferences.edit()
        var gson = Gson()
        var json = gson.toJson(currentCardItem)
        editor.putString(SHARED_CURRENT_CARD, json)
        editor.apply()
        Log.d(Constants.APP_NAME, "persistCurrentCard() -> Persisting current card: ${currentCardItem.name} ")

    }

    private fun loadCurrentCard(): CardItem{
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var gson = Gson()
        var json: String? = sharedPreferences.getString(SHARED_CURRENT_CARD,null)
            ?: return CardItem("Back",R.drawable.card_back_blue,"Draw a card to continue...")
        val type: Type = object : TypeToken<CardItem?>() {}.type
        currentCardItem = gson.fromJson(json, type)

//        if(currentCardItem == null){
//            return CardItem("Back",R.drawable.card_back_blue,"Draw a card to continue...")
//        }

        return currentCardItem
    }

    private fun deleteCurrentCard(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPreferences.edit()
        editor.remove(SHARED_SWITCH_MODE)
        editor.commit()
        Log.d(Constants.APP_NAME, "deleteCurrentCard() -> Deleting current card...")
    }

    private fun persistSwitchMode(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPreferences.edit()
        editor.putBoolean(SHARED_SWITCH_MODE, switch.isChecked)
        editor.apply()
    }

    private fun loadSwitchMode(): Boolean{
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreferences.getBoolean(SHARED_SWITCH_MODE, false)

    }

    private fun deleteSwitchMode(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPreferences.edit()
        editor.remove(SHARED_SWITCH_MODE)
        editor.commit()
    }
}

