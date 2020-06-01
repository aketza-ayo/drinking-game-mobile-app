package com.devapp.drinkinggame

import android.content.Context
import android.util.Log

class CardsData {

    companion object {
        val TAG: String = CardsData::class.java.simpleName
    }

    fun getAllCards(context: Context): MutableSet<CardItem>{
        Log.d(Constants.APP_NAME, "Deck of cards loaded from $TAG")

        return setOf(
            CardItem("AC",R.drawable.ic_1c_small,context.resources.getString(R.string.rule_1),5),
            CardItem("AD",R.drawable.ic_1d_small,context.resources.getString(R.string.rule_1),5),
            CardItem("AH",R.drawable.ic_1h_small,context.resources.getString(R.string.rule_1),5),
            CardItem("AS",R.drawable.ic_1s_small,context.resources.getString(R.string.rule_1),5),
            CardItem("2C",R.drawable.ic_2c_small,context.resources.getString(R.string.rule_2),7),
            CardItem("2D",R.drawable.ic_2d_small,context.resources.getString(R.string.rule_2),7),
            CardItem("2H",R.drawable.ic_2h_small,context.resources.getString(R.string.rule_2),7),
            CardItem("2S",R.drawable.ic_2s_small,context.resources.getString(R.string.rule_2),7),
            CardItem("3C",R.drawable.ic_3c_small,context.resources.getString(R.string.rule_3),10),
            CardItem("3D",R.drawable.ic_3d_small,context.resources.getString(R.string.rule_3),10),
            CardItem("3H",R.drawable.ic_3h_small,context.resources.getString(R.string.rule_3),10),
            CardItem("3S",R.drawable.ic_3s_small,context.resources.getString(R.string.rule_3),10),
            CardItem("4C",R.drawable.ic_4c_small,context.resources.getString(R.string.rule_4),15),
            CardItem("4D",R.drawable.ic_4d_small,context.resources.getString(R.string.rule_4),15),
            CardItem("4H",R.drawable.ic_4h_small,context.resources.getString(R.string.rule_4),15),
            CardItem("4S",R.drawable.ic_4s_small,context.resources.getString(R.string.rule_4),15),
            CardItem("5C",R.drawable.ic_5c_small,context.resources.getString(R.string.rule_5),7),
            CardItem("5D",R.drawable.ic_5d_small,context.resources.getString(R.string.rule_5),7),
            CardItem("5H",R.drawable.ic_5h_small,context.resources.getString(R.string.rule_5),7),
            CardItem("5S",R.drawable.ic_5s_small,context.resources.getString(R.string.rule_5),7),
            CardItem("6C",R.drawable.ic_6c_small,context.resources.getString(R.string.rule_6),7),
            CardItem("6D",R.drawable.ic_6d_small,context.resources.getString(R.string.rule_6),7),
            CardItem("6H",R.drawable.ic_6h_small,context.resources.getString(R.string.rule_6),7),
            CardItem("6S",R.drawable.ic_6s_small,context.resources.getString(R.string.rule_6),7),
            CardItem("7C",R.drawable.ic_7c_small,context.resources.getString(R.string.rule_7),5),
            CardItem("7D",R.drawable.ic_7d_small,context.resources.getString(R.string.rule_7),5),
            CardItem("7H",R.drawable.ic_7h_small,context.resources.getString(R.string.rule_7),5),
            CardItem("7S",R.drawable.ic_7s_small,context.resources.getString(R.string.rule_7),5),
            CardItem("8C",R.drawable.ic_8c_small,context.resources.getString(R.string.rule_8),7),
            CardItem("8D",R.drawable.ic_8d_small,context.resources.getString(R.string.rule_8),7),
            CardItem("8H",R.drawable.ic_8h_small,context.resources.getString(R.string.rule_8),7),
            CardItem("8S",R.drawable.ic_8s_small,context.resources.getString(R.string.rule_8),7),
            CardItem("9C",R.drawable.ic_9c_small,context.resources.getString(R.string.rule_9),7),
            CardItem("9D",R.drawable.ic_9d_small,context.resources.getString(R.string.rule_9),7),
            CardItem("9H",R.drawable.ic_9h_small,context.resources.getString(R.string.rule_9),7),
            CardItem("9S",R.drawable.ic_9s_small,context.resources.getString(R.string.rule_9),7),
            CardItem("10C",R.drawable.ic_10c_small,context.resources.getString(R.string.rule_10),20),
            CardItem("10D",R.drawable.ic_10d_small,context.resources.getString(R.string.rule_10),20),
            CardItem("10H",R.drawable.ic_10h_small,context.resources.getString(R.string.rule_10),20),
            CardItem("10S",R.drawable.ic_10s_small,context.resources.getString(R.string.rule_10),20),
            CardItem("JC",R.drawable.ic_jc_small,context.resources.getString(R.string.rule_J),40),
            CardItem("JD",R.drawable.ic_jd_small,context.resources.getString(R.string.rule_J),40),
            CardItem("JH",R.drawable.ic_jh_small,context.resources.getString(R.string.rule_J),40),
            CardItem("JS",R.drawable.ic_js_small,context.resources.getString(R.string.rule_J),40),
            CardItem("QC",R.drawable.ic_qc_small,context.resources.getString(R.string.rule_Q),10),
            CardItem("QD",R.drawable.ic_qd_small,context.resources.getString(R.string.rule_Q),10),
            CardItem("QH",R.drawable.ic_qh_small,context.resources.getString(R.string.rule_Q),10),
            CardItem("QS",R.drawable.ic_qs_small,context.resources.getString(R.string.rule_Q),10),
            CardItem("KC",R.drawable.ic_kc_small,context.resources.getString(R.string.rule_K),20),
            CardItem("KD",R.drawable.ic_kd_small,context.resources.getString(R.string.rule_K),20),
            CardItem("KH",R.drawable.ic_kh_small,context.resources.getString(R.string.rule_K),20),
            CardItem("KS",R.drawable.ic_ks_small,context.resources.getString(R.string.rule_K),20)
            ) as MutableSet<CardItem>
    }

