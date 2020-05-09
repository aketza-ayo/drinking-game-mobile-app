package com.devapp.drinkinggame

import android.util.Log

class CardsData {

    companion object {
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

    fun getAllCards(): MutableSet<Card>{
        Log.d("drinking-game", "Deck of cards loaded")

        return setOf(
            Card("1C","ac.png",RULE_1),
            Card("1D","ad.png",RULE_1),
            Card("1H","ah.png",RULE_1),
            Card("1S","as.png",RULE_1),
            Card("2C","twoc.png",RULE_2),
            Card("2D","twod.png",RULE_2),
            Card("2H","twoh.png",RULE_2),
            Card("2S","twos.png",RULE_2),
            Card("3C","threec.png",RULE_3),
            Card("3D","threed.png",RULE_3),
            Card("3H","threeh.png",RULE_3),
            Card("3S","threes.png",RULE_3),
            Card("4C","fourc.png",RULE_4),
            Card("4D","fourd.png",RULE_4),
            Card("4H","fourh.png",RULE_4),
            Card("4S","fours.png",RULE_4),
            Card("5C","fivec.png",RULE_5),
            Card("5D","fived.png",RULE_5),
            Card("5H","fiveh.png",RULE_5),
            Card("5S","fives.png",RULE_5),
            Card("6C","sixc.png",RULE_6),
            Card("6D","sixd.png",RULE_6),
            Card("6H","sixh.png",RULE_6),
            Card("6S","sixs.png",RULE_6),
            Card("7C","sevenc.png",RULE_7),
            Card("7D","sevend.png",RULE_7),
            Card("7H","sevenh.png",RULE_7),
            Card("7S","sevens.png",RULE_7),
            Card("8C","eightc.png",RULE_8),
            Card("8D","eightd.png",RULE_8),
            Card("8H","eighth.png",RULE_8),
            Card("8S","eights.png",RULE_8),
            Card("9C","ninec.png",RULE_9),
            Card("9D","nined.png",RULE_9),
            Card("9H","nineh.png",RULE_9),
            Card("9S","nines.png",RULE_9),
            Card("10C","tenc.png",RULE_10),
            Card("10D","tend.png",RULE_10),
            Card("10H","tenh.png",RULE_10),
            Card("10S","tens.png",RULE_10),
            Card("JC","jc.png",RULE_J),
            Card("JD","jd.png",RULE_J),
            Card("JH","jh.png",RULE_J),
            Card("JS","js.png",RULE_J),
            Card("QC","qc.png",RULE_Q),
            Card("QD","qd.png",RULE_Q),
            Card("QH","qh.png",RULE_Q),
            Card("QS","qs.png",RULE_Q),
            Card("KC","kc.png",RULE_K),
            Card("KD","kd.png",RULE_K),
            Card("KH","kh.png",RULE_K),
            Card("KS","ks.png",RULE_K)
            ) as MutableSet<Card>
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