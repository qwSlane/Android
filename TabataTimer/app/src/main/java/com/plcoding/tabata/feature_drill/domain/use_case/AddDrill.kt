package com.plcoding.tabata.feature_drill.domain.use_case

import com.plcoding.tabata.feature_drill.domain.model.InvalidDrillException
import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.repository.DrillRepository

class AddDrill(
    private val repository: DrillRepository
) {

    suspend operator fun invoke(drill: Workout){

        @Throws(InvalidDrillException::class)
        if(drill.title.isBlank()){
            throw InvalidDrillException("The title of the drill can't be empty.")
        }

        repository.insertDrill(drill)
    }
}