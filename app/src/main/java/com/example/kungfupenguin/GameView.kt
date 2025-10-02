package com.example.project

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.kungfupenguin.Card
import com.example.kungfupenguin.Player
import com.example.kungfupenguin.R

class GameView : LinearLayout {

    var blueColor: Int = Color.parseColor("#0557a3")


    private var player1: Player
    private var player2: Player

    lateinit var  player1Chosen: TextView
    lateinit var  player2Chosen: TextView
    lateinit var  timerView: TextView
    lateinit var  player1ScoreView: TextView
    lateinit var  player2ScoreView: TextView
    private var cardClickListener: OnClickListener




    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    constructor(context: Context, width: Int, height: Int, player1: Player, player2: Player, clickHandler: OnClickListener) : super(context) {
        this.cardClickListener = clickHandler

        this.player1 = player1
        this.player2 = player2

        this.viewWidth = width
        this.viewHeight = height

        this.orientation = VERTICAL
        this.layoutParams = LayoutParams(width, LayoutParams.MATCH_PARENT)
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundBlue))
        this.gravity = Gravity.BOTTOM

        this.addView(makeName(player2.getName(), false))
        this.addView(makeGameData("Score: 0", false))
//        this.addView(otherCardRow(player2Cards))
        this.addView(makePenguin(player2, false))
        this.addView(makeTimerAndDojoView())
        this.addView(makePenguin(player1, true))
        this.addView(cardRow(player1.getHand()))
        this.addView(makeGameData("Score: 0", true))
        this.addView(makeName(player1.getName(), true))
    }



    fun cardRow(cards: List<Card>): LinearLayout {
        val layout = LinearLayout(context)
        layout.orientation = HORIZONTAL
        layout.gravity = Gravity.CENTER
        layout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 7 * viewHeight / 32)

        val cardWidth = viewWidth / 6
        val cardHeight = viewHeight / 8

        for (i in cards.indices) {
            val btn = Button(context)
            btn.text = cards[i].getCard()
            btn.layoutParams = LayoutParams(cardWidth, cardHeight)
            btn.tag = i
            btn.textSize = 12f
            btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#db9f60"))
            btn.setOnClickListener(cardClickListener)
            layout.addView(btn)
        }

        return layout
    }


    private fun makeName(text: String, isPlayer1: Boolean): LinearLayout {
        val layout = LinearLayout(context)
        layout.orientation = HORIZONTAL
        layout.gravity = if (isPlayer1) Gravity.END else Gravity.START

        layout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, viewHeight / 8 )

        val tv = TextView(context)
        tv.text = text
        tv.textSize = 16f
        tv.setTypeface(null, Typeface.BOLD)
        tv.gravity = Gravity.CENTER
        tv.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
        tv.setTextColor(Color.BLACK)
        tv.layoutParams = LayoutParams(viewWidth / 3, viewHeight / 16)

        layout.addView(tv)
        return layout
    }

    private fun makeGameData(text: String, isPlayer1: Boolean): LinearLayout {
        val layout = LinearLayout(context)
        layout.orientation = HORIZONTAL
        layout.gravity = if (isPlayer1) Gravity.START else Gravity.END
        layout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, if(isPlayer1) 5 * viewHeight / 32 else viewHeight / 8 )

        val tv = TextView(context)
        tv.text = text
        tv.setTypeface(null, Typeface.BOLD)

        tv.setTextColor(Color.BLACK)
        tv.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))

        tv.gravity = Gravity.CENTER
        tv.layoutParams = LayoutParams(viewWidth - (viewWidth / 4), viewHeight / 10)

        layout.addView(tv)

        if(isPlayer1) {
            this.player1ScoreView = tv
        } else {
            this.player2ScoreView = tv
        }
        return layout
    }

    private fun makePenguin(player: Player, isPlayer1: Boolean): LinearLayout {
        var colorPenguin : Int = R.drawable.black_penguin
        if (isPlayer1) {            // means that this is the current player
            val playerColor = player.getColor()
            if (playerColor == "Blue") {
                colorPenguin = R.drawable.blue_penguin
            } else if (playerColor == "Red") {
                colorPenguin = R.drawable.red_penguin
            } else if (playerColor == "Green") {
                colorPenguin = R.drawable.green_penguin
            }
        }
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.HORIZONTAL
        layout.gravity = Gravity.CENTER
        layout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            viewHeight / 12
        )

        val imageView = ImageView(context)
        imageView.setImageResource(colorPenguin)
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        if (isPlayer1) {
            imageView.translationX = -100f
        } else {
            imageView.translationX = 100f
        }
        val imageSize = 250 // Size in pixels (you can also convert from dp if needed)
        imageView.layoutParams = LinearLayout.LayoutParams(imageSize, imageSize)

        layout.addView(imageView)
        return layout
    }

    private fun makeTimerAndDojoView(): LinearLayout {
        val layout = LinearLayout(context)
        layout.orientation = HORIZONTAL
        layout.gravity = Gravity.CENTER
        layout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, viewHeight / 8)
        this.player1Chosen = TextView(context)


        player1Chosen.textSize = 16f
        player1Chosen.setTypeface(null, Typeface.BOLD)
        player1Chosen.setTextColor(Color.WHITE)
        player1Chosen.gravity = Gravity.CENTER
        player1Chosen.layoutParams = LayoutParams(viewWidth / 3, LayoutParams.MATCH_PARENT)

        this.timerView = TextView(context)
        timerView.text = "00:00"
        timerView.textSize = 16f
        timerView.setTypeface(null, Typeface.BOLD)
        timerView.setTextColor(Color.WHITE)
        timerView.gravity = Gravity.CENTER
        timerView.layoutParams = LayoutParams(viewWidth / 3, LayoutParams.MATCH_PARENT)

        this.player2Chosen = TextView(context)
        player2Chosen.textSize = 16f
        player2Chosen.setTypeface(null, Typeface.BOLD)
        player2Chosen.setTextColor(Color.WHITE)
        player2Chosen.gravity = Gravity.CENTER
        player2Chosen.layoutParams = LayoutParams(viewWidth / 3, LayoutParams.MATCH_PARENT)

        layout.addView(player1Chosen)
        layout.addView(timerView)
        layout.addView(player2Chosen)

        return layout
    }
}
