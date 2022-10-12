package com.plcoding.tabata.feature_drill.domain.use_case

import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.repository.DrillRepository
import com.plcoding.tabata.feature_drill.domain.util.DrillOrder
import com.plcoding.tabata.feature_drill.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDrills(
    private val repository: DrillRepository
) {

    operator fun invoke(
        drillOrder: DrillOrder = DrillOrder.Date(OrderType.Descending)
    ): Flow<List<Workout>> {
        return repository.getDrills().map { drills ->
            when (drillOrder.orderType) {
                is OrderType.Ascending -> {
                    when(drillOrder){
                        is DrillOrder.Title -> drills.sortedBy { it.title.lowercase() }
                        is DrillOrder.Date -> drills.sortedBy { it.sets }
                        is DrillOrder.Color -> drills.sortedBy { it.color }
                    }
                }
                is OrderType.Descending -> {
                    when(drillOrder){
                        is DrillOrder.Title -> drills.sortedByDescending { it.title.lowercase() }
                        is DrillOrder.Date -> drills.sortedByDescending { it.sets }
                        is DrillOrder.Color -> drills.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}