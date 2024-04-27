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
        val newHighScoreTextView: TextView = findViewById(R.id.newHighScoreTextView)
        val roundScoreTextView: TextView = findViewById(R.id.roundScoreTextView)
        val playAgainButton: Button = findViewById(R.id.playAgainButton)
        val menuButton: Button = findViewById(R.id.menu_button)

        val roundScore = intent.getIntExtra("ROUND_SCORE", 0)
        val highScore = viewModel.sharedPreferencesManager.getHighScore()

        if (roundScore == highScore) {
            newHighScoreTextView.text = "Congratulations! New High Score"
        } else {
            newHighScoreTextView.text = ""
        }

        roundScoreTextView.text = "Round Score: $roundScore"
        highScoreTextView.text = "High Score : $highScore"


        playAgainButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }

        menuButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
