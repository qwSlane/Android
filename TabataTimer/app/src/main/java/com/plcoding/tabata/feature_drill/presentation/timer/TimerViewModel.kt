package com.plcoding.tabata.feature_drill.presentation.timer

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.use_case.DrillUseCases
import com.plcoding.tabata.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val drillUseCases: DrillUseCases,
    savedStateHandle: SavedStateHandle,

    ) : ViewModel() {

    private val serviceIntent = Intent(TimerSettings.appcontext, TimerService::class.java)

    private var currentId: Int? = null

    val title = mutableStateOf(
        R.string.preparation
    )

    private val _drillColor = mutableStateOf<Int>(Workout.colors.random().toArgb())
    val drillColor: State<Int> = _drillColor

    val _allTime = mutableStateOf<Int>(0)
    var allTime:Int = 0

    private val _totalTime = mutableStateOf<String>("")
    val totalTime: State<String> = _totalTime

    val _index = mutableStateOf<Int>(0)

    lateinit var currentDrill: Workout

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
                    TimerSettings.currentTime.value = currentDrill.actions[0].second
                    toMinutes()
                }
            }
        }
    }


    fun toMinutes() {
        val minutes = _allTime.value / 60
        val sec = _allTime.value % 60
        _totalTime.value = "$minutes:$sec"
    }

    fun switchAction(value: Int) {
        _index.value += value
        stop()
        TimerSettings.currentTime.value = currentDrill.actions[_index.value].second
        start()
        title.value = currentDrill.actions[_index.value].first
        Log.i("Type", title.value.toString())
    }

    fun start() {
        var action: IntArray = intArrayOf()
        for (act in currentDrill.actions){
            action += intArrayOf(act.second)
        }
        serviceIntent.putExtra(TimerService.TIME_EXTRA, action)
        TimerSettings.appcontext.startService(serviceIntent)
    }

    fun stop() {
        TimerSettings.appcontext.stopService(serviceIntent)
    }

}