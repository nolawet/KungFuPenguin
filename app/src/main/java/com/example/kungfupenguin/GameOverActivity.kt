package com.example.kungfupenguin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.database.*

class GameOverActivity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private var playAgainB: Boolean = false
    private lateinit var winnerName: String
    private lateinit var loserName: String
    private lateinit var ad: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)

        resultText = findViewById(R.id.resultsText)
        val playAgain = findViewById<Button>(R.id.playButton)
        val menu = findViewById<Button>(R.id.menuButton)

        winnerName = intent.getStringExtra("winner") ?: "Unknown"
        loserName = intent.getStringExtra("loser") ?: "Unknown"

        Log.e("Firebase", "wins $winnerName  losses $loserName")
        resultText.text = "$winnerName wins!"

        val firebase = FirebaseDatabase.getInstance("https://kungfupenguin-289c5-default-rtdb.firebaseio.com/")
        Log.d("FirebaseCheck", "Firebase initialized? ${firebase != null}")

        val leaderboardRef = firebase.getReference("leaderboard")

        leaderboardRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    leaderboardRef.setValue(mapOf<String, Any>()) // Create empty leaderboard node
                }
//                } else{
//                    snapshot.value
//                }
//

                val winnerRef = leaderboardRef.child(winnerName)
                val loserRef = leaderboardRef.child(loserName)

                updateWinLoss(winnerRef, true) {
                    updateWinLoss(loserRef, false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to check leaderboard node: ${error.message}")
            }
        })

        playAgain.setOnClickListener {
            playAgainB = true
            showTheAd()
        }

        menu.setOnClickListener {  showTheAd() }
    }


    private fun updateWinLoss(
        ref: DatabaseReference,
        isWinner: Boolean,
        onComplete: (() -> Unit)? = null
    ) {
        ref.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                var wins = currentData.child("wins").getValue(Int::class.java) ?: 0
                var losses = currentData.child("losses").getValue(Int::class.java) ?: 0

                if (isWinner) wins++ else losses++

                currentData.child("wins").value = wins
                currentData.child("losses").value = losses

                Log.e("Firebase", "Updated ${ref.key} - wins: $wins, losses: $losses")
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?
            ) {
                if (error != null) {
                    Log.e("Firebase", "Transaction failed for ${ref.key}: ${error.message}")
                } else {
                    Log.d("Firebase", "Transaction committed for ${ref.key}")
                }
                onComplete?.invoke()
            }
        })
    }



    fun showTheAd( ) {
        var builder : AdRequest.Builder = AdRequest.Builder( )
        builder.addKeyword( "cards" ).addKeyword( "penguins" )
        var request : AdRequest = builder.build()

        var adUnitId : String = "ca-app-pub-3940256099942544/1033173712"


        var adLoad : AdLoad = AdLoad( )
        InterstitialAd.load( this, adUnitId, request, adLoad )
    }

    fun goToGame() {
        Log.w( "MainActivity", "Inside MainActivity::go" )
        var intent = Intent(this, GameActivity::class.java)
        this.startActivity( intent )


    }


    inner class AdLoad : InterstitialAdLoadCallback() {
        override fun onAdLoaded(p0: InterstitialAd) {
            super.onAdLoaded(p0)
            // Log.w( "GameOverActivity", "Inside onAdLoaded" )
            ad = p0

            var manageAdShowing : ManageAdShowing = ManageAdShowing( )
            ad.fullScreenContentCallback = manageAdShowing

            ad.show(this@GameOverActivity)

        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
             Log.w( "AdError", "Inside onAdFailedToLoad" )


        }
    }

    inner class ManageAdShowing : FullScreenContentCallback() {
        override fun onAdClicked() {
            super.onAdClicked()
            Log.w( "Ad", "Inside adClicked" )
        }

        override fun onAdImpression() {
            super.onAdImpression()
            Log.w( "Ad", "Inside onAdImpression" )
        }

        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            Log.w( "Ad", "Inside onAdDismissedFullScreenContent" )
            if (playAgainB) {
                goToGame()
            }
            finish()
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
             Log.w( "Ad", "Inside on AdShowedFullScreenContent" )
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
            Log.w( "Ad", "Inside onAdFailedToShowFullScreenContent" )
        }
    }

}








