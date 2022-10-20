package com.plcoding.tabata.feature_drill.presentation.timer

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.tabata.MainActivity
import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.use_case.DrillUseCases
import com.plcoding.tabata.R
import com.plcoding.tabata.TimerWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val drillUseCases: DrillUseCases,
    savedStateHandle: SavedStateHandle,

    ) : ViewModel() {

    private val serviceIntent = Intent(TimerSettings.appcontext, TimerService::class.java)

    private var currentId: Int? = null
    private lateinit var action: IntArray

    val title = mutableStateOf(
        R.string.preparation
    )

    private val _drillColor = mutableStateOf<Int>(Workout.colors.random().toArgb())
    val drillColor: State<Int> = _drillColor

    val _allTime = mutableStateOf<Int>(0)
    var allTime: Int = 0

    private val _totalTime = mutableStateOf<String>("")
    val totalTime: State<String> = _totalTime


    lateinit var currentDrill: Workout

    var actionsCount: Int = 0

    var isRun = false


    fun isDrillInitialized() = ::currentDrill.isInitialized

    init {
        savedStateHandle.get<Int>("drillId")?.let { drillId ->
            if (drillId != -1) {
                viewModelScope.launch {
                    drillUseCases.getDrill(drillId)?.also { drill ->
                        currentId = drill.id
                        currentDrill = drill
                    }
                    for (action in currentDrill.actions) {
                        _allTime.value += action.second
                    }
                    _allTime.value *= currentDrill.sets
                    allTime = _allTime.value
                    TimerSettings.currentIndex.value = 0
                    TimerSettings.currentTime.value = currentDrill.actions[0].second
                    TimerSettings.elapsedTime.value = 0
                    toMinutes()

                    action = intArrayOf()
                    for (act in currentDrill.actions) {
                        action += intArrayOf(act.second)
                    }
                    TimerSettings.elapsed = action
                    TimerSettings.initial = action
                    actionsCount = (action.size * currentDrill.sets) - 1


                }
            }
        }
    }


     fun stopStart(): BroadcastReceiver? {
        Log.i("sdfsf", "LOLO")
        stop()
        return null
    }

    fun toMinutes() {
        val minutes = _allTime.value / 60
        val sec = _allTime.value % 60
        if (sec < 10) {
            _totalTime.value = "$minutes:0$sec"
        } else {
            _totalTime.value = "$minutes:$sec"
        }
    }

    fun start() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, TimerSettings.elapsed)
        TimerSettings.appcontext.startService(serviceIntent)
    }

    fun skipBack() {

        TimerSettings.appcontext.stopService(serviceIntent)
        if (currentDrill.actions[TimerSettings.currentIndex.value].second - TimerSettings.currentTime.value > 1) {
            var time: Int = 0
            for (i in 1..TimerSettings.currentIndex.value) {
                time += action[i]
            }
            TimerSettings.elapsed[TimerSettings.currentIndex.value] =
                currentDrill.actions[TimerSettings.currentIndex.value].second
            TimerSettings.currentTime.value =
                currentDrill.actions[TimerSettings.currentIndex.value].second
            TimerSettings.elapsedTime.value = time

        } else {

            if (TimerSettings.currentIndex.value > 0) {
                var time: Int = 0

                for (i in 1 until TimerSettings.currentIndex.value) {
                    time += action[i]
                }
                TimerSettings.elapsed[TimerSettings.currentIndex.value - 1] =
                    currentDrill.actions[TimerSettings.currentIndex.value - 1].second
                TimerSettings.elapsedTime.value = time
                TimerSettings.currentIndex.value -= 1

            } else {
                Toast.makeText(TimerSettings.appcontext, "You are on the first element", 1000)
                    .show()
            }
        }
        start()
    }

    fun skipNext() {
        TimerSettings.appcontext.stopService(serviceIntent)
        var time: Int = 0

        if (TimerSettings.currentIndex.value + 1 <= actionsCount) {

            for (i in 0..TimerSettings.currentIndex.value) {
                time += action[i]
            }
            TimerSettings.currentIndex.value += 1
            TimerSettings.elapsedTime.value = time
            start()

        } else {

            TimerSettings.currentIndex.value = actionsCount
            TimerSettings.elapsedTime.value = allTime
            TimerSettings.currentTime.value = 0
        }
    }

    fun stop() {
        TimerSettings.appcontext.stopService(serviceIntent)
        TimerSettings.elapsed[TimerSettings.currentIndex.value] =
            TimerSettings.currentTime.value
    }


    fun exit() {
        TimerSettings.appcontext.stopService(serviceIntent)
    }

}