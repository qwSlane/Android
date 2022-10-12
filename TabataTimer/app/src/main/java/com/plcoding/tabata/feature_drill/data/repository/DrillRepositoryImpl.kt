package com.plcoding.tabata.feature_drill.data.repository

import com.plcoding.tabata.feature_drill.data.data_source.DrillDao
import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.repository.DrillRepository
import kotlinx.coroutines.flow.Flow

class DrillRepositoryImpl(
    private val dao: DrillDao
): DrillRepository {
    override fun getDrills(): Flow<List<Workout>> {
        return dao.getDrills()
    }

    override suspend fun getDrillById(id: Int): Workout? {
        return dao.getDrillById(id)
    }

    override suspend fun insertDrill(drill: Workout) {
        return dao.insertDrill(drill)
    }

    override suspend fun deleteDrill(drill: Workout) {
        return dao.deleteDrill(drill)
    }
}