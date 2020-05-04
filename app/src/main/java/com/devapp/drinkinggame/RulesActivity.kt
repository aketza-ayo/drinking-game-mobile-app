package com.devapp.drinkinggame

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_rules.*


class RulesActivity : AppCompatActivity() {

    var defaultCustomeMode = true

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

        initToolbar()
        initSpinner()

//        populateRulesTextFieldsIfDefaultMode()  //this method is automatically called from onItemSelected() when the activity first loads

        addCloseIconInToolbar()
        replaceTitleFromToolbar()
    }

    private fun initSpinner(){
        var spinner = findViewById<Spinner>(R.id.spinnerRules)

        val itemsAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.spinnerNames))
        itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(itemsAdapter)

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("to be implemented if needed")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //0 Default (true) - 1 Custom (false)

                if(position == 0){
                    defaultCustomeMode = true
                    populateRulesTextFieldsIfDefaultMode()
                }else{
                    defaultCustomeMode = false
                    clearAllTextFields()
                }
            }
        }
    }

    private fun initToolbar(){
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun addCloseIconInToolbar(){
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)

    }

    private fun replaceTitleFromToolbar(){
        this.title = "Rules Management"
    }

    private fun clearAllTextFields(){
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

    private fun populateRulesTextFieldsIfDefaultMode(){

        if(defaultCustomeMode) {
            var inputAce = findViewById<TextInputEditText>(R.id.inputAce)
            var inputTwo = findViewById<TextInputEditText>(R.id.inputTwo)

            val allCardsSet = CardsData().getAllCards()
            var allCardsMap = allCardsSet.map { it.name to it.tooltip }.toMap()

            inputAce.setText(allCardsMap.get("AC"))
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
    }
}
