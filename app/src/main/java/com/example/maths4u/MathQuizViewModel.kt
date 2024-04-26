package com.example.maths4u

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlin.random.Random

class MathQuizViewModel(application: Application) : AndroidViewModel(application) {

    val sharedPreferencesManager = SharedPreferencesManager(application)

    var operand1: Int = 0
    var operand2: Int = 0
    var operator: Char = '+'
    private var correctAnswer: Int = 0

    var score: Int = 0
    var lives: Int = 3
        private set
    init {
        generateEquation()
    }

    private fun generateEquation() {
        operand1 = Random.nextInt(1, 101)
        operand2 = Random.nextInt(1, 101)
        operator = arrayOf('+', '-', '*', '/').random()
        correctAnswer = when (operator) {
            '+' -> operand1 + operand2
            '-' -> operand1 - operand2
            '*' -> operand1 * operand2
            '/' -> operand1 / operand2
            else -> 0
        }
    }

    fun checkAnswer(userAnswer: Int): Boolean {
        val isCorrect = userAnswer == correctAnswer
        if(isCorrect) {
            score += 10
        }
        else {
            lives--
        }
        generateEquation()
        return isCorrect
    }

    fun saveHighScore() {
        val highScore = sharedPreferencesManager.getHighScore()
        if(score > highScore) {
            sharedPreferencesManager.saveHighScore(score)
        }
    }

}