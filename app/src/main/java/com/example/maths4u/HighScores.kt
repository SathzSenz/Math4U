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

        val highScores = getTop5Highscores()

        val highScoresTextView: TextView = findViewById(R.id.highScoresTextView)
        highScoresTextView.text = highScores.joinToString("\n")
    }

    private fun getTop5Highscores(): List<Int> {
        val allRoundScores = sharedPreferencesManager.getAllHighScores()

        val sortedHighScores = allRoundScores.sortedDescending()

        return sortedHighScores.take(5)
    }
}