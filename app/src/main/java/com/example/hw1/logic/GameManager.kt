package com.example.hw1.logic

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
    }

    fun proceed() {
        for (col in 0..<currentEnemyPos.size) {
            currentEnemyPos[col][currentEnemyPos[col].size - 1] = 0
            for (row in (currentEnemyPos[col].size - 2) downTo 0) {
                if (currentEnemyPos[col][row] == 1) {
                    currentEnemyPos[col][row + 1] = 1
                    currentEnemyPos[col][row] = 0
                }
            }
        }
        val randomLane = (0..<laneCount).random()
        if ((0..1).random() == 1) {
            currentEnemyPos[randomLane][0] = 1
        }
    }

}
