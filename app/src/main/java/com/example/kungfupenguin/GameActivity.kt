package com.example.kungfupenguin
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//import com.example.project.GameView
import android.widget.Toast
import com.example.kungfupenguin.GameModel
import com.example.kungfupenguin.GameOverActivity
import com.example.project.GameView

class GameActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var game: GameModel
    private lateinit var player1: Player
    private lateinit var player2: Player

    private lateinit var timer: CountDownTimer
    private var cardSelected = false

    private var topInset: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val displayMetrics = Resources.getSystem().displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val sharedPreferences : SharedPreferences = getSharedPreferences("groupProject", MODE_PRIVATE)

        player2 = Player("CPU", "#aa0000")

        val playerName = sharedPreferences.getString("playerName", "Anonymous")
        val color = sharedPreferences.getString("color", "Black")
        player1 = Player("$playerName", "$color")


        // RHEA CHANGED THE PLAYER1 and PLAYER2 locations to get Player in bottom right
        // Create gameView first
        gameView = GameView(this, screenWidth, screenHeight, player1, player2, CardClickHandler())
        game = GameModel(player1,  player2 )

        // Now apply inset listener safely
        ViewCompat.setOnApplyWindowInsetsListener(gameView) { view, insets ->
            topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            view.setPadding(0, topInset, 0, 0)
            insets
        }
        setContentView(gameView)

        startTimer()
    }


    inner class CardClickHandler : View.OnClickListener {
        override fun onClick(v: View?) {
            val index = v?.tag as? Int ?: return
            onCardSelected(index)
        }

    }

    fun onCardSelected(index: Int) {
        cardSelected = true
        timer.cancel()

        val card1 = player1.getHand().getOrNull(index) ?: return
        val card2 = player2.getHand().getOrNull(0) ?: return

        val result = game.playRound(index, 0)

        gameView.player1Chosen.text = card1.getCard()
        gameView.player2Chosen.text = card2.getCard()
        gameView.player1ScoreView.text = "Score: ${player1.getScore()}"
        gameView.player2ScoreView.text = "Score: ${player2.getScore()}"

        // Refresh the hand view
        gameView.removeViewAt(5) // assumes this is always cardRow
        gameView.addView(gameView.cardRow(player1.getHand()), 5)

        checkGameOverAndReset()

    }


    private fun startTimer() {
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                gameView.timerView.text = "Time: $secondsLeft"
            }

            override fun onFinish() {
                if (!cardSelected) {
                    Toast.makeText(this@GameActivity, "Time's up! ${player2.getName()} wins this round!", Toast.LENGTH_SHORT).show()
                    player2.incrementScore()
                    gameView.player2ScoreView.text = "Score: ${player2.getScore()}"
                    checkGameOverAndReset()
                }
            }
        }
        cardSelected = false
        timer.start()
    }

    private fun checkGameOverAndReset() {
        if (game.isGameOver()) {
            val winner = game.getWinner()
            val loser  = game.getLoser()

            Log.d("GameActivity", "Winner: $winner | Loser: $loser")  // <-- ADD THIS


            val intent = Intent(this@GameActivity, GameOverActivity::class.java)
            intent.putExtra("winner", winner)
            intent.putExtra("loser", loser)
            startActivity(intent)

            finish() // Finish after launching GameOverActivity
        } else {
            startTimer()
        }

    }
}
