package com.devapp.drinkinggame

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_rules.*


class RulesActivity : AppCompatActivity() {

    private var database = DatabaseHelper.getInstance(this)

    private lateinit var spinner : Spinner
    private var isCustomMode: Boolean = false

    private var isDarkModeFeatureEnabled = false

    companion object {
        private const val PREFERENCE_FEATURE_DARK_MODE = "prefDark"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_rules, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title){
           "Save"-> saveDataIfCustomMode()
            else -> {
                finish()
            }

        }
        return true
    }

    private fun saveDataIfCustomMode(){
        if(spinner.selectedItemPosition == 1){
            for(card in getTextFieldValues()){
                database.updateRule(card.name, card.rule, card.value )
            }
            Toast.makeText(this, resources.getString(R.string.saved), Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, resources.getString(R.string.unable_to_save), Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPreferences()

        if(isDarkModeFeatureEnabled){
            setTheme(R.style.DarkTheme)
        }else{
            setTheme(R.style.AppTheme)
        }
        setContentView(R.layout.activity_rules)

        isCustomMode = intent.getBooleanExtra("IS_CUSTOM_RULE_ON", isCustomMode)
        Log.d("drinking-game", "Intent Extra IS_CUSTOM_RULE_ON: $isCustomMode")

        if(isCustomMode){
            textRuleMode.text = resources.getString(R.string.playing_with_custom_rules)
        }else{
            textRuleMode.text = resources.getString(R.string.playing_with_default_rules)
        }

        initToolbar()
        initSpinner()
        initNumberPickers()

        /**
         * default rules are populated into the fields onItemSelected() when the activity first loads
         */

        addCloseArrowIconInToolbar()
        replaceTitleFromToolbar()
    }

    private fun initSpinner(){

        spinner = this.findViewById(R.id.spinnerRules)

        val itemsAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.spinnerNames))
        itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = itemsAdapter
        spinner.setSelection(if(isCustomMode) 1 else 0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("to be implemented if needed")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                populateRulesTextFields(position)
            }
        }
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

    private fun addCloseArrowIconInToolbar(){
        val toolbar = this.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)

    }

    private fun replaceTitleFromToolbar(){
        this.title = getString(R.string.action_rules_management)
    }

    private fun populateRulesTextFields(position: Int){
        if(position == 1) populateCustomRules() else populateDefaultRules()
    }

    private fun populateCustomRules(){
        val customRules = database.getCustomRules()

        if(customRules.count != 0){
            while(customRules.moveToNext()){
                when(customRules.getString(1)){
                    "RULE_A"-> {
                        inputAce.setText(customRules.getString(2))
                        numberPickerAces.value = customRules.getInt(3)
                    }
                    "RULE_2"-> {
                        inputTwo.setText(customRules.getString(2))
                        numberPickerTwos.value = customRules.getInt(3)
                    }
                    "RULE_3"-> {
                        inputThree.setText(customRules.getString(2))
                        numberPickerThrees.value = customRules.getInt(3)
                    }
                    "RULE_4"-> {
                        inputFour.setText(customRules.getString(2))
                        numberPickerFours.value = customRules.getInt(3)
                    }
                    "RULE_5"-> {
                        inputFive.setText(customRules.getString(2))
                        numberPickerFives.value = customRules.getInt(3)
                    }
                    "RULE_6"-> {
                        inputSix.setText(customRules.getString(2))
                        numberPickerSixes.value = customRules.getInt(3)
                    }
                    "RULE_7"-> {
                        inputSeven.setText(customRules.getString(2))
                        numberPickerSevens.value = customRules.getInt(3)
                    }
                    "RULE_8"-> {
                        inputEight.setText(customRules.getString(2))
                        numberPickerEights.value = customRules.getInt(3)
                    }
                    "RULE_9"-> {
                        inputNine.setText(customRules.getString(2))
                        numberPickerNines.value = customRules.getInt(3)
                    }
                    "RULE_10"-> {
                        inputTen.setText(customRules.getString(2))
                        numberPickerTens.value = customRules.getInt(3)
                    }
                    "RULE_J"-> {
                        inputJack.setText(customRules.getString(2))
                        numberPickerJacks.value = customRules.getInt(3)
                    }
                    "RULE_Q"-> {
                        inputQueen.setText(customRules.getString(2))
                        numberPickerQueens.value = customRules.getInt(3)
                    }
                    "RULE_K"-> {
                        inputKing.setText(customRules.getString(2))
                        numberPickerKings.value = customRules.getInt(3)
                    }
                    else ->  Toast.makeText(this, "Oops!", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            clearDefaultFields()
        }
    }

    private fun clearDefaultFields(){
        inputAce.setText("")
        inputTwo.setText("")
        inputThree.setText("")
        inputFour.setText("")
        inputFive.setText("")
        inputSix.setText("")
        inputSeven.setText("")
        inputEight.setText("")
        inputNine.setText("")
        inputTen.setText("")
        inputJack.setText("")
        inputQueen.setText("")
        inputKing.setText("")
    }

    private fun populateDefaultRules(){
        val allCardsSet = CardsData().getAllCards(applicationContext)
        val allRulesMap = allCardsSet.map { it.name to it.rule }.toMap()
        val allTimesMap = allCardsSet.map { it.name to it.value}.toMap()

        inputAce.setText(allRulesMap["AC"])
        numberPickerAces.value = allTimesMap["AC"]!!

        inputTwo.setText(allRulesMap["2C"])
        numberPickerTwos.value = allTimesMap["2C"]!!

        inputThree.setText(allRulesMap["3C"])
        numberPickerThrees.value = allTimesMap["3C"]!!

        inputFour.setText(allRulesMap["4C"])
        numberPickerFours.value = allTimesMap["4C"]!!

        inputFive.setText(allRulesMap["5C"])
        numberPickerFives.value = allTimesMap["5C"]!!

        inputSix.setText(allRulesMap["6C"])
        numberPickerSixes.value = allTimesMap["6C"]!!

        inputSeven.setText(allRulesMap["7C"])
        numberPickerSevens.value = allTimesMap["7C"]!!

        inputEight.setText(allRulesMap["8C"])
        numberPickerEights.value = allTimesMap["8C"]!!

        inputNine.setText(allRulesMap["9C"])
        numberPickerNines.value = allTimesMap["9C"]!!

        inputTen.setText(allRulesMap["10C"])
        numberPickerTens.value = allTimesMap["10C"]!!

        inputJack.setText(allRulesMap["JC"])
        numberPickerJacks.value = allTimesMap["JC"]!!

        inputQueen.setText(allRulesMap["QC"])
        numberPickerQueens.value = allTimesMap["QC"]!!

        inputKing.setText(allRulesMap["KC"])
        numberPickerKings.value = allTimesMap["KC"]!!
    }

    private fun getTextFieldValues() : List<CardItem>{
        return listOf(
            CardItem("RULE_A",0, inputAce.text?.toString(), numberPickerAces.value),
            CardItem("RULE_2",0, inputTwo.text?.toString(), numberPickerTwos.value),
            CardItem("RULE_3",0, inputThree.text?.toString(), numberPickerThrees.value),
            CardItem("RULE_4",0, inputFour.text?.toString(), numberPickerFours.value),
            CardItem("RULE_5",0, inputFive.text?.toString(), numberPickerFives.value),
            CardItem("RULE_6",0, inputSix.text?.toString(), numberPickerSixes.value),
            CardItem("RULE_7",0, inputSeven.text?.toString(), numberPickerSevens.value),
            CardItem("RULE_8",0, inputEight.text?.toString(), numberPickerEights.value),
            CardItem("RULE_9",0, inputNine.text?.toString(), numberPickerNines.value),
            CardItem("RULE_10",0, inputTen.text?.toString(), numberPickerTens.value),
            CardItem("RULE_J",0, inputJack.text?.toString(), numberPickerJacks.value),
            CardItem("RULE_Q",0, inputQueen.text?.toString(),numberPickerQueens.value),
            CardItem("RULE_K",0, inputKing.text?.toString(), numberPickerKings.value)
        )
    }

    private fun initPreferences(){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        isDarkModeFeatureEnabled = sharedPreferences.getBoolean(PREFERENCE_FEATURE_DARK_MODE, false)
    }

    private fun initNumberPickers(){
        val numberPickerAces = this.findViewById<NumberPicker>(R.id.numberPickerAces)
        numberPickerAces.minValue = 1
        numberPickerAces.maxValue = 60

        val numberPickerTwos = this.findViewById<NumberPicker>(R.id.numberPickerTwos)
        numberPickerTwos.minValue = 1
        numberPickerTwos.maxValue = 60

        val numberPickerThrees = this.findViewById<NumberPicker>(R.id.numberPickerThrees)
        numberPickerThrees.minValue = 1
        numberPickerThrees.maxValue = 60

        val numberPickerFours = this.findViewById<NumberPicker>(R.id.numberPickerFours)
        numberPickerFours.minValue = 1
        numberPickerFours.maxValue = 60

        val numberPickerFives = this.findViewById<NumberPicker>(R.id.numberPickerFives)
        numberPickerFives.minValue = 1
        numberPickerFives.maxValue = 60

        val numberPickerSixes = this.findViewById<NumberPicker>(R.id.numberPickerSixes)
        numberPickerSixes.minValue = 1
        numberPickerSixes.maxValue = 60

        val numberPickerSevens = this.findViewById<NumberPicker>(R.id.numberPickerSevens)
        numberPickerSevens.minValue = 1
        numberPickerSevens.maxValue = 60

        val numberPickerEights = this.findViewById<NumberPicker>(R.id.numberPickerEights)
        numberPickerEights.minValue = 1
        numberPickerEights.maxValue = 60

        val numberPickerNines = this.findViewById<NumberPicker>(R.id.numberPickerNines)
        numberPickerNines.minValue = 1
        numberPickerNines.maxValue = 60

        val numberPickerTens = this.findViewById<NumberPicker>(R.id.numberPickerTens)
        numberPickerTens.minValue = 1
        numberPickerTens.maxValue = 60

        val numberPickerJacks = this.findViewById<NumberPicker>(R.id.numberPickerJacks)
        numberPickerJacks.minValue = 1
        numberPickerJacks.maxValue = 60

        val numberPickerQueens = this.findViewById<NumberPicker>(R.id.numberPickerQueens)
        numberPickerQueens.minValue = 1
        numberPickerQueens.maxValue = 60

        val numberPickerKings = this.findViewById<NumberPicker>(R.id.numberPickerKings)
        numberPickerKings.minValue = 1
        numberPickerKings.maxValue = 60
    }
}
