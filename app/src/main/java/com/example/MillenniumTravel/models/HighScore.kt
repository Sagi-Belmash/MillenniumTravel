package com.example.MillenniumTravel.models

import com.google.android.gms.maps.model.LatLng

data class HighScore private constructor(val score: Int, val location: LatLng) {
    class Builder(var score: Int = 0, var location: LatLng = LatLng(0.0, 0.0)) {
        fun score(score: Int) = apply {this.score = score}
        fun location(lat: Double, lng: Double) = apply {location = LatLng(lat, lng) }
        fun build() = HighScore(score, location)
    }
}
