package com.example.MillenniumTravel

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.MillenniumTravel.utilities.Constants
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var main_BTN_tilt : MaterialButton
    private lateinit var main_BTN_buttons : MaterialButton
    private lateinit var main_BTN_leaderboard : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()

        getLocationPermission()
    }

    private fun findViews() {
        main_BTN_tilt = findViewById(R.id.main_BTN_tilt)
        main_BTN_buttons = findViewById(R.id.main_BTN_buttons)
        main_BTN_leaderboard = findViewById(R.id.main_BTN_leaderboard)
    }

    private fun initViews() {
        main_BTN_tilt.setOnClickListener{ changeActivityToGame(Constants.Game.GAME_MODE_TILT) }
        main_BTN_buttons.setOnClickListener{ changeActivityToGame(Constants.Game.GAME_MODE_BUTTONS) }
        main_BTN_leaderboard.setOnClickListener{ changeActivityToLeaderboard() }
    }

    private fun changeActivityToGame(gameMode: String) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(Constants.Game.GAME_MODE_KEY, gameMode)
        startActivity(intent)
        finish()
    }

    private fun changeActivityToLeaderboard() {
        val intent = Intent(this, LeaderboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1)
        }
    }
}