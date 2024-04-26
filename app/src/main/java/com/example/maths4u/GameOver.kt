package com.example.maths4u

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.maths4u.R

class GameOver : AppCompatActivity() {

    private lateinit var viewModel: MathQuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        viewModel = ViewModelProvider(this).get(MathQuizViewModel::class.java)

        val highScoreTextView: TextView = findViewById(R.id.highScoreTextView)
        val roundScoreTextView: TextView = findViewById(R.id.roundScoreTextView)
        val playAgainButton: Button = findViewById(R.id.playAgainButton)

        val roundScore = intent.getIntExtra("ROUND_SCORE", 0)
        val highScore = viewModel.sharedPreferencesManager.getHighScore()

        roundScoreTextView.text = "Round Score: $roundScore"
        highScoreTextView.text = "High Score : $highScore"

        playAgainButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
