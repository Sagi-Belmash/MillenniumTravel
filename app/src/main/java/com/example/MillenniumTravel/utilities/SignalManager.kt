package com.example.MillenniumTravel.utilities

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import java.lang.ref.WeakReference

class SignalManager private constructor(context: Context) {
    private val contextRef = WeakReference(context)

    enum class ToastLength(val length: Int) {
        SHORT(Toast.LENGTH_SHORT),
        LONG(Toast.LENGTH_LONG)
    }

    companion object {
        @Volatile
        private var instance: SignalManager? = null

        fun init(context: Context): SignalManager {
            return instance ?: synchronized(this) {
                instance ?: SignalManager(context).also { instance = it }
            }
        }

        fun getInstance(): SignalManager {
            return instance ?: throw IllegalStateException(
                "SignalManager must be initialized by calling init(context) before use."
            )
        }
    }

    fun toast(text: String, duration: ToastLength) {
        contextRef.get()?.let { context ->
            Toast
                .makeText(
                    context,
                    text,
                    duration.ordinal
                )
                .show()
        }
    }

    fun vibrate() {
        contextRef.get()?.let { context ->
            val vibratorManager =
                context.getSystemService(
                    Context.VIBRATOR_MANAGER_SERVICE
                ) as VibratorManager
            val vibrator: Vibrator = vibratorManager.defaultVibrator

            val vibrationEffect =
                VibrationEffect
                    .createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
            vibrator.vibrate(vibrationEffect)
        }

    }
}