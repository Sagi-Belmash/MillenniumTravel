package com.example.MillenniumTravel.models

data class Coordinates private constructor(val lat: Double, val lng: Double) {
    class Builder(var lat: Double = 0.0, var lng: Double = 0.0) {
        fun lat(lat: Double) = apply { this.lat = lat }
        fun lng(lng: Double) = apply { this.lng = lng }
        fun build() = Coordinates(lat, lng)
    }
}
