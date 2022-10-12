package com.plcoding.tabata.feature_drill.domain.repository

import com.plcoding.tabata.feature_drill.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface DrillRepository {

    fun getDrills(): Flow<List<Workout>>

    suspend fun getDrillById(id:Int): Workout?

    suspend fun insertDrill(drill: Workout)

    suspend fun deleteDrill(drill: Workout)

}