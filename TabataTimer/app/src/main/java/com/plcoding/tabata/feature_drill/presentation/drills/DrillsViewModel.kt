package com.plcoding.tabata.feature_drill.presentation.drills

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.use_case.DrillUseCases
import com.plcoding.tabata.feature_drill.domain.util.DrillOrder
import com.plcoding.tabata.feature_drill.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrillsViewModel @Inject constructor(
    private val drillUseCases: DrillUseCases
) : ViewModel() {

    private val _state = mutableStateOf(DrillsState())
    val state: State<DrillsState> = _state

    private var recentlyDeletedDrill: Workout? = null

    private var getDrillsJob: Job? = null

    init {
        getDrills(DrillOrder.Date(OrderType.Descending))
    }


    fun onEvent(event: DrillsEvent) {
        when (event) {
            is DrillsEvent.Order -> {
                if (state.value.drillOrder::class == event.drillOrder::class &&
                    state.value.drillOrder.orderType == event.drillOrder.orderType
                ) {
                    return
                }

                getDrills(event.drillOrder)
            }
            is DrillsEvent.DeleteDrill -> {
                viewModelScope.launch {
                    drillUseCases.deleteDrill(event.drill)
                    recentlyDeletedDrill = event.drill
                }
            }
            is DrillsEvent.RestoreDrill -> {
                viewModelScope.launch {
                    drillUseCases.addDrill(recentlyDeletedDrill ?: return@launch)
                    recentlyDeletedDrill = null
                }
            }
            is DrillsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getDrills(drillOrder: DrillOrder){
        getDrillsJob?.cancel()
        getDrillsJob = drillUseCases.getDrills(drillOrder)
            .onEach { drills ->
                _state.value = state.value.copy(
                    drills = drills,
                    drillOrder = drillOrder
                )
            }
            .launchIn(viewModelScope)
    }
}