package com.example.MillenniumTravel.models

data class HighScore private constructor(val score: Int, val location: Coordinates) {
    class Builder(var score: Int = 0, var location: Coordinates = Coordinates.Builder().build()) {
        fun score(score: Int) = apply {this.score = score}
        fun location(lat: Double, lng: Double) = apply {location = Coordinates.Builder(lat, lng).build() }
        fun build() = HighScore(score, location)
    }
}
