package com.plcoding.tabata.feature_drill.presentation.timer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.*


class TimerSettings(){
    companion object{
        var initialTime:Double = 0.0
        var currentTime = mutableStateOf<Double>(0.0)
        lateinit var appcontext: Context
    }
}

class TimerService() : Service() {

    private lateinit var player: MediaPlayer
    private val timer = Timer()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val time = intent.getDoubleExtra(TIME_EXTRA, 0.0)
        timer.scheduleAtFixedRate(TimeTask(time), 1000, 1000)
        return START_NOT_STICKY
    }

    private inner class TimeTask(private var time:Double): TimerTask(){
        override fun run() {
            val intent = Intent(TIME_UPDATED)
            time--
            Log.i("TIME", time.toString())
            intent.putExtra(TIME_EXTRA, time)
            sendBroadcast(intent)
        }
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()

    }


    override fun onBind(p0: Intent?): IBinder? = null

    companion object {
        const val TIME_UPDATED = "timerUpdated"
        const val TIME_EXTRA = "timeExtra"
    }

}