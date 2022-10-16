package com.plcoding.tabata.feature_drill.presentation.timer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class TimerSettings(){
    companion object{
        var elapsedTime = mutableStateOf<Int>(0)
        var currentIndex = mutableStateOf<Int>(0)
        var currentTime = mutableStateOf<Int>(0)
        lateinit var elapsed: IntArray
        lateinit var appcontext: Context
    }
}

class TimerService() : Service() {

    private var player: MediaPlayer = MediaPlayer()
    private val timer = Timer()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val time = intent.getIntArrayExtra(TIME_EXTRA)
        player = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI)
        timer.scheduleAtFixedRate(TimeTask(time), 1000, 1000)

        return START_NOT_STICKY
    }

    private inner class TimeTask(private var time: IntArray?): TimerTask(){
        override fun run() {
            val intent = Intent(TIME_UPDATED)
            time!![TimerSettings.currentIndex.value] -=1
            TimerSettings.elapsedTime.value++
            if(time!![TimerSettings.currentIndex.value] == 0){
                player.start()
                TimerSettings.currentIndex.value +=1
//                stopPlayer()
            }
            TimerSettings.currentTime.value = time!![TimerSettings.currentIndex.value]

//            intent.putExtra(TIME_EXTRA, intArrayOf())
            sendBroadcast(intent)
        }
    }

     @OptIn(DelicateCoroutinesApi::class)
     fun stopPlayer() = GlobalScope.launch{
        delay(2000)
        player.stop()
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