package com.devapp.drinkinggame

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*


class DatabaseHelper: SQLiteOpenHelper{

    companion object {
        const val CUSTOM_MESSAGE = "n/a"

        const val DATABASE_NAME = "drinkinggame.db"
        const val TABLE_NAME = "rules"
        const val COL1 = "ID"
        const val COL2 = "REF"
        const val COL3 = "NAME"
        const val COL4 = "TIME"

        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            if(instance == null) {
                instance = DatabaseHelper(context)
            }

            return instance!!
        }

    }

    private constructor(context: Context?) :  super(context, DATABASE_NAME, null, 1)

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())


    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
            "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " REF TEXT," +
                    " NAME TEXT," +
                    " TIME TEXT);"
        db.execSQL(createTable)
        insertCustomRules(db)
    }

    private fun insertCustomRules(db: SQLiteDatabase){
        for (x in 1..10){
            val contentValues = ContentValues()
            contentValues.put(COL2, "RULE_$x")
            contentValues.put(COL3, CUSTOM_MESSAGE)
            contentValues.put(COL4, dateFormat.format(Date()).toString())
            db.insert(TABLE_NAME, null, contentValues)
        }

        val contentValuesJ = ContentValues()
        contentValuesJ.put(COL2, "RULE_J")
        contentValuesJ.put(COL3, CUSTOM_MESSAGE)
        contentValuesJ.put(COL4, dateFormat.format(Date()).toString())
        db.insert(TABLE_NAME, null, contentValuesJ)

        val contentValuesQ = ContentValues()
        contentValuesQ.put(COL2, "RULE_Q")
        contentValuesQ.put(COL3, CUSTOM_MESSAGE)
        contentValuesQ.put(COL4, dateFormat.format(Date()).toString())
        db.insert(TABLE_NAME, null, contentValuesQ)

        val contentValuesK = ContentValues()
        contentValuesK.put(COL2, "RULE_K")
        contentValuesK.put(COL3, CUSTOM_MESSAGE)
        contentValuesK.put(COL4, dateFormat.format(Date()).toString())
        db.insert(TABLE_NAME, null, contentValuesK)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db)
    }

    fun updateRule(ref: String, rule: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL3, rule)
        contentValues.put(COL4, dateFormat.format(Date()).toString())
        return db.update(TABLE_NAME, contentValues, "REF = ?", arrayOf(ref))

    }

    fun getCustomRules(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
    }

    private fun deleteAllElements(){
        val db = this.writableDatabase
        db.rawQuery("DELETE FROM " + TABLE_NAME, null)
//        db.rawQuery("DELETE FROM SQLITE_SEQUENCE WHERE name = $TABLE_NAME", null)
    }

}