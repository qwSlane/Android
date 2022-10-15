package com.plcoding.tabata.feature_drill.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.domain.model.WorkoutConverter

@Database(
    entities = [Workout::class],
    version = 2
)

@TypeConverters(WorkoutConverter::class)
abstract class DrillDatabase: RoomDatabase() {

    abstract val drillDao: DrillDao

    companion object{
        const val DATABASE_NAME = "drills_db"
    }

}