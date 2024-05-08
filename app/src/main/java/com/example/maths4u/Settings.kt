package com.example.maths4u

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch

class Settings : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var musicSwitch: Switch
    private lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //assingning variable to back button
        val backButton: Button = findViewById(R.id.bckbttn)
        backButton.setOnClickListener{
            displayHome()
        }

        musicSwitch = findViewById(R.id.switch2)

        sharedPreferences = getSharedPreferences("music_pref", MODE_PRIVATE)


        mediaPlayer = MediaPlayer.create(this, R.raw.bgmusic)
        mediaPlayer.isLooping = true

        val isMusicOn = sharedPreferences.getBoolean("music_on", true)
        musicSwitch.isChecked = isMusicOn

        //checking when to stqart player
        musicSwitch.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                mediaPlayer.start()
            } else {
                mediaPlayer.pause()
            }

            // Save the state of the switch to shared preferences
            sharedPreferences.edit().putBoolean("music_on", isChecked).apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()    //stop player when activity terminated
    }

    private fun displayHome() {
        val intent = Intent(this, MainActivity::class.java)  //navigate to home activity
        startActivity(intent)
    }
}