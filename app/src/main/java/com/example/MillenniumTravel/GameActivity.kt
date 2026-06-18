package com.example.MillenniumTravel

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.location.LocationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.MillenniumTravel.interfaces.Callback_TiltCallback
import com.example.MillenniumTravel.logic.GameManager
import com.example.MillenniumTravel.models.HighScore
import com.example.MillenniumTravel.utilities.BackgroundMusicPlayer
import com.example.MillenniumTravel.utilities.Constants
import com.example.MillenniumTravel.utilities.SharedPreferencesManager
import com.example.MillenniumTravel.utilities.SignalManager
import com.example.MillenniumTravel.utilities.SingleSoundPlayer
import com.example.MillenniumTravel.utilities.TiltDetector
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.sign

class GameActivity : AppCompatActivity() {

    private lateinit var game_IMG_enemies: Array<Array<AppCompatImageView>>
    private lateinit var game_IMG_ships: Array<AppCompatImageView>
    private lateinit var game_IMG_hearts: Array<AppCompatImageView>
    private lateinit var game_LBL_score: MaterialTextView

    private lateinit var game_BTN_left : ExtendedFloatingActionButton
    private lateinit var game_BTN_right : ExtendedFloatingActionButton


    private lateinit var gameMode: String
    private lateinit var gameManager: GameManager
    private lateinit var tiltDetector: TiltDetector
    private var speedFactor = 0L

    private var elapsedTime: AtomicLong = AtomicLong(0)
    private var timerOn: Boolean = false
    private val timerHandler: Handler = Handler(Looper.getMainLooper())
    private lateinit var timerRunnable: Runnable

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var location: LatLng = LatLng(32.114965179298, 34.81860279084453)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        gameMode = intent.getStringExtra(Constants.Game.GAME_MODE_KEY).toString()

        findViews()
        initViews()

        gameManager = GameManager(game_IMG_hearts.size, game_IMG_enemies.size, game_IMG_enemies[0].size)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getDeviceLocation()

