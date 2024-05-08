package com.example.maths4u

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //assigning variables to buttons
        val startGameButton: Button = findViewById(R.id.button)
        startGameButton.setOnClickListener{
            startGame()
        }

        val highScoreButton: Button = findViewById(R.id.button2)
        highScoreButton.setOnClickListener{
            displayHighScore()
        }

        val settings_Button: Button = findViewById(R.id.settings_bttn)
        settings_Button.setOnClickListener{
            displaySettings()
        }
    }

    private fun startGame() {
        val intent = Intent(this, GameActivity::class.java)  //navigate to game activity
        startActivity(intent)
    }

    private fun displayHighScore() {
        val allRoundScores = SharedPreferencesManager(this).getAllRoundScores()
        val uniqueTop5RoundScores = allRoundScores.toSet() // Convert to set to remove duplicates
            .sortedDescending() // Sort in descending order
            .take(5) // Take top 5 scores
        val intent = Intent(this, HighScores::class.java)    //navigate to highscore activity
        intent.putExtra("TOP_SCORES", ArrayList(uniqueTop5RoundScores))
        startActivity(intent)
    }

    private fun displaySettings() {
        val intent = Intent(this, Settings::class.java)  //navigate to settings activity
        startActivity(intent)
    }
}