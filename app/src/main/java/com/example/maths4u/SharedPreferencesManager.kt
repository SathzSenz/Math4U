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
}