        initTiltDetector()
        initTimer()
        BackgroundMusicPlayer.getInstance().setResourceId(R.raw.background_music)
    }

    private fun findViews() {
        game_LBL_score = findViewById(R.id.game_LBL_score)

        game_BTN_left = findViewById(R.id.game_BTN_left)
        game_BTN_right = findViewById(R.id.game_BTN_right)

        game_IMG_enemies = arrayOf(
            arrayOf(
                findViewById(R.id.game_enemy_0_0),
                findViewById(R.id.game_enemy_0_1),
                findViewById(R.id.game_enemy_0_2),
                findViewById(R.id.game_enemy_0_3),
                findViewById(R.id.game_enemy_0_4),
                findViewById(R.id.game_enemy_0_5),
                findViewById(R.id.game_enemy_0_6),
                findViewById(R.id.game_enemy_0_7),
                findViewById(R.id.game_enemy_0_8)
            ),
            arrayOf(
                findViewById(R.id.game_enemy_1_0),
                findViewById(R.id.game_enemy_1_1),
                findViewById(R.id.game_enemy_1_2),
                findViewById(R.id.game_enemy_1_3),
                findViewById(R.id.game_enemy_1_4),
                findViewById(R.id.game_enemy_1_5),
                findViewById(R.id.game_enemy_1_6),
                findViewById(R.id.game_enemy_1_7),
                findViewById(R.id.game_enemy_1_8)
            ),
            arrayOf(
                findViewById(R.id.game_enemy_2_0),
                findViewById(R.id.game_enemy_2_1),
                findViewById(R.id.game_enemy_2_2),
                findViewById(R.id.game_enemy_2_3),
                findViewById(R.id.game_enemy_2_4),
                findViewById(R.id.game_enemy_2_5),
                findViewById(R.id.game_enemy_2_6),
                findViewById(R.id.game_enemy_2_7),
                findViewById(R.id.game_enemy_2_8)
            ),
            arrayOf(
                findViewById(R.id.game_enemy_3_0),
                findViewById(R.id.game_enemy_3_1),
                findViewById(R.id.game_enemy_3_2),
                findViewById(R.id.game_enemy_3_3),
                findViewById(R.id.game_enemy_3_4),
                findViewById(R.id.game_enemy_3_5),
                findViewById(R.id.game_enemy_3_6),
                findViewById(R.id.game_enemy_3_7),
                findViewById(R.id.game_enemy_3_8)
            ),
            arrayOf(
                findViewById(R.id.game_enemy_4_0),
                findViewById(R.id.game_enemy_4_1),
                findViewById(R.id.game_enemy_4_2),
                findViewById(R.id.game_enemy_4_3),
                findViewById(R.id.game_enemy_4_4),
                findViewById(R.id.game_enemy_4_5),
                findViewById(R.id.game_enemy_4_6),
                findViewById(R.id.game_enemy_4_7),
                findViewById(R.id.game_enemy_4_8)
            )
        )

        game_IMG_ships = arrayOf(
            findViewById(R.id.game_ship_0),
            findViewById(R.id.game_ship_1),
            findViewById(R.id.game_ship_2),
            findViewById(R.id.game_ship_3),
            findViewById(R.id.game_ship_4)
        )

        game_IMG_hearts = arrayOf(
            findViewById(R.id.game_heart_0),
            findViewById(R.id.game_heart_1),
            findViewById(R.id.game_heart_2)
        )
    }

    private fun initViews() {
        if (gameMode == Constants.Game.GAME_MODE_TILT) {
            game_BTN_left.visibility = View.INVISIBLE
            game_BTN_right.visibility = View.INVISIBLE
        }
        else {
            game_BTN_left.setOnClickListener { moveLeft() }
            game_BTN_right.setOnClickListener { moveRight() }
        }
    }

    override fun onPause() {
        super.onPause()
        pauseTimer()
        tiltDetector.stop()
        BackgroundMusicPlayer.getInstance().pauseMusic()
    }

    override fun onResume() {
        super.onResume()
        startOrPauseTimer()
        tiltDetector.start()
        BackgroundMusicPlayer.getInstance().playMusic()
    }

    // ========================== Timer ==========================
    private fun initTimer() {
        timerRunnable = object: Runnable {
            override fun run() {
                elapsedTime.getAndIncrement()
                tickGame()
                timerHandler.postDelayed(this, Constants.Game.DELAY - speedFactor)
            }
        }
    }

    private fun pauseTimer() {
        timerHandler.removeCallbacks(timerRunnable)
        timerOn = false
    }

    fun stopTimer() {
        pauseTimer()
        timerHandler.removeCallbacks(timerRunnable)
    }

    fun startOrPauseTimer() {
        if (!timerOn) {
            timerOn = true
            timerHandler.post(timerRunnable)
        }
        else {
            pauseTimer()
        }
    }




    // ========================== Tilt ==========================
    private fun initTiltDetector() {
        tiltDetector = TiltDetector(
            this,
            object : Callback_TiltCallback {
                override fun tiltX(tiltValue: Float) {
                    if (gameMode == Constants.Game.GAME_MODE_TILT) {
                        if (tiltValue > 0) moveLeft() else moveRight()
                    }
                }

                override fun tiltY(tiltValue: Float) {
                    speedFactor = (speedFactor - (tiltValue.sign * Constants.Game.SPEED_MULT).toLong()).coerceIn(0L, Constants.Game.MAX_SPEED)
                }
            })
    }

    // ========================== Gameplay ==========================
    private fun moveLeft() {
        gameManager.movePlayer(-1)
        refreshUI()
    }

    private fun moveRight() {
        gameManager.movePlayer(1)
        refreshUI()
    }

    private fun refreshUI() {
        // Game Over
        if (gameManager.isGameOver) {
            gameOver()
        }

        // Enemy Position
        for (col in 0..<game_IMG_enemies.size) {
            for (row in 0..<game_IMG_enemies[0].size) {
                if (gameManager.currentEnemyPos[col][row] == 1) {
                    game_IMG_enemies[col][row].setImageResource(R.drawable.enemy_ship)
                    game_IMG_enemies[col][row].visibility = View.VISIBLE
                }
                else if (gameManager.currentEnemyPos[col][row] == 2) {
                    game_IMG_enemies[col][row].setImageResource(R.drawable.rebel_logo)
                    game_IMG_enemies[col][row].visibility = View.VISIBLE
                }
                else {
                    game_IMG_enemies[col][row].visibility = View.INVISIBLE
                }
            }
        }

        // Player Position
        for (lane in 0..<game_IMG_ships.size) {
            if (lane == gameManager.currentPlayerPos) {
                game_IMG_ships[lane].visibility = View.VISIBLE
            }
            else {
                game_IMG_ships[lane].visibility = View.INVISIBLE
            }
        }

        // Lives
        if (gameManager.hitCount != 0)
            game_IMG_hearts[game_IMG_hearts.size - gameManager.hitCount].visibility = View.INVISIBLE
        if (game_IMG_hearts.size - gameManager.hitCount != 0)
            game_IMG_hearts[game_IMG_hearts.size - gameManager.hitCount - 1].visibility = View.VISIBLE

        // Score
        game_LBL_score.text = if (elapsedTime.toInt() < 10) "${elapsedTime.toInt() * 100}m" else "${elapsedTime.toFloat() / 10}km"
    }

    private fun tickGame() {
        gameManager.checkHit()
        gameManager.proceed()

        if (gameManager.isGameOver) {
            stopTimer()
        }

        refreshUI()
    }

    private fun gameOver() {
        val currentScore = elapsedTime.toInt()
        val scoreText = if (currentScore < 10) "${currentScore * 100}m" else "${currentScore / 10}km"

        BackgroundMusicPlayer.getInstance().stopMusic()
        val ssp = SingleSoundPlayer(this)
        ssp.playSound(R.raw.explosion)
        SignalManager.getInstance().toast("Game Over!\nYou've traveled ${scoreText}!", SignalManager.ToastLength.LONG)
        SignalManager.getInstance().vibrate()

        val sp = SharedPreferencesManager.getInstance()
        val jsonString = sp.getString(Constants.SP_KEYS.HIGHSCORES_KEY, "[]")
        val type = object : TypeToken<List<HighScore>>() {}.type
        val savedHighScores: MutableList<HighScore> = Gson().fromJson<List<HighScore>>(jsonString, type).toMutableList()


        if (savedHighScores.size < 10 || currentScore > savedHighScores.last().score) {
            val newHighScore: HighScore = HighScore.Builder(currentScore, location).build()
            savedHighScores.add(newHighScore)
            savedHighScores.sortByDescending { it.score }
            if (savedHighScores.size > 10)
                savedHighScores.removeLast()
            val updatedJson = Gson().toJson(savedHighScores)
            sp.putString(Constants.SP_KEYS.HIGHSCORES_KEY, updatedJson)
        }


        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getDeviceLocation() {
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    location = LatLng(task.result.latitude, task.result.longitude)
                } else {
                    Log.d(TAG, "Current location is null. Using defaults.")
                    Log.e(TAG, "Exception: %s", task.exception)
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
}
