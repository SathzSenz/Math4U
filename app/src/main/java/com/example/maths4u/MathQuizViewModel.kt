package com.example.maths4u

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlin.random.Random

class MathQuizViewModel(application: Application) : AndroidViewModel(application) {

    val sharedPreferencesManager = SharedPreferencesManager(application)

    var operand1: Int = 0     //initializing operand1
    var operand2: Int = 0     //initializing operand2
    var operator: Char = '+'  //initializing operator
    private var correctAnswer: Int = 0         //declaring correct answer

    var score: Int = 0         //declaring score
    var lives: Int = 3          //initializing no of lives
    var correctAnswers: Int = 0     //declaring no of correct answers
    var currentLevel: Int = 1       //declaring current level

    init {
        generateEquation()             //generating equation
    }

    fun generateEquation() {
        operand1 = Random.nextInt(1, 101)    //generate random operand1
        if(currentLevel == 1) {
            operator = arrayOf('+', '-').random()            //operator for level1
        }else {
            operator = arrayOf('+', '-', '*', '/').random()    //operator for other levels
        }

        if(operator == '*') {
            operand2 = Random.nextInt(1, 10)        //second operand if operator is *
        }else {
            operand2 = Random.nextInt(1, 101)
        }

        correctAnswer = when (operator) {      //correction of answer
            '+' -> operand1 + operand2
            '-' -> operand1 - operand2
            '*' -> operand1 * operand2
            '/' -> operand1 / operand2
            else -> 0
        }
    }

    fun checkAnswer(userAnswer: Int, timeTakenMs: Long): Int {
        val isCorrect = userAnswer == correctAnswer           //checking for correct answer
        val timeRemaining = (timeTakenMs / 1000).toInt()
        val score = when {                                          //declaring scoring system
            isCorrect && timeRemaining <= 5 -> 30
            isCorrect && timeRemaining <= 10 -> 25
            isCorrect && timeRemaining <= 15 -> 20
            isCorrect && timeRemaining <= 20 -> 15
            isCorrect && timeRemaining <= 25 -> 10
            isCorrect && timeRemaining <= 30 -> 5
            else -> 0
        }
        if(isCorrect) {
            this.score += score                          //assigning levels according to no of correct answers
            correctAnswers++
            if (currentLevel == 1 && correctAnswers >= 5) {
                currentLevel = 2
                correctAnswers = 0
            }else if (currentLevel == 2 && correctAnswers >= 5) {
                currentLevel = 3
                correctAnswers = 0
            }else if (currentLevel == 3 && correctAnswers >= 10) {
                currentLevel = 4
                correctAnswers = 0
            } else if (currentLevel == 4 && correctAnswers >= 10) {
                currentLevel = 5
                correctAnswers = 0
            }
        }
        else {
            lives--              //take a life if answer is wrong
        }
        generateEquation()
        return score                      //return final score
    }

    fun getTimeLeftMs(): Long {
        return if(currentLevel == 3) 20000           //setting timer according to levels
        else if (currentLevel == 4) 15000
        else if(currentLevel == 5) 10000
        else 30000
    }

    fun saveHighScore() {
        val highScore = sharedPreferencesManager.getHighScore()       //saving highscore
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