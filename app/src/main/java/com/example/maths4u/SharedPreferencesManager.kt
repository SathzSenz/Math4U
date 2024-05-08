package com.example.maths4u

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MathQuiz", Context.MODE_PRIVATE)

    fun saveHighScore(score: Int) {
        sharedPreferences.edit().putInt("HighScore", score).apply()
    }

    fun getHighScore(): Int {
        return sharedPreferences.getInt("HighScore", 0)
    }

    fun getAllHighScores(): List<Int> {
        val highScoreSet = sharedPreferences.getStringSet("HighScores", setOf()) ?: setOf()
        return highScoreSet.map { it.toInt() }
    }

    fun saveAllRoundScores(roundScores: List<Int>) {
        val roundScoresSet = roundScores.map { it.toString()}.toSet()
        sharedPreferences.edit().putStringSet("RoundScores", roundScoresSet).apply()
    }

    fun getAllRoundScores(): List<Int> {
        val roundScoreSet = sharedPreferences.getStringSet("RoundScores", setOf()) ?: setOf()
        return roundScoreSet.map { it.toInt() }
    }

    fun saveRoundScore(score: Int) {
        val roundScores = getAllRoundScores().toMutableList()
        roundScores.add(score)
        sharedPreferences.edit().putStringSet("RoundScores", roundScores.map { it.toString() }.toSet()).apply()
    }
}