package com.example.MillenniumTravel

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.MillenniumTravel.interfaces.Callback_HighScoreClicked
import com.example.MillenniumTravel.ui.HighScoreFragment
import com.example.MillenniumTravel.ui.MapFragment

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var lb_FRAME_list: FrameLayout
    private lateinit var lb_FRAME_map: FrameLayout

    private lateinit var highScoreFragment: HighScoreFragment
    private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leaderboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViews()
    }

    private fun findViews() {
        lb_FRAME_list = findViewById(R.id.lb_FRAME_list)
        lb_FRAME_map = findViewById(R.id.lb_FRAME_map)
    }

    private fun initViews() {
        highScoreFragment = HighScoreFragment()
        HighScoreFragment.callback = object: Callback_HighScoreClicked {
            override fun highScoreItemClicked(lat: Double, lon: Double) {
                mapFragment.zoom(lat, lon)
            }
        }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.lb_FRAME_list, highScoreFragment)
            .commit()

        mapFragment = MapFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.lb_FRAME_map, mapFragment)
            .commit()
    }
}