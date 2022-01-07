package com.devapp.drinkinggame

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
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
import com.devapp.drinkinggame.mutliplayer.MultiplayerActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.teresaholfeld.stories.StoriesProgressView
import kotlinx.android.synthetic.main.content_main.*
import java.lang.reflect.Type
import java.util.*


class MainActivity : AppCompatActivity(), RightFragment.OnFragmentInteractionListener, StoriesProgressView.StoriesListener{

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var database = DatabaseHelper.getInstance(this)

    private lateinit var mutableDeck: MutableSet<CardItem>
    private lateinit var currentCardItem: CardItem
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
    private var isProgressCardEnabled = false

    private lateinit var textToSpeech: TextToSpeech

    private var cardsData = CardsData()

    companion object {
        private const val MIN_DISTANCE = 150
        private const val PREFERENCE_FEATURE_HISTORICAL = "prefHistorical"
        private const val PREFERENCE_FEATURE_RETURN_CARD = "prefReturnCard"
        private const val PREFERENCE_FEATURE_SOUND_FX = "prefSound"
        private const val PREFERENCE_FEATURE_DARK_MODE = "prefDark"
        private const val PREFERENCE_FEATURE_PROGRESS_CARD = "prefStoriesProgress"

        private const val SHARED_HISTORICAL_DECK = "sharedHistoricalDeck"
        private const val SHARED_PLAYING_DECK = "sharedPlayingDeck"
        private const val SHARED_CURRENT_CARD = "sharedCurrentCard"
        private const val SHARED_SWITCH_MODE = "sharedSwitchMode"

    }

    private var cardResources = intArrayOf(R.drawable.card_back_blue)
    private lateinit var storiesProgressView: StoriesProgressView

    private var lastTouchTime: Long = 0
    private var longPressTime = 500

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_rules_management-> openRulesActivity()
            R.id.action_multiplayer-> openMultiplayerActivity()
            R.id.action_settings -> openFragmentActivity()
            R.id.main_refresh -> createNewGame()
            else ->  super.onOptionsItemSelected(item)
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        Log.d(Constants.APP_NAME, "onCreateOptionsMenu() ")
        var item = menu.findItem(R.id.main_switch)
        val view: View = MenuItemCompat.getActionView(item)
        switch = view.findViewById<View>(R.id.switchRule) as Switch
        switch.isChecked = loadSwitchMode()
        switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(switch.isChecked){
                textTip.text = showCustomRules()
                Toast.makeText(applicationContext,  resources.getString(R.string.custom_rules), Toast.LENGTH_SHORT).show()
            }else{
                textTip.text = showDefaultRules()
                Toast.makeText(applicationContext, resources.getString(R.string.default_rules),Toast.LENGTH_SHORT).show()
            }

            if(historicalDeck.isNotEmpty()){
                var duration = getCurrentCardDurationBasedOnMode(switch.isChecked)
                storiesProgressView.setStoryDuration(duration.toLong() * 1000)
                storiesProgressView.startStories()
            }

