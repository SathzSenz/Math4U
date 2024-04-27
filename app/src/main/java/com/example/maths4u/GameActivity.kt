package com.example.maths4u

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
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
    private lateinit var vibrator: Vibrator
    private lateinit var timerTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftMs: Long = 30000

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        viewModel = ViewModelProvider(this).get(MathQuizViewModel::class.java)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        equationTextView = findViewById(R.id.equationTextView)
        userAnswerEditText = findViewById(R.id.userAnswerEditText)
        nextQuestionButton = findViewById(R.id.nextQuestionButton)
        scoreTextView = findViewById(R.id.scoreTextView)
        livesTextView = findViewById(R.id.livesTextView)
        timerTextView = findViewById(R.id.timerTextView)

        userAnswerEditText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        startTimer()
        updateScoreAndLives()


        nextQuestionButton.setOnClickListener {
            countDownTimer.cancel()
            val userAnswer = userAnswerEditText.text.toString().toIntOrNull()
            if (userAnswer != null) {
                val timeTaken = 30000 - timeLeftMs
                if(viewModel.checkAnswer(userAnswer, timeTaken) > 0) {
                    updateScoreAndLives()
                } else {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
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
        countDownTimer.cancel()
        timeLeftMs = 30000
        startTimer()
    }

    private fun gameOver() {
        val intent = Intent(this, GameOver::class.java)
        intent.putExtra("ROUND_SCORE", viewModel.score)
        startActivity(intent)
        finish()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftMs, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftMs = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                viewModel.lives--
                if(viewModel.lives == 0) {
                    gameOver()
                } else {
                    viewModel.generateEquation()
                    updateScoreAndLives()
                }
            }
        }.start()
    }

    private fun updateTimer() {
        val seconds = (timeLeftMs / 1000).toInt()
        timerTextView.text = "$seconds s Left"
    }
}
