package com.example.MillenniumTravel.logic

import kotlin.math.max

class GameManager(private val lifeCount: Int, private val laneCount: Int, private val laneLength: Int) {
    var hitCount: Int = 0

    var currentEnemyPos: Array<IntArray> = Array(laneCount) { IntArray(laneLength) }
    var currentPlayerPos: Int = laneCount / 2
    val isGameOver: Boolean
        get() = hitCount == lifeCount

    fun movePlayer(dir: Int) {
        currentPlayerPos = (currentPlayerPos + dir).coerceIn(0, laneCount - 1)
    }

    fun checkHit() {
        if (currentEnemyPos[currentPlayerPos][laneLength - 1] == 1) hitCount++
        else if (currentEnemyPos[currentPlayerPos][laneLength - 1] == 2) hitCount = max(hitCount - 1, 0)
    }

    fun proceed() {
        for (col in 0..<currentEnemyPos.size) {
            currentEnemyPos[col][currentEnemyPos[col].size - 1] = 0
            for (row in (currentEnemyPos[col].size - 2) downTo 0) {
                if (currentEnemyPos[col][row] != 0) {
                    currentEnemyPos[col][row + 1] = currentEnemyPos[col][row]
                    currentEnemyPos[col][row] = 0
                }
            }
        }
        val randomLane = (0..<laneCount).random()
        if ((0..1).random() == 0) {
            currentEnemyPos[randomLane][0] = 1
        }
        else if ((0..9).random() == 0) {
            currentEnemyPos[randomLane][0] = 2
        }
    }

}
