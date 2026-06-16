package com.example.MillenniumTravel

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.MillenniumTravel.interfaces.Callback_HighScoreClicked
import com.example.MillenniumTravel.models.HighScore
import com.example.MillenniumTravel.ui.HighScoreFragment
import com.example.MillenniumTravel.utilities.Constants
import com.example.MillenniumTravel.utilities.SharedPreferencesManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LeaderboardActivity : AppCompatActivity(), OnMapReadyCallback  {

    private lateinit var lb_FRAME_list: FrameLayout
    private lateinit var lb_FRAME_map: FrameLayout
    private lateinit var lb_BTN_home: ExtendedFloatingActionButton

    private lateinit var highScoreFragment: HighScoreFragment
    private lateinit var mapFragment: SupportMapFragment

    private var googleMap: GoogleMap? = null
    private var pendingLocation: LatLng? = null

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
        lb_BTN_home = findViewById(R.id.lb_BTN_home)
    }

    private fun initViews() {
        lb_BTN_home.setOnClickListener { changeActivityToMain() }

        highScoreFragment = HighScoreFragment()
        HighScoreFragment.highScoreItemClicked =
            object: Callback_HighScoreClicked {
                override fun highScoreItemClicked(lat: Double, lon: Double) {
                    zoomMap(lat, lon)
                }
            }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.lb_FRAME_list, highScoreFragment)
            .commit()

        mapFragment = SupportMapFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.lb_FRAME_map, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        pendingLocation?.let { location ->
            zoomMap(location.latitude, location.longitude)
            pendingLocation = null
        }

        val sp = SharedPreferencesManager.getInstance()
        val jsonString = sp.getString(Constants.SP_KEYS.HIGHSCORES_KEY, "[]")
        val type = object : TypeToken<List<HighScore>>() {}.type
        val savedHighScores: List<HighScore> = Gson().fromJson(jsonString, type)

        for (i in (0..<savedHighScores.size)) {
            googleMap.addMarker(MarkerOptions().position(savedHighScores[i].location))
        }
    }

    private fun zoomMap(lat: Double, lon: Double) {
        val position = LatLng(lat, lon)

        if (googleMap != null) {
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
        } else {
            pendingLocation = position
        }
    }

    private fun changeActivityToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}