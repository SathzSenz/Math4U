package com.example.maths4u

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.preference.PreferenceManager
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.maths4u.R

class GameActivity : AppCompatActivity() {

    //implementing variables
    private lateinit var viewModel: MathQuizViewModel
    private lateinit var equationTextView: TextView
    private lateinit var userAnswerEditText: EditText
    private lateinit var nextQuestionButton: Button
    private lateinit var pauseButton: Button
    private lateinit var scoreTextView: TextView
    private lateinit var livesTextView: TextView
    private lateinit var levelTextView: TextView
    private lateinit var vibrator: Vibrator
    private lateinit var timerTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeftMs: Long = 30000
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var sharedPreferences: SharedPreferences
    private var backgroundMusicOn = true
    private lateinit var pausedLayout: View
    private lateinit var resumeButton : Button
    private lateinit var quitButton : Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        viewModel = ViewModelProvider(this).get(MathQuizViewModel::class.java) //initilizing view Model using viewModel provider
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator  //implementing vibrator

        //assigning variables to the ids
        equationTextView = findViewById(R.id.equationTextView)
        userAnswerEditText = findViewById(R.id.userAnswerEditText)
        nextQuestionButton = findViewById(R.id.nextQuestionButton)
        pauseButton = findViewById(R.id.pause_button)
        scoreTextView = findViewById(R.id.scoreTextView)
        livesTextView = findViewById(R.id.livesTextView)
        levelTextView = findViewById(R.id.levelTextView)
        timerTextView = findViewById(R.id.timerTextView)
        pausedLayout = findViewById(R.id.pausedLayout)
        resumeButton = findViewById(R.id.resumeButton)
        quitButton = findViewById(R.id.quitButton)

        userAnswerEditText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        sharedPreferences = getSharedPreferences("music_pref", MODE_PRIVATE)
        backgroundMusicOn = sharedPreferences.getBoolean("music_on", true)

// Initialize media player
        mediaPlayer = MediaPlayer.create(this, R.raw.bgmusic)
        mediaPlayer.isLooping = true

// Check if background music should be played
        if (backgroundMusicOn) {
            mediaPlayer.start()
        }
        startTimer()
        updateScoreAndLives()


        nextQuestionButton.setOnClickListener {
            countDownTimer.cancel()                          //reset timer when button clicked
            val userAnswer = userAnswerEditText.text.toString().toIntOrNull()
            if (userAnswer != null) {
                val timeTaken = 30000 - timeLeftMs             //time taken to answer
                if(viewModel.checkAnswer(userAnswer, timeTaken) > 0) {
                    updateScoreAndLives()                   //updating scores and lives
                } else {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))   //vibrate if answer is wrong
                    if(viewModel.lives == 0) {
                        viewModel.saveHighScore()     //savehighscore if no lives remaining
                        gameOver()
                    } else {
                        updateScoreAndLives()           //udpate score and lives
                    }
                }
            }
        }

        pauseButton.setOnClickListener{
            pauseGame()
        }

        resumeButton.setOnClickListener{
            resumeGame()
        }

        quitButton.setOnClickListener{
            quitGame()
        }

        pausedLayout.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()          //release timer when activity terminated
        mediaPlayer.release()          //release mp3 when activity terminated
    }

    private fun updateScoreAndLives() {
        val oldLevel = viewModel.currentLevel
        levelTextView.text = "Level ${viewModel.currentLevel}"
        scoreTextView.text = "Score: ${viewModel.score}"
        livesTextView.text = "Lives: ${viewModel.lives}"
        equationTextView.text = "${viewModel.operand1} ${viewModel.operator} ${viewModel.operand2} = "
        userAnswerEditText.text.clear()
        countDownTimer.cancel()
        timeLeftMs = viewModel.getTimeLeftMs()
        startTimer()

        if(viewModel.currentLevel != oldLevel) {
            val message = "Level ${viewModel.currentLevel} reached!"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun gameOver() {
        val roundScore = viewModel.score
        SharedPreferencesManager(this).saveRoundScore(roundScore)

        val intent = Intent(this, GameOver::class.java)     //if game over navigate to game over activity
        intent.putExtra("ROUND_SCORE", roundScore)
        startActivity(intent)
        finish()
    }

    fun saveRoundScore(score: Int) {
        val roundScores = getAllRoundScores().toMutableList()
        roundScores.add(score)
        sharedPreferences.edit().putStringSet("RoundScores", roundScores.map { it.toString() }.toSet()).apply()
    }

    fun getAllRoundScores(): List<Int> {
        val roundScoreSet = sharedPreferences.getStringSet("RoundScores", setOf()) ?: setOf()
        return roundScoreSet.map { it.toInt() }
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
                    viewModel.saveRoundScore()
                    gameOver()
                } else {
                    viewModel.generateEquation()
                    updateScoreAndLives()
                }
            }
        }.start()
    }

    private fun updateTimer() {                    //display remaining time
        val seconds = (timeLeftMs / 1000).toInt()
        timerTextView.text = "$seconds s Left"
    }

    private fun pauseGame() {
        countDownTimer.cancel()
        mediaPlayer.pause()
        pausedLayout.visibility = View.VISIBLE
    }

    private fun resumeGame() {
        pausedLayout.visibility = View.GONE
        startTimer()
        if(backgroundMusicOn && !mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    private fun quitGame() {
        countDownTimer.cancel()
        mediaPlayer.release()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
