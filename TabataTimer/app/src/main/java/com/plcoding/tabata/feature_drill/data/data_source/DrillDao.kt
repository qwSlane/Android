package com.plcoding.tabata.feature_drill.data.data_source

import androidx.room.*
import com.plcoding.tabata.feature_drill.domain.model.Workout
import kotlinx.coroutines.flow.Flow


@Dao
interface DrillDao {

    @Query("SELECT * FROM workout")
    fun getDrills(): Flow<List<Workout>>

    @Query("SELECT * FROM workout WHERE id = :id")
    suspend fun getDrillById(id: Int): Workout

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrill(drill: Workout)

    @Delete
    suspend fun deleteDrill(drill: Workout)

}