package com.plcoding.tabata.feature_drill.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.plcoding.tabata.feature_drill.domain.model.Workout

@Database(
    entities = [Workout::class],
    version = 3
)


abstract class DrillDatabase: RoomDatabase() {

    abstract val drillDao: DrillDao

    companion object{
        const val DATABASE_NAME = "drills_db"
    }

}