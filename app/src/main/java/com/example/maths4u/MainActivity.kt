package com.example.maths4u

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startGameButton: Button = findViewById(R.id.button)
        startGameButton.setOnClickListener{
            startGame()
        }

        val highScoreButton: Button = findViewById(R.id.button2)
        highScoreButton.setOnClickListener{
            displayHighScore()
        }
    }

    private fun startGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    private fun displayHighScore() {
        val intent = Intent(this, HighScores::class.java)
        startActivity(intent)
    }
}