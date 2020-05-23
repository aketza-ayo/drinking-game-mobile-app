package com.devapp.drinkinggame

import android.util.Log

class CardsData {

    companion object {
        val TAG: String = CardsData::class.java.simpleName

        const val RULE_1 = "You assign one drink."
        const val RULE_2 = "You assign two drinks."
        const val RULE_3 = "You assign three drinks."
        const val RULE_4 = "You assign four drinks."
        const val RULE_5 = "The one on your left drinks."
        const val RULE_6 = "The one on your right drinks."
        const val RULE_7 = "Rule of the thumb. Keep that in mind."
        const val RULE_8 = "All girls drink!"
        const val RULE_9 = "Men drink!"
        const val RULE_10 = "Make up a rule."
        const val RULE_J = "Sing a song with category, caricachupas..."
        const val RULE_Q = "You have a shield - Choose a person."
        const val RULE_K = "Down it!"

    }

    fun getAllCards(): MutableSet<CardItem>{
        Log.d(Constants.APP_NAME, "Deck of cards loaded from $TAG")

        return setOf(
            CardItem("1C",R.drawable.ic_1c_small,RULE_1),
            CardItem("1D",R.drawable.ic_1d_small,RULE_1),
            CardItem("1H",R.drawable.ic_1h_small,RULE_1),
            CardItem("1S",R.drawable.ic_1s_small,RULE_1),
            CardItem("2C",R.drawable.ic_2c_small,RULE_2),
            CardItem("2D",R.drawable.ic_2d_small,RULE_2),
            CardItem("2H",R.drawable.ic_2h_small,RULE_2),
            CardItem("2S",R.drawable.ic_2s_small,RULE_2),
            CardItem("3C",R.drawable.ic_3c_small,RULE_3),
            CardItem("3D",R.drawable.ic_3d_small,RULE_3),
            CardItem("3H",R.drawable.ic_3h_small,RULE_3),
            CardItem("3S",R.drawable.ic_3s_small,RULE_3),
            CardItem("4C",R.drawable.ic_4c_small,RULE_4),
            CardItem("4D",R.drawable.ic_4d_small,RULE_4),
            CardItem("4H",R.drawable.ic_4h_small,RULE_4),
            CardItem("4S",R.drawable.ic_4s_small,RULE_4),
            CardItem("5C",R.drawable.ic_5c_small,RULE_5),
            CardItem("5D",R.drawable.ic_5d_small,RULE_5),
            CardItem("5H",R.drawable.ic_5h_small,RULE_5),
            CardItem("5S",R.drawable.ic_5s_small,RULE_5),
            CardItem("6C",R.drawable.ic_6c_small,RULE_6),
            CardItem("6D",R.drawable.ic_6d_small,RULE_6),
            CardItem("6H",R.drawable.ic_6h_small,RULE_6),
            CardItem("6S",R.drawable.ic_6s_small,RULE_6),
            CardItem("7C",R.drawable.ic_7c_small,RULE_7),
            CardItem("7D",R.drawable.ic_7d_small,RULE_7),
            CardItem("7H",R.drawable.ic_7h_small,RULE_7),
            CardItem("7S",R.drawable.ic_7s_small,RULE_7),
            CardItem("8C",R.drawable.ic_8c_small,RULE_8),
            CardItem("8D",R.drawable.ic_8d_small,RULE_8),
            CardItem("8H",R.drawable.ic_8h_small,RULE_8),
            CardItem("8S",R.drawable.ic_8s_small,RULE_8),
            CardItem("9C",R.drawable.ic_9c_small,RULE_9),
            CardItem("9D",R.drawable.ic_9d_small,RULE_9),
            CardItem("9H",R.drawable.ic_9h_small,RULE_9),
            CardItem("9S",R.drawable.ic_9s_small,RULE_9),
            CardItem("10C",R.drawable.ic_10c_small,RULE_10),
            CardItem("10D",R.drawable.ic_10d_small,RULE_10),
            CardItem("10H",R.drawable.ic_10h_small,RULE_10),
            CardItem("10S",R.drawable.ic_10s_small,RULE_10),
            CardItem("JC",R.drawable.ic_jc_small,RULE_J),
            CardItem("JD",R.drawable.ic_jd_small,RULE_J),
            CardItem("JH",R.drawable.ic_jh_small,RULE_J),
            CardItem("JS",R.drawable.ic_js_small,RULE_J),
            CardItem("QC",R.drawable.ic_qc_small,RULE_Q),
            CardItem("QD",R.drawable.ic_qd_small,RULE_Q),
            CardItem("QH",R.drawable.ic_qh_small,RULE_Q),
            CardItem("QS",R.drawable.ic_qs_small,RULE_Q),
            CardItem("KC",R.drawable.ic_kc_small,RULE_K),
            CardItem("KD",R.drawable.ic_kd_small,RULE_K),
            CardItem("KH",R.drawable.ic_kh_small,RULE_K),
            CardItem("KS",R.drawable.ic_ks_small,RULE_K)
            ) as MutableSet<CardItem>
    }

    fun getDefaultRules(): List<Pair<String, String>> {
        return listOf(
            Pair("RULE_1",RULE_1),
            Pair("RULE_2",RULE_2),
            Pair("RULE_3",RULE_3),
            Pair("RULE_4",RULE_4),
            Pair("RULE_5",RULE_5),
            Pair("RULE_6",RULE_6),
            Pair("RULE_7",RULE_7),
            Pair("RULE_8",RULE_8),
            Pair("RULE_9",RULE_9),
            Pair("RULE_10",RULE_10),
            Pair("RULE_J",RULE_J),
            Pair("RULE_Q",RULE_Q),
            Pair("RULE_K",RULE_K)
        ) as MutableList<Pair<String,String>>
    }
}