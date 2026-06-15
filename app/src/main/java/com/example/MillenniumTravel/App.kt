package com.example.MillenniumTravel

import android.app.Application
import com.example.MillenniumTravel.utilities.BackgroundMusicPlayer
import com.example.MillenniumTravel.utilities.SharedPreferencesManager
import com.example.MillenniumTravel.utilities.SignalManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
        SignalManager.init(this)
        BackgroundMusicPlayer.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        BackgroundMusicPlayer.getInstance().stopMusic()
    }
}