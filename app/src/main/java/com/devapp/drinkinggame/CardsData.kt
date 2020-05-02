package com.devapp.drinkinggame

import android.util.Log

class CardsData {

    companion object {
        const val ASSIGN_ONE_DRINK_RULE_1 = "You assign one drink."
        const val ASSIGN_TWO_DRINK_RULE_2 = "You assign two drinks."
        const val ASSIGN_THREE_DRINK_RULE_3 = "You assign three drinks."
        const val ASSIGN_FOUR_DRINK_RULE_4 = "You assign four drinks."
        const val LEFT_PERSON_RULE_5 = "The one on your left drinks."
        const val RIGHT_PERSON_RULE_6 = "The one on your right drinks."
        const val THUMB_RULE_7 = "Rule of the thumb. Keep that in mind."
        const val GIRLS_DRINK_RULE_8 = "All girls drink!"
        const val MEN_DRINK_RULE_9 = "Men drink!"
        const val MAKE_UP_RULE_10 = "Make up a rule."
        const val SING_A_SONG_RULE_J = "Sing a song with category, caricachupas..."
        const val SHIELD_RULE_Q = "You have a shield - Choose a person."
        const val DOWN_IT_RULE_K = "Down it!"

    }

    fun getAllCards(): MutableSet<Card>{
        Log.d("drinking-game", "Deck of cards loaded")

        return setOf(
            Card("AC","ac.png",ASSIGN_ONE_DRINK_RULE_1),
            Card("AD","ad.png",ASSIGN_ONE_DRINK_RULE_1),
            Card("AH","ah.png",ASSIGN_ONE_DRINK_RULE_1),
            Card("AS","as.png",ASSIGN_ONE_DRINK_RULE_1),
            Card("2C","twoc.png",ASSIGN_TWO_DRINK_RULE_2),
            Card("2D","twod.png",ASSIGN_TWO_DRINK_RULE_2),
            Card("2H","twoh.png",ASSIGN_TWO_DRINK_RULE_2),
            Card("2S","twos.png",ASSIGN_TWO_DRINK_RULE_2),
            Card("3C","threec.png",ASSIGN_THREE_DRINK_RULE_3),
            Card("3D","threed.png",ASSIGN_THREE_DRINK_RULE_3),
            Card("3H","threeh.png",ASSIGN_THREE_DRINK_RULE_3),
            Card("3S","threes.png",ASSIGN_THREE_DRINK_RULE_3),
            Card("4C","fourc.png",ASSIGN_FOUR_DRINK_RULE_4),
            Card("4D","fourd.png",ASSIGN_FOUR_DRINK_RULE_4),
            Card("4H","fourh.png",ASSIGN_FOUR_DRINK_RULE_4),
            Card("4S","fours.png",ASSIGN_FOUR_DRINK_RULE_4),
            Card("5C","fivec.png",LEFT_PERSON_RULE_5),
            Card("5D","fived.png",LEFT_PERSON_RULE_5),
            Card("5H","fiveh.png",LEFT_PERSON_RULE_5),
            Card("5S","fives.png",LEFT_PERSON_RULE_5),
            Card("6C","sixc.png",RIGHT_PERSON_RULE_6),
            Card("6D","sixd.png",RIGHT_PERSON_RULE_6),
            Card("6H","sixh.png",RIGHT_PERSON_RULE_6),
            Card("6S","sixs.png",RIGHT_PERSON_RULE_6),
            Card("7C","sevenc.png",THUMB_RULE_7),
            Card("7D","sevend.png",THUMB_RULE_7),
            Card("7H","sevenh.png",THUMB_RULE_7),
            Card("7S","sevens.png",THUMB_RULE_7),
            Card("8C","eightc.png",GIRLS_DRINK_RULE_8),
            Card("8D","eightd.png",GIRLS_DRINK_RULE_8),
            Card("8H","eighth.png",GIRLS_DRINK_RULE_8),
            Card("8S","eights.png",GIRLS_DRINK_RULE_8),
            Card("9C","ninec.png",MEN_DRINK_RULE_9),
            Card("9D","nined.png",MEN_DRINK_RULE_9),
            Card("9H","nineh.png",MEN_DRINK_RULE_9),
            Card("9S","nines.png",MEN_DRINK_RULE_9),
            Card("10C","tenc.png",MAKE_UP_RULE_10),
            Card("10D","tend.png",MAKE_UP_RULE_10),
            Card("10H","tenh.png",MAKE_UP_RULE_10),
            Card("10S","tens.png",MAKE_UP_RULE_10),
            Card("JC","jc.png",SING_A_SONG_RULE_J),
            Card("JD","jd.png",SING_A_SONG_RULE_J),
            Card("JH","jh.png",SING_A_SONG_RULE_J),
            Card("JS","js.png",SING_A_SONG_RULE_J),
            Card("QC","qc.png",SHIELD_RULE_Q),
            Card("QD","qd.png",SHIELD_RULE_Q),
            Card("QH","qh.png",SHIELD_RULE_Q),
            Card("QS","qs.png",SHIELD_RULE_Q),
            Card("KC","kc.png",DOWN_IT_RULE_K),
            Card("KD","kd.png",DOWN_IT_RULE_K),
            Card("KH","kh.png",DOWN_IT_RULE_K),
            Card("KS","ks.png",DOWN_IT_RULE_K)
            ) as MutableSet<Card>
    }
}