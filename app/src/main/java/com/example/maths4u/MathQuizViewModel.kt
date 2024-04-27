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

    init {
        generateEquation()
    }

    fun generateEquation() {
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

    fun checkAnswer(userAnswer: Int, timeTakenMs: Long): Int {
        val isCorrect = userAnswer == correctAnswer
        val timeRemaining = (timeTakenMs / 1000).toInt()
        val score = when {
            isCorrect && timeRemaining <= 5 -> 30
            isCorrect && timeRemaining <= 10 -> 25
            isCorrect && timeRemaining <= 15 -> 20
            isCorrect && timeRemaining <= 20 -> 15
            isCorrect && timeRemaining <= 25 -> 10
            isCorrect && timeRemaining <= 30 -> 5
            else -> 0
        }
        if(isCorrect) {
            this.score += score
        }
        else {
            lives--
        }
        generateEquation()
        return score
    }

    fun saveHighScore() {
        val highScore = sharedPreferencesManager.getHighScore()
        if(score > highScore) {
            sharedPreferencesManager.saveHighScore(score)
        }
    }

    fun saveRoundScore() {
        val roundScores = sharedPreferencesManager.getAllHighScores().toMutableList()
        roundScores.add(score)
        sharedPreferencesManager.saveAllRoundScores(roundScores)
    }


}