package com.plcoding.tabata.feature_drill.presentation.add_drill

import androidx.compose.runtime.*
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
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

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

    private var _items = mutableListOf(
        Pair(mutableStateOf("Preparation"), mutableStateOf(10)),
        Pair(mutableStateOf("Work"), mutableStateOf(10)),
        Pair(mutableStateOf("Rest"), mutableStateOf(10)),
    )
    var items by mutableStateOf(_items)

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
                        items.clear()
                        for(action in drill.actions){
                            items =
                                (items + listOf(Pair(mutableStateOf(action.first), mutableStateOf(action.second)))) as MutableList<Pair<MutableState<String>, MutableState<Int>>>
                        }
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
            is AddEditDrillEvent.Dec -> {
                if (items[event.value].second.value - 1 < 1) {
                    items[event.value].second.value = 1
                } else {
                    items[event.value].second.value--
                }
            }
            is AddEditDrillEvent.Inc -> {
                items[event.value].second.value += 1
            }
            is AddEditDrillEvent.CycleInc -> {
                _drillSets.value += event.value
            }
            is AddEditDrillEvent.CycleDec -> {
                if (_drillSets.value - event.value < 1) {
                    _drillSets.value = 1
                } else {
                    _drillSets.value--
                }
            }
            is AddEditDrillEvent.AddDrill -> {
                items = (items + listOf(Pair(mutableStateOf("Preparation"), mutableStateOf(10)))) as MutableList<Pair<MutableState<String>, MutableState<Int>>>
            }
            is AddEditDrillEvent.Delete ->{
                items = items.toMutableList().also {
                    it.removeAt(index = event.index)
                }
            }
            is AddEditDrillEvent.SaveDrill -> {
                viewModelScope.launch {
                    try {
                        var actions: List<Pair<String, Int>> = emptyList()
                        for(item in items){
                            actions = actions + listOf(Pair(item.first.value, item.second.value))
                        }
                        drillUseCases.addDrill(
                            Workout(
                                title = title.value.text,
                                color = drillColor.value,
                                sets = drillSets.value,
                                actions = actions as ArrayList<Pair<String, Int>>,
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

//


    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveDrill : UiEvent()
    }
}