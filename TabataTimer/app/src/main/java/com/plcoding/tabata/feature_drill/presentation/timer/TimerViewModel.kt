package com.plcoding.tabata.feature_drill.presentation.timer

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.use_case.DrillUseCases
import com.plcoding.tabata.feature_drill.presentation.add_drill.AddEditDrillViewModel
import com.plcoding.tabata.feature_drill.presentation.add_drill.DrillTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val drillUseCases: DrillUseCases,
    savedStateHandle: SavedStateHandle

) : ViewModel() {

    private var currentId: Int? = null

    private val _title = mutableStateOf(
        DrillTextFieldState(
            text = "Current"
        )
    )

    val title: State<DrillTextFieldState> = _title

    private val _drillColor = mutableStateOf<Int>(Workout.colors.random().toArgb())
    val drillColor: State<Int> = _drillColor

    private val _drillSets = mutableStateOf<Int>(1)
    val drillSets: State<Int> = _drillSets

    private val _drillPrepare = mutableStateOf<Int>(10)
    val drillPrepare: State<Int> = _drillPrepare

    private val _drillWork = mutableStateOf<Int>(10)
    val drillWork: State<Int> = _drillWork

    private val _drillRest = mutableStateOf<Int>(10)
    val drillRest: State<Int> = _drillRest

    private val _drillRestCount = mutableStateOf<Int>(1)
    val drillRestCount: State<Int> = _drillRestCount

    private val _drillRestPeriods = mutableStateOf<Int>(1)
    val drillRestPeriods: State<Int> = _drillRestPeriods

    private val _allTime = mutableStateOf<String>("")
    val allTime: State<String> = _allTime

    init {
        savedStateHandle.get<Int>("drillId")?.let { drillId ->
            if (drillId != -1) {
                viewModelScope.launch {
                    drillUseCases.getDrill(drillId)?.also { drill ->
                        currentId = drill.id
                        _title.value = title.value.copy(
                            text = drill.title,
                            isHintVisible = false
                        )
                        _drillColor.value = drill.color
                        _drillPrepare.value = drill.prepInterval
                        _drillRest.value = drill.restInterval
                        _drillWork.value = drill.workInterval
                        _drillRestCount.value = drill.restPeriods
                        _drillSets.value = drill.sets
                    }
                }
            }
        }

        val time =
            (_drillPrepare.value + _drillRest.value * _drillRestCount.value + _drillWork.value) * _drillSets.value
        toMinutes(time)
    }



    private fun toMinutes(value: Int) {
        val minutes = value / 60
        val sec = value % 60
        _allTime.value = "$minutes:$sec"

    }

}