package com.example.MillenniumTravel.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.MillenniumTravel.interfaces.Callback_TiltCallback
import kotlin.math.abs

class TiltDetector(context: Context, private val tiltCallback: Callback_TiltCallback?) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor
    private lateinit var sensorEventListener : SensorEventListener

    private var timestamp: Long = 0L

    init {
        initEventListener()
    }

    private fun initEventListener() {
        sensorEventListener = object:SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // pass
            }

            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                calculateTilt(x, y)
            }

            private fun calculateTilt(x: Float, y: Float) {
                if (System.currentTimeMillis() - timestamp >= 300L) {
                    if (abs(x) >= 3.0f) {
                        tiltCallback?.tiltX(x)
                        timestamp = System.currentTimeMillis()
                    }
                    if (abs(y) >= 0.5f) {
                        tiltCallback?.tiltY(y)
                    }
                }
            }
        }
    }

    fun stop() {
        sensorManager.unregisterListener(sensorEventListener, sensor)
    }

    fun start() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
}