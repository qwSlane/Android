package com.plcoding.tabata.di

import android.app.Application
import androidx.room.Room
import com.plcoding.tabata.feature_drill.data.data_source.DrillDatabase
import com.plcoding.tabata.feature_drill.data.repository.DrillRepositoryImpl
import com.plcoding.tabata.feature_drill.domain.repository.DrillRepository
import com.plcoding.tabata.feature_drill.domain.use_case.AddDrill
import com.plcoding.tabata.feature_drill.domain.use_case.DeleteDrill
import com.plcoding.tabata.feature_drill.domain.use_case.DrillUseCases
import com.plcoding.tabata.feature_drill.domain.use_case.GetDrills
import com.plcoding.tabata.feature_drill.domain.use_case.GetDrill
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDrillDatabase(app: Application): DrillDatabase{
        return Room.databaseBuilder(
            app,
            DrillDatabase::class.java,
            DrillDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDrillRepository(db: DrillDatabase)  : DrillRepository{
        return DrillRepositoryImpl(db.drillDao)
    }

    @Provides
    @Singleton
    fun provideDrillUseCases(repository: DrillRepository): DrillUseCases{
        return  DrillUseCases(
            getDrills = GetDrills(repository),
            deleteDrill = DeleteDrill(repository),
            addDrill = AddDrill(repository),
            getDrill = GetDrill(repository)
        )
    }
}