            persistSwitchMode()
        })

        if(loadCurrentCard().name == "Back"){
            Log.d(Constants.APP_NAME, "onCreate() -> New Game deleting sharedPreferences...")
            freeSharedPreferences()
        }else{
            Log.d(Constants.APP_NAME, "onCreate() -> Existing Game load sharedPreferences...")
//            initStoriesProgressView()
            displayCardInView(loadCurrentCard())
            displayRuleInToolbar()

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

        currentCardItem = loadCurrentCard()
        mutableDeck = cardsData.getAllCards(applicationContext)

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

        if(isProgressCardEnabled){
            initStoriesProgressView()
        }else{
            storiesProgressView = findViewById<StoriesProgressView>(R.id.stories)
            storiesProgressView.visibility = View.GONE
            //should hide a view of the progress view
        }

        PreferenceManager.setDefaultValues(this, R.xml.settings, false)

        viewDeck.setOnClickListener {
//            Toast.makeText(this, "setOnClickListener() ", Toast.LENGTH_SHORT).show()

            if(isFragmentVisible()){
               closeFragment()
            }else {
                unfoldCard()
            }
        }

        viewDeck.setOnLongClickListener {
//            Toast.makeText(this, "setOnLongClickListener() Long!", Toast.LENGTH_SHORT).show()
            storiesProgressView.resume()
            true
        }

        viewDeck.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event!!.action){
                    MotionEvent.ACTION_DOWN -> {
                        lastTouchTime = SystemClock.elapsedRealtime()
//                        Toast.makeText(applicationContext, "setOnTouchListener() -> ACTION_DOWN ", Toast.LENGTH_SHORT).show()

                        x1 = event.x
                        y1 = event.y
                        if(isProgressCardEnabled){
                            storiesProgressView.pause()
                        }
                    }

                    MotionEvent.ACTION_UP -> {
//                        Toast.makeText(applicationContext, "setOnTouchListener() -> ACTION_UP ", Toast.LENGTH_SHORT).show()

                        x2 = event.x
                        y2 = event.y

                        var valueX = x2 - x1
                        var valueY = y2 - y1

                        if (Math.abs(valueX) > MIN_DISTANCE) {

                            if (x2 > x1) {
                                Log.d(Constants.APP_NAME,"onTouch() -> right swipe" )
                                closeFragment()
//                                storiesProgressView.resume();
                            } else {
                                Log.d(Constants.APP_NAME,"onTouch() -> left swipe")
                                if(isHistoricalFeatureEnabled){
                                    if(isProgressCardEnabled){
                                        storiesProgressView.pause()
                                    }
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
                            Log.d(Constants.APP_NAME,"setOnTouchListener() ->  elapsedRealtime=${SystemClock.elapsedRealtime()} lastTouchTime=$lastTouchTime longPressTime=$longPressTime" )

                            if((SystemClock.elapsedRealtime() - lastTouchTime > longPressTime) && isProgressCardEnabled){
                                viewDeck.performLongClick()
                            }else{
                                viewDeck.performClick()
                            }
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

                var phoneLocale = Locale.getDefault().isO3Language
                val result: Int = textToSpeech.setLanguage(Locale(phoneLocale, phoneLocale))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.d(Constants.APP_NAME,"initTextToSpeech() -> Language not supported" )
                    if(phoneLocale == "eus"){
                        Log.d(Constants.APP_NAME,"initTextToSpeech() -> Defaulting to spanish" )
                        textToSpeech.language = Locale("spa", "spa")
                    }else{
                        Log.d(Constants.APP_NAME,"initTextToSpeech() -> Defaulting to english" )
                        textToSpeech.language = Locale("eng", "eng")
                    }

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
            mutableDeck = cardsData.getAllCards(applicationContext)
            persistPlayingDeck()

            currentCardItem = CardItem("Back", R.drawable.card_back_blue, resources.getString(R.string.press_to_play_again), 5000)
            persistCurrentCard()
            displayCardInView(currentCardItem)
            displayRuleInToolbar()

            textCardCounter.text = resources.getString(R.string.cards_left) + mutableDeck.size
            textTip.text = resources.getString(R.string.press_to_play_again)

            historicalDeck.clear()
            persistHistoricalDeck()
            Log.d(Constants.APP_NAME, "unfoldCard() -> Size of the deck: ${mutableDeck.size}")
            return
        }

        var unfoldedCard = unfoldCardFromDeck()

        if(isProgressCardEnabled){ // this is the new automatic swipe feature condition
            automaticCardSwipe(currentCardItem)
        }else{
            displayCardInView(currentCardItem)
        }

        displayRuleInToolbar()
        displayDeckCounter(mutableDeck.size)

        addCurrentCardIntoHistoricalList(unfoldedCard)
    }

    private fun initStoriesProgressView(){
        storiesProgressView = findViewById<StoriesProgressView>(R.id.stories)
        storiesProgressView.visibility = View.VISIBLE
        storiesProgressView.setStoriesCount(1) // <- set stories
        storiesProgressView.setStoryDuration(2000L)
        storiesProgressView.setStoriesListener(this) // <- set listener
        storiesProgressView.pause() // <- start progress
    }

    private fun automaticCardSwipe(cardItem: CardItem) {
        storiesProgressView = findViewById<StoriesProgressView>(R.id.stories)
        storiesProgressView.setStoriesCount(1) // <- set stories
        storiesProgressView.setStoryDuration(getCurrentCardDurationBasedOnMode(switch.isChecked).toLong() * 1000)
        storiesProgressView.setStoriesListener(this) // <- set listener
        storiesProgressView.startStories() // <- start progress

        cardResources[0] = cardsData.getImageResourceDrawable(cardItem)
        viewDeck.setImageResource(cardResources[0])
    }

    private fun isFragmentVisible():Boolean{

        val rightFragment: RightFragment? = supportFragmentManager.findFragmentByTag("RIGHT_FRAGMENT") as RightFragment?

        if (rightFragment != null && rightFragment.isVisible) {
            return true
        }

        return false
    }

    private fun getCurrentCardDurationBasedOnMode(isCustom: Boolean): Int{
        var duration = 0

        if(isCustom){
            val customRules = database.getCustomRules()

            if (customRules.count != 0) {
                while (customRules.moveToNext()) {
                    var ruleNameId = customRules.getString(1).substring(5)

                    if (ruleNameId in currentCardItem.name!!) {
                        duration = customRules.getInt(3)
                    }
                }
            }

        }else{
            if(currentCardItem.name!!.contains("Back")){
                duration = 0
            }else{
                duration = cardsData.getDefaultDuration(currentCardItem, applicationContext)
            }
        }

        return duration
    }

    private fun showDefaultRules(): String {
        var tooltip = cardsData.getDefaultRule(currentCardItem, applicationContext)
        if(tooltip == "null"){
            tooltip = resources.getString(R.string.draw_a_card_to_continue)
        }

        return tooltip
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
        return resources.getString(R.string.draw_a_card_to_continue)
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

    private fun openMultiplayerActivity(){
        val intent = Intent(this@MainActivity, MultiplayerActivity::class.java)
//        intent.putExtra("IS_CUSTOM_RULE_ON", switch.isChecked)
        startActivity(intent)
    }


    private fun unfoldCardFromDeck(): CardItem{
        currentCardItem = mutableDeck.shuffled().take(1)[0]
        persistCurrentCard()
        mutableDeck.remove(currentCardItem)
        persistPlayingDeck()

        Log.d(Constants.APP_NAME,"unfoldCardFromDeck() -> Deck: ${mutableDeck.size} histo: ${historicalDeck.size} current: ${currentCardItem.name}" )

        return currentCardItem
    }

    private fun displayDeckCounter(size: Int ){
        textCardCounter.text = resources.getString(R.string.cards_left) + size
    }

    private fun addCardBackIntoDeck(cardItem: CardItem){
        mutableDeck.add(cardItem)
        persistPlayingDeck()
    }

    private fun addCurrentCardIntoHistoricalList(currentCardItem: CardItem){
        historicalDeck.add(0,currentCardItem)
        persistHistoricalDeck()
    }

    private fun displayCardInView(cardItem: CardItem){

        when (cardItem.name) {
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

            else ->  viewDeck.setImageResource(R.drawable.card_back_blue)

        }
    }

    private fun displayRuleInToolbar(){

        switch.isChecked = loadSwitchMode()
        if(switch.isChecked){
            textTip.text = showCustomRules()
        }else{
            textTip.text = showDefaultRules()
        }

        speakIfEnabled(textTip.text.toString())
    }

    private fun openFragment(){
        val fragment: RightFragment? = supportFragmentManager.findFragmentByTag("RIGHT_FRAGMENT") as RightFragment?

        val bundle = Bundle()
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
                        if(isProgressCardEnabled){
                            storiesProgressView.resume()
                        }
                    } else {
                        Log.d(Constants.APP_NAME,"onTouchEvent() -> left swipe" )
//                        Toast.makeText(this, "left swipe", Toast.LENGTH_SHORT).show()
                        if(isHistoricalFeatureEnabled){
                            if(isProgressCardEnabled){
                                storiesProgressView.pause()
                            }
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
        isProgressCardEnabled =  sharedPreferences.getBoolean(PREFERENCE_FEATURE_PROGRESS_CARD, false)
    }

    override fun onDestroy() {
//        deleteCurrentCard()

        if(textToSpeech != null){
            textToSpeech.stop()
            textToSpeech.shutdown()
        }

        // Very important !
        if(isProgressCardEnabled){
            storiesProgressView.destroy()
        }
        super.onDestroy()
    }

    private fun createNewGame(){
        deleteCurrentCard()
        deleteHistoricalDeck()
        deletePlayingDeck()
//        deleteSwitchMode()

        val intent  = Intent(this, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun freeSharedPreferences(){
        deleteHistoricalDeck()
        deletePlayingDeck()
//        deleteSwitchMode()
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
            mutableDeck = CardsData().getAllCards(applicationContext)
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
            ?: return CardItem("Back",R.drawable.card_back_blue,resources.getString(R.string.cards_left), 5000)
        val type: Type = object : TypeToken<CardItem?>() {}.type
        currentCardItem = gson.fromJson(json, type)
        Log.d(Constants.APP_NAME, "loadCurrentCard() ->Loading card=${currentCardItem.name}")
        return currentCardItem
    }

    private fun deleteCurrentCard(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPreferences.edit()
        editor.remove(SHARED_CURRENT_CARD)
        editor.commit()
        Log.d(Constants.APP_NAME, "deleteCurrentCard() -> Deleting current card...")
    }

    private fun persistSwitchMode(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPreferences.edit()
        editor.putBoolean(SHARED_SWITCH_MODE, switch.isChecked)
        editor.apply()
        Log.d(Constants.APP_NAME, "persistSwitchMode() Persisting switch state isChecked=${switch.isChecked}")
    }

    private fun loadSwitchMode(): Boolean{
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val switchValue = sharedPreferences.getBoolean(SHARED_SWITCH_MODE, false)
        Log.d(Constants.APP_NAME, "loadSwitchMode() Loading switch value isChecked=$switchValue")
        return switchValue

    }

    private fun deleteSwitchMode(){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPreferences.edit()
        editor.remove(SHARED_SWITCH_MODE)
        editor.commit()
    }

    override fun onComplete() {
        unfoldCard()
    }

    override fun onPrev() {
//        Toast.makeText(this, "onPrev", Toast.LENGTH_SHORT).show()
    }

    override fun onNext() {
//        Toast.makeText(this, "onNext", Toast.LENGTH_SHORT).show()

    }
}

