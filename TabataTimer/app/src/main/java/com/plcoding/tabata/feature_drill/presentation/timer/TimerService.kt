package com.plcoding.tabata.feature_drill.presentation.timer

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.runtime.mutableStateOf
import com.plcoding.tabata.R
import com.plcoding.tabata.TimerWidget
import com.plcoding.tabata.TimerWidget.Companion.INTENT_COMMAND
import com.plcoding.tabata.TimerWidget.Companion.INTENT_COMMAND_NEXT
import com.plcoding.tabata.TimerWidget.Companion.INTENT_COMMAND_PAUSE
import com.plcoding.tabata.TimerWidget.Companion.INTENT_COMMAND_PREV
import com.plcoding.tabata.feature_drill.presentation.Preferences.PreferencesSerializer.Companion.context
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate


class TimerSettings() {
    companion object {
        var elapsedTime = mutableStateOf<Int>(0)
        var currentIndex = mutableStateOf<Int>(0)
        var currentTime = mutableStateOf<Int>(0)
        lateinit var elapsed: IntArray
        lateinit var initial: IntArray

        lateinit var appcontext: Context
        var isRun: Boolean = true
    }
}

class TimerService() : Service() {

    private var player: MediaPlayer = MediaPlayer()
    private var timer: Timer? = Timer()

    private var isRun: Boolean = false


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val time = intent.getIntArrayExtra(TIME_EXTRA)
        if (time != null) {
            timer?.scheduleAtFixedRate(TimeTask(time), 1000, 1000)
        }
        player =
            MediaPlayer.create(TimerSettings.appcontext, Settings.System.DEFAULT_NOTIFICATION_URI)


        val command = intent.getStringExtra(INTENT_COMMAND)
        Log.i("Intent", command.toString())
        if (command == INTENT_COMMAND_PAUSE) {
            Log.i("qwe","qwe")
            stop()
        }else if (command == INTENT_COMMAND_PREV){
            prev()
        }else if (command == INTENT_COMMAND_NEXT){
            next()
        }




        return START_NOT_STICKY
    }


    private inner class TimeTask(private var time: IntArray?) : TimerTask() {
        override fun run() {


            val remoteViews = RemoteViews(context!!.packageName, R.layout.timer_widget)
            val thisWidget = ComponentName(context!!, TimerWidget::class.java)
            remoteViews.setTextViewText(
                R.id.appwidget_text,
                TimerSettings.currentTime.value.toString()
            )
            AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, remoteViews)

            if (TimerSettings.currentIndex.value == time!!.size) {
                timer?.cancel()
                return
            }
            time!![TimerSettings.currentIndex.value] -= 1
            TimerSettings.elapsedTime.value++
            if (time!![TimerSettings.currentIndex.value] == 0) {
                player.start()
                TimerSettings.currentIndex.value += 1

            }
            if (TimerSettings.currentIndex.value == time!!.size) {
                remoteViews.setTextViewText(
                    R.id.appwidget_text,
                    "FINISH"
                )
                timer?.cancel()
                return
            } else {
                TimerSettings.currentTime.value = time!![TimerSettings.currentIndex.value]
            }


        }
    }

    private fun stop() {
        if (TimerSettings.isRun) {
            Log.i("adsasd", "adsasd")
            timer?.cancel()
            TimerSettings.isRun = false
            TimerSettings.elapsed[TimerSettings.currentIndex.value] =
                TimerSettings.currentTime.value
        } else {
            this.timer = null
            this.timer = Timer()
            TimerSettings.isRun = true
            TimerSettings.appcontext.startService(
                Intent(
                    TimerSettings.appcontext,
                    TimerService::class.java
                ).putExtra(TimerService.TIME_EXTRA, TimerSettings.elapsed)
            )
        }
        isRun = !isRun
    }
    private fun prev(){
        Log.i("PREV","PR")
        if (TimerSettings.currentIndex.value == 0){
            timer?.cancel()
            TimerSettings.appcontext.startService(
                Intent(
                    TimerSettings.appcontext,
                    TimerService::class.java
                ).putExtra(TimerService.TIME_EXTRA, TimerSettings.initial.clone())
            )

        }else{
            TimerSettings.currentIndex.value -=1
        }
    }
    private fun next(){

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun stopPlayer() = GlobalScope.launch {
        delay(2000)
        player.stop()
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()

    }


    override fun onBind(p0: Intent?): IBinder? = null

    companion object {
        const val TIME_UPDATED = "timerUpdated"
        const val TIME_EXTRA = "timeExtra"
    }

}