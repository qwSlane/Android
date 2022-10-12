package com.plcoding.tabata.feature_drill.presentation.drills

import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.util.DrillOrder

sealed class DrillsEvent{
    data class Order(val drillOrder: DrillOrder): DrillsEvent()
    data class DeleteDrill(val drill : Workout): DrillsEvent()
    object RestoreDrill: DrillsEvent()
    object ToggleOrderSection: DrillsEvent()
}
