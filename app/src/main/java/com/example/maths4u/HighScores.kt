package com.example.maths4u

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class HighScores : AppCompatActivity() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_scores)

        sharedPreferencesManager = SharedPreferencesManager(this)

        // Retrieve the top scores from the intent extra
        val topScores = intent.getIntegerArrayListExtra("TOP_SCORES")
        val leaderboardTextView: TextView = findViewById(R.id.leaderboardTextView)

        // Check if topScores is not null and contains data
        if (topScores != null && topScores.isNotEmpty()) {
            // Convert the list of scores to a string and display it in the TextView
            leaderboardTextView.text = topScores.joinToString("\n")
        } else {
            // Handle case where topScores is null or empty
            leaderboardTextView.text = "No high scores available"
        }
    }
}
