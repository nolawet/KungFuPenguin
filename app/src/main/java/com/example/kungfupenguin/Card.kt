package com.example.kungfupenguin

enum class Card {
    FIRE, WATER, SNOW;


    fun beats(other: Card): Int {
        if ((this == FIRE && other == SNOW) ||
                (this == WATER && other == FIRE) ||
                (this == SNOW && other == WATER)) {
            return 1
        } else if ((this == FIRE && other == FIRE) ||
            (this == WATER && other == WATER) ||
            (this == SNOW && other == SNOW)) {
            return 0
        } else {
            return -1
        }
    }


    fun getCard(): String {

        return when (this) {
            FIRE -> "🔥 FIRE"
            WATER -> "💧 WATER"
            SNOW -> "❄️ SNOW"
        }
    }
}
