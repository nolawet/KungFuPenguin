package com.example.kungfupenguin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class InstructionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructions) // This is your instructions.xml layout

        val backButton = findViewById<Button>(R.id.back_Button)
        backButton.setOnClickListener { // Return to MainActivity
            val intent = Intent(
                this@InstructionsActivity,
                MainActivity::class.java
            )
            startActivity(intent)
            finish()
        }
    }
}