package com.devapp.drinkinggame

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
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
            for(e in getTextFieldValues()){
                database.updateRule(e.first, e.second.text.toString())
            }
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Unable to save default rules", Toast.LENGTH_SHORT).show()
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
            textRuleMode.text = "  Playing with <custom> rules"
        }else{
            textRuleMode.text = "  Playing with <default> rules"
        }

        initToolbar()
        initSpinner()

        /**
         * default rules are populated into the fields onItemSelected() when the activity first loads
         */

        addCloseArrowIconInToolbar()
        replaceTitleFromToolbar()
    }

    private fun initSpinner(){

        spinner = findViewById<Spinner>(R.id.spinnerRules)

        val itemsAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.spinnerNames))
        itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(itemsAdapter)
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
                    "RULE_1"-> inputAce.setText(customRules.getString(2))
                    "RULE_2"-> inputTwo.setText(customRules.getString(2))
                    "RULE_3"-> inputThree.setText(customRules.getString(2))
                    "RULE_4"-> inputFour.setText(customRules.getString(2))
                    "RULE_5"-> inputFive.setText(customRules.getString(2))
                    "RULE_6"-> inputSix.setText(customRules.getString(2))
                    "RULE_7"-> inputSeven.setText(customRules.getString(2))
                    "RULE_8"-> inputEight.setText(customRules.getString(2))
                    "RULE_9"-> inputNine.setText(customRules.getString(2))
                    "RULE_10"-> inputTen.setText(customRules.getString(2))
                    "RULE_J"-> inputJack.setText(customRules.getString(2))
                    "RULE_Q"-> inputQueen.setText(customRules.getString(2))
                    "RULE_K"-> inputKing.setText(customRules.getString(2))
                    else ->  Toast.makeText(this, "Oops!", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            clearDefaulFields()
        }
    }

    private fun clearDefaulFields(){
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
        val allCardsSet = CardsData().getAllCards()
        var allCardsMap = allCardsSet.map { it.name to it.rule }.toMap()

        inputAce.setText(allCardsMap.get("1C"))
        inputTwo.setText(allCardsMap.get("2C"))
        inputThree.setText(allCardsMap.get("3C"))
        inputFour.setText(allCardsMap.get("4C"))
        inputFive.setText(allCardsMap.get("5C"))
        inputSix.setText(allCardsMap.get("6C"))
        inputSeven.setText(allCardsMap.get("7C"))
        inputEight.setText(allCardsMap.get("8C"))
        inputNine.setText(allCardsMap.get("9C"))
        inputTen.setText(allCardsMap.get("10C"))
        inputJack.setText(allCardsMap.get("JC"))
        inputQueen.setText(allCardsMap.get("QC"))
        inputKing.setText(allCardsMap.get("KC"))
    }

    private fun getTextFieldValues() : List<Pair<String,TextInputEditText>>{
        return listOf(
            Pair("RULE_1",inputAce),Pair("RULE_2",inputTwo),Pair("RULE_3",inputThree), Pair("RULE_4",inputFour),
            Pair("RULE_5",inputFive), Pair("RULE_6",inputSix), Pair("RULE_7",inputSeven),Pair("RULE_8",inputEight),
            Pair("RULE_9",inputNine), Pair("RULE_10",inputTen), Pair("RULE_J",inputJack),Pair("RULE_Q",inputQueen),
            Pair("RULE_K",inputKing
            )
        )
    }

    private fun initPreferences(){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        isDarkModeFeatureEnabled = sharedPreferences.getBoolean(PREFERENCE_FEATURE_DARK_MODE, false)
    }
}
