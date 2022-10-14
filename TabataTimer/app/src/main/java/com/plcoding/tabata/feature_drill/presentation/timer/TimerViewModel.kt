package com.plcoding.tabata.feature_drill.presentation.timer

import android.content.BroadcastReceiver
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.use_case.DrillUseCases
import com.plcoding.tabata.feature_drill.presentation.Preferences.PreferencesSerializer
import com.plcoding.tabata.feature_drill.presentation.add_drill.DrillTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import com.plcoding.tabata.R
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val drillUseCases: DrillUseCases,
    savedStateHandle: SavedStateHandle,

    ) : ViewModel() {

    private var currentId: Int? = null

    var list: List<Pair<Int, String>> = emptyList()

    private val _title = mutableStateOf(
        DrillTextFieldState(
            text = "Current"
        )
    )

    val title: State<DrillTextFieldState> = _title

    private val _drillColor = mutableStateOf<Int>(Workout.colors.random().toArgb())
    val drillColor: State<Int> = _drillColor

    val _allTime = mutableStateOf<Int>(1)

    private val _totalTime = mutableStateOf<String>("")
    val totalTime: State<String> = _totalTime

    private val _initialValue = mutableStateOf<Int>(1)
    val initialValue: State<Int> = _initialValue

    private lateinit var currentDrill: Workout

    init {
        savedStateHandle.get<Int>("drillId")?.let { drillId ->
            if (drillId != -1) {
                viewModelScope.launch {
                    drillUseCases.getDrill(drillId)?.also { drill ->
                        currentId = drill.id
                        currentDrill = Workout(
                            drill.sets,
                            drill.title,
                            drill.workInterval,
                            drill.restInterval,
                            drill.restPeriods,
                            drill.prepInterval,
                            drill.color
                        )

                        Log.i("time:", currentDrill.workInterval.toString())
                    }
                }

                TimerSettings.initialTime = 10.0
                TimerSettings.currentTime.value = 10.0


                toMinutes()
            }
        }
    }


    fun toMinutes() {
        val minutes = _allTime.value / 60
        val sec = _allTime.value % 60
        _totalTime.value = "$minutes:$sec"

    }

    fun createList() {
//        for (i in 0 until ) {
//            list.plus(listOf(Pair(_drillPrepare.value, "str")))
//        }
    }

}