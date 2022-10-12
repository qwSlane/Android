package com.plcoding.tabata.feature_drill.presentation.drills

import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.util.DrillOrder
import com.plcoding.tabata.feature_drill.domain.util.OrderType

data class DrillsState(
    val drills: List<Workout> = emptyList(),
    val drillOrder: DrillOrder = DrillOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
