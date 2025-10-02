package com.example.kungfupenguin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

/*
Nolawe Temketem     20%
Felix Ogordi        20%
Rhea Ovungal        20%
Lilli Terry         20%

Model-View-Controller architecture is used in GameModel.kt(Player.kt and Cards.kt) GameView.kt and GameActivity.kt

Icon for the App is a Penguin

3 Views activtiy_main.xml, activtiy_instructions.xml, GameView.kt, activity_gameover.xml

Local persistent data:  store color and name of penguin. used to determining the player/penguin  in  game (MainActivity.kt)
Remote persistent data:  firebase to have a leaderboard with total wins and losses for each player (GameOverActivity.kt)

Requires meaningful use of app or google service : Use of a Timer when playing (GameActivity.kt and GameView.kt)

Two New GUI components
Spinner for users to choose penguin color(Uses Listener)
FrameLayout used in activtiy_instructions.xml and activtiy_main.xml

Advertising:
Interstitial Ad after  the GameOver screen when user to leaves screen

*/



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val spinner: Spinner = findViewById(R.id.Color_spinner)

// Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.colors_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        var playerColor: String = "Black"
// Set up Lister for Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selectedColor = parent.getItemAtPosition(position).toString()
                playerColor = selectedColor
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val sharedPreferences: SharedPreferences =
            getSharedPreferences("groupProject", MODE_PRIVATE)

        val setNameButton: Button = findViewById<Button>(R.id.setName)
        val nameSetter: EditText = findViewById<EditText>(R.id.Enter_Name)

        var playerName: String = "Anonymous"
        setNameButton.setOnClickListener {

            playerName = nameSetter.text.toString()
            //  playerColor = spinner.selectedItem.toString()
            val editor = sharedPreferences.edit()
            editor.putString("playerName", playerName)
            editor.putString("color", playerColor)
            editor.commit()
        }

        // DEFAULT players
        val rootLayout = findViewById<RelativeLayout>(R.id.main)
        val earnBeltsButton = findViewById<Button>(R.id.Earn_Belts)
        val howToPlayButton = findViewById<Button>(R.id.Instructions)

        earnBeltsButton.setOnClickListener {
            // Get screen dimensions
            val displayMetrics = resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val screenHeight = displayMetrics.heightPixels

            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("playerName", playerName)
            intent.putExtra("color", playerColor)
            startActivity(intent)
        }

        howToPlayButton.setOnClickListener {
            // Launch instruction activity
            val intent = Intent(this, InstructionsActivity::class.java)
            startActivity(intent)
        }
    }

}