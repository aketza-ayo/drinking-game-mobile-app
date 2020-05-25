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
            CardItem("1C",R.drawable.ic_1c_small,context.resources.getString(R.string.rule_1)),
            CardItem("1D",R.drawable.ic_1d_small,context.resources.getString(R.string.rule_1)),
            CardItem("1H",R.drawable.ic_1h_small,context.resources.getString(R.string.rule_1)),
            CardItem("1S",R.drawable.ic_1s_small,context.resources.getString(R.string.rule_1)),
            CardItem("2C",R.drawable.ic_2c_small,context.resources.getString(R.string.rule_2)),
            CardItem("2D",R.drawable.ic_2d_small,context.resources.getString(R.string.rule_2)),
            CardItem("2H",R.drawable.ic_2h_small,context.resources.getString(R.string.rule_2)),
            CardItem("2S",R.drawable.ic_2s_small,context.resources.getString(R.string.rule_2)),
            CardItem("3C",R.drawable.ic_3c_small,context.resources.getString(R.string.rule_3)),
            CardItem("3D",R.drawable.ic_3d_small,context.resources.getString(R.string.rule_3)),
            CardItem("3H",R.drawable.ic_3h_small,context.resources.getString(R.string.rule_3)),
            CardItem("3S",R.drawable.ic_3s_small,context.resources.getString(R.string.rule_3)),
            CardItem("4C",R.drawable.ic_4c_small,context.resources.getString(R.string.rule_4)),
            CardItem("4D",R.drawable.ic_4d_small,context.resources.getString(R.string.rule_4)),
            CardItem("4H",R.drawable.ic_4h_small,context.resources.getString(R.string.rule_4)),
            CardItem("4S",R.drawable.ic_4s_small,context.resources.getString(R.string.rule_4)),
            CardItem("5C",R.drawable.ic_5c_small,context.resources.getString(R.string.rule_5)),
            CardItem("5D",R.drawable.ic_5d_small,context.resources.getString(R.string.rule_5)),
            CardItem("5H",R.drawable.ic_5h_small,context.resources.getString(R.string.rule_5)),
            CardItem("5S",R.drawable.ic_5s_small,context.resources.getString(R.string.rule_5)),
            CardItem("6C",R.drawable.ic_6c_small,context.resources.getString(R.string.rule_6)),
            CardItem("6D",R.drawable.ic_6d_small,context.resources.getString(R.string.rule_6)),
            CardItem("6H",R.drawable.ic_6h_small,context.resources.getString(R.string.rule_6)),
            CardItem("6S",R.drawable.ic_6s_small,context.resources.getString(R.string.rule_6)),
            CardItem("7C",R.drawable.ic_7c_small,context.resources.getString(R.string.rule_7)),
            CardItem("7D",R.drawable.ic_7d_small,context.resources.getString(R.string.rule_7)),
            CardItem("7H",R.drawable.ic_7h_small,context.resources.getString(R.string.rule_7)),
            CardItem("7S",R.drawable.ic_7s_small,context.resources.getString(R.string.rule_7)),
            CardItem("8C",R.drawable.ic_8c_small,context.resources.getString(R.string.rule_8)),
            CardItem("8D",R.drawable.ic_8d_small,context.resources.getString(R.string.rule_8)),
            CardItem("8H",R.drawable.ic_8h_small,context.resources.getString(R.string.rule_8)),
            CardItem("8S",R.drawable.ic_8s_small,context.resources.getString(R.string.rule_8)),
            CardItem("9C",R.drawable.ic_9c_small,context.resources.getString(R.string.rule_9)),
            CardItem("9D",R.drawable.ic_9d_small,context.resources.getString(R.string.rule_9)),
            CardItem("9H",R.drawable.ic_9h_small,context.resources.getString(R.string.rule_9)),
            CardItem("9S",R.drawable.ic_9s_small,context.resources.getString(R.string.rule_9)),
            CardItem("10C",R.drawable.ic_10c_small,context.resources.getString(R.string.rule_10)),
            CardItem("10D",R.drawable.ic_10d_small,context.resources.getString(R.string.rule_10)),
            CardItem("10H",R.drawable.ic_10h_small,context.resources.getString(R.string.rule_10)),
            CardItem("10S",R.drawable.ic_10s_small,context.resources.getString(R.string.rule_10)),
            CardItem("JC",R.drawable.ic_jc_small,context.resources.getString(R.string.rule_J)),
            CardItem("JD",R.drawable.ic_jd_small,context.resources.getString(R.string.rule_J)),
            CardItem("JH",R.drawable.ic_jh_small,context.resources.getString(R.string.rule_J)),
            CardItem("JS",R.drawable.ic_js_small,context.resources.getString(R.string.rule_J)),
            CardItem("QC",R.drawable.ic_qc_small,context.resources.getString(R.string.rule_Q)),
            CardItem("QD",R.drawable.ic_qd_small,context.resources.getString(R.string.rule_Q)),
            CardItem("QH",R.drawable.ic_qh_small,context.resources.getString(R.string.rule_Q)),
            CardItem("QS",R.drawable.ic_qs_small,context.resources.getString(R.string.rule_Q)),
            CardItem("KC",R.drawable.ic_kc_small,context.resources.getString(R.string.rule_K)),
            CardItem("KD",R.drawable.ic_kd_small,context.resources.getString(R.string.rule_K)),
            CardItem("KH",R.drawable.ic_kh_small,context.resources.getString(R.string.rule_K)),
            CardItem("KS",R.drawable.ic_ks_small,context.resources.getString(R.string.rule_K))
            ) as MutableSet<CardItem>
    }

    public fun getDefaultRule(cardItem: CardItem, context: Context): String{
        val map = getAllCards(context).map {it.name to it.rule  }.toMap()

        return map[cardItem.name].toString()
    }
}