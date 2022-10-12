package com.plcoding.tabata.feature_drill.domain.use_case

import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.repository.DrillRepository

class DeleteDrill(
    private val repository: DrillRepository
) {

    suspend operator fun invoke(drill: Workout){
        repository.deleteDrill(drill)
    }
}