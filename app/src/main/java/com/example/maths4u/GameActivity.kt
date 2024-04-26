package com.example.maths4u

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.maths4u.R

class GameActivity : AppCompatActivity() {

    private lateinit var viewModel: MathQuizViewModel
    private lateinit var equationTextView: TextView
    private lateinit var userAnswerEditText: EditText
    private lateinit var nextQuestionButton: Button
    private lateinit var scoreTextView: TextView
    private lateinit var livesTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        viewModel = ViewModelProvider(this).get(MathQuizViewModel::class.java)

        equationTextView = findViewById(R.id.equationTextView)
        userAnswerEditText = findViewById(R.id.userAnswerEditText)
        nextQuestionButton = findViewById(R.id.nextQuestionButton)
        scoreTextView = findViewById(R.id.scoreTextView)
        livesTextView = findViewById(R.id.livesTextView)

        userAnswerEditText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        updateScoreAndLives()

        nextQuestionButton.setOnClickListener {
            val userAnswer = userAnswerEditText.text.toString().toIntOrNull()
            if (userAnswer != null) {
                if(viewModel.checkAnswer(userAnswer)) {
                    updateScoreAndLives()
                } else {
                    if(viewModel.lives == 0) {
                        viewModel.saveHighScore()
                        gameOver()
                    } else {
                        updateScoreAndLives()
                    }
                }
            }
        }
    }

    private fun updateScoreAndLives() {
        scoreTextView.text = "Score: ${viewModel.score}"
        livesTextView.text = "Lives: ${viewModel.lives}"
        equationTextView.text = "${viewModel.operand1} ${viewModel.operator} ${viewModel.operand2} = "
        userAnswerEditText.text.clear()
    }

    private fun gameOver() {
        val intent = Intent(this, GameOver::class.java)
        intent.putExtra("ROUND_SCORE", viewModel.score)
        startActivity(intent)
        finish()
    }
}
