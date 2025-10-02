package com.example.kungfupenguin

import android.graphics.Color
import androidx.core.graphics.toColorInt
import com.example.kungfupenguin.Card

class Player {

    private var name: String
    private var penguinColor: String
    private lateinit var hand: MutableList<Card>

    private var score: Int = 0


    constructor(name: String, color: String){
        this.name = name
        this.hand = mutableListOf()
        this.penguinColor = color
        for ( x in 0..4)
            this.drawCard()

    }

    fun getName(): String{
        return name
    }

    fun getHand(): List<Card> {
        return hand
    }

    fun getColor(): String {
        return penguinColor
    }

    fun getScore(): Int {
        return this.score
    }


    fun playCard(index: Int): Card? {
        return  if (index == -1) null else hand.removeAt(index)
    }

    fun drawCard() {
        if (hand.size < 5) {

            val newCard = Card.entries.random() // random card type
            hand.add(newCard)
        }
    }

    fun incrementScore() {
        score++
    }
}
