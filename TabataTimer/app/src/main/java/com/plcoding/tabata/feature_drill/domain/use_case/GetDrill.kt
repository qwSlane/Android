package com.plcoding.tabata.feature_drill.domain.use_case

import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.repository.DrillRepository

class GetDrill(
    private val repository: DrillRepository
) {
    suspend operator fun invoke(id: Int): Workout?{
        return repository.getDrillById(id)
    }
}