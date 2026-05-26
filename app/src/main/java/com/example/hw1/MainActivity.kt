package com.example.hw1

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hw1.logic.GameManager
import com.example.hw1.utilities.Constants
import com.google.android.material.button.MaterialButton
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.atomic.AtomicLong

class MainActivity : AppCompatActivity() {

    private lateinit var main_IMG_enemies: Array<Array<AppCompatImageView>>

    private lateinit var main_IMG_ships: Array<AppCompatImageView>

    private lateinit var main_IMG_hearts: Array<AppCompatImageView>

    private lateinit var main_BTN_left: MaterialButton
    private lateinit var main_BTN_right: MaterialButton

    private lateinit var gameManager: GameManager

    private lateinit var timer: Timer

    private var elapsedTime: AtomicLong = AtomicLong(0)

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

        gameManager = GameManager(main_IMG_hearts.size, main_IMG_enemies.size, main_IMG_enemies[0].size)

        initViews()

        initTimer()
    }

    private fun findViews() {
        main_IMG_enemies =arrayOf(
            arrayOf(
                findViewById(R.id.enemy_0_0),
                findViewById(R.id.enemy_0_1),
                findViewById(R.id.enemy_0_2),
                findViewById(R.id.enemy_0_3),
                findViewById(R.id.enemy_0_4),
                findViewById(R.id.enemy_0_5)
            ),
            arrayOf(
                findViewById(R.id.enemy_1_0),
                findViewById(R.id.enemy_1_1),
                findViewById(R.id.enemy_1_2),
                findViewById(R.id.enemy_1_3),
                findViewById(R.id.enemy_1_4),
                findViewById(R.id.enemy_1_5)
            ),
            arrayOf(
                findViewById(R.id.enemy_2_0),
                findViewById(R.id.enemy_2_1),
                findViewById(R.id.enemy_2_2),
                findViewById(R.id.enemy_2_3),
                findViewById(R.id.enemy_2_4),
                findViewById(R.id.enemy_2_5)
            )
        )

        main_IMG_ships = arrayOf(
            findViewById(R.id.ship_0),
            findViewById(R.id.ship_1),
            findViewById(R.id.ship_2)
        )

        main_IMG_hearts = arrayOf(
            findViewById(R.id.heart_0),
            findViewById(R.id.heart_1),
            findViewById(R.id.heart_2)
        )

        main_BTN_left = findViewById(R.id.btn_left)
        main_BTN_right = findViewById(R.id.btn_right)
    }

    private fun initViews() {
        main_BTN_left.setOnClickListener{ moveLeft() }
        main_BTN_right.setOnClickListener{ moveRight() }
    }

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
            Toast.makeText(this, "Game Over!\nYou survived for: $elapsedTime seconds!", Toast.LENGTH_SHORT).show()
            gameManager = GameManager(main_IMG_hearts.size, main_IMG_enemies.size, main_IMG_enemies[0].size)
            elapsedTime.set(0)
            for (i in 0..<main_IMG_hearts.size) {
                main_IMG_hearts[i].visibility = View.VISIBLE
            }
            initTimer()
        }

        // Enemy Position
        for (col in 0..<main_IMG_enemies.size) {
            for (row in 0..<main_IMG_enemies[0].size) {
                if (gameManager.currentEnemyPos[col][row] == 1) {
                    main_IMG_enemies[col][row].visibility = View.VISIBLE
                }
                else {
                    main_IMG_enemies[col][row].visibility = View.INVISIBLE
                }
            }
        }

        // Player Position
        for (lane in 0..<main_IMG_ships.size) {
            if (lane == gameManager.currentPlayerPos) {
                main_IMG_ships[lane].visibility = View.VISIBLE
            }
            else {
                main_IMG_ships[lane].visibility = View.INVISIBLE
            }
        }

        // Lives
        if (gameManager.hitCount != 0) {
            main_IMG_hearts[main_IMG_hearts.size - gameManager.hitCount].visibility = View.INVISIBLE
        }
    }

    private fun initTimer() {
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    elapsedTime.getAndIncrement()
                    tickGame()
                }
            },
            0,
            Constants.Timer.DELAY
        )
    }

    private fun tickGame() {
        gameManager.checkHit()
        gameManager.proceed()

        if (gameManager.isGameOver) {
            timer.cancel()
        }

        runOnUiThread {
            refreshUI()
        }
    }
}
