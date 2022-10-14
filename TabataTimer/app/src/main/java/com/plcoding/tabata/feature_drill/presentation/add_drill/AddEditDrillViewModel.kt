package com.plcoding.tabata.feature_drill.presentation.add_drill

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.tabata.feature_drill.domain.model.InvalidDrillException
import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.use_case.DrillUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditDrillViewModel @Inject constructor(
    private val drillUseCases: DrillUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentId: Int? = null

    private val _title = mutableStateOf(
        DrillTextFieldState(
            hint = "Enter title"
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

    private val _drillRestPeriods = mutableStateOf<Int>(1)
    val drillRestPeriods: State<Int> = _drillRestPeriods


    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

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
                        _drillSets.value = drill.sets
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditDrillEvent) {
        when (event) {
            is AddEditDrillEvent.EnteredTitle -> {
                _title.value = title.value.copy(
                    text = event.value
                )
            }
            is AddEditDrillEvent.ChangeTitleFocus -> {
                _title.value = title.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            title.value.text.isBlank()
                )
            }
            is AddEditDrillEvent.ChangeColor -> {
                _drillColor.value = event.color
            }
            is AddEditDrillEvent.Inc -> {
                when (event.type) {
                    "Sets" -> _drillSets.value += event.value
                    "Work" -> _drillWork.value += event.value
                    "Rest" -> _drillRest.value += event.value
                    "RestCount" -> _drillRestPeriods.value += event.value
                    "Preparation" -> _drillPrepare.value += event.value
                }
            }
            is AddEditDrillEvent.Dec -> {
                when (event.type) {
                    "Sets" -> _drillSets.value =
                        if (_drillSets.value - event.value < 1) 1 else _drillSets.value
                    "Work" -> _drillWork.value =
                        if (--_drillWork.value - event.value < 1) 1 else _drillWork.value
                    "Rest" -> _drillRest.value =
                        if (--_drillRest.value - event.value < 1) 1 else _drillRest.value
                    "RestCount" -> _drillRestPeriods.value =
                        if (_drillRestPeriods.value - event.value < 1) 1 else _drillRestPeriods.value
                    "Preparation" -> _drillPrepare.value =
                        if (--_drillPrepare.value - event.value < 1) 1 else _drillPrepare.value
                }

            }

            is AddEditDrillEvent.SaveDrill -> {
                viewModelScope.launch {
                    try {
                        drillUseCases.addDrill(
                            Workout(
                                title = title.value.text,
                                color = drillColor.value,
                                workInterval = drillWork.value,
                                restInterval = drillRest.value,
                                sets = drillSets.value,
                                restPeriods = drillRestPeriods.value,
                                prepInterval = drillPrepare.value,
                                id = currentId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveDrill)
                    } catch (e: InvalidDrillException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save drill"
                            )
                        )
                    }
                }
            }
        }


    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveDrill : UiEvent()
    }
}