    public fun getImageResourceDrawable(cardItem: CardItem): Int {
        return when (cardItem.name) {
            "AC" -> R.drawable.ac
            "AD" -> R.drawable.ad
            "AH" -> R.drawable.ah
            "AS" -> R.drawable.`as`
            "2C" -> R.drawable.twoc
            "2D" -> R.drawable.twod
            "2H" -> R.drawable.twoh
            "2S" -> R.drawable.twos
            "3C" -> R.drawable.threec
            "3D" -> R.drawable.threed
            "3H" -> R.drawable.threeh
            "3S" -> R.drawable.threes
            "4C" -> R.drawable.fourc
            "4D" -> R.drawable.fourd
            "4H" -> R.drawable.fourh
            "4S" -> R.drawable.fours
            "5C" -> R.drawable.fivec
            "5D" -> R.drawable.fived
            "5H" -> R.drawable.fiveh
            "5S" -> R.drawable.fives
            "6C" -> R.drawable.sixc
            "6D" -> R.drawable.sixd
            "6H" -> R.drawable.sixh
            "6S" -> R.drawable.sixs
            "7C" -> R.drawable.sevenc
            "7D" -> R.drawable.sevend
            "7H" -> R.drawable.sevenh
            "7S" -> R.drawable.sevens
            "8C" -> R.drawable.eightc
            "8D" -> R.drawable.eightd
            "8H" -> R.drawable.eighth
            "8S" -> R.drawable.eights
            "9C" -> R.drawable.ninec
            "9D" -> R.drawable.nined
            "9H" -> R.drawable.nineh
            "9S" -> R.drawable.nines
            "10C" -> R.drawable.tenc
            "10D" -> R.drawable.tend
            "10H" -> R.drawable.tenh
            "10S" -> R.drawable.tens
            "JC" -> R.drawable.jc
            "JD" -> R.drawable.jd
            "JH" -> R.drawable.jh
            "JS" -> R.drawable.js
            "QC" -> R.drawable.qc
            "QD" -> R.drawable.qd
            "QH" -> R.drawable.qh
            "QS" -> R.drawable.qs
            "KC" -> R.drawable.kc
            "KD" -> R.drawable.kd
            "KH" -> R.drawable.kh
            "KS" -> R.drawable.ks

            else -> R.drawable.card_back_blue
        }
    }

    public fun getDefaultRule(cardItem: CardItem, context: Context): String{
        val map = getAllCards(context).map {it.name to it.rule  }.toMap()

        return map[cardItem.name].toString()
    }

    public fun getDefaultDuration(cardItem: CardItem, context: Context): Int{
        val map = getAllCards(context).map {it.name to it.value  }.toMap()

        return map[cardItem.name]!!
    }
}