package com.plcoding.tabata.feature_drill.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.plcoding.tabata.ui.theme.*

@Entity
data class Workout(
    val sets: Int,
    val title: String,
    val workInterval: Int,
    val restInterval: Int,
    val restPeriods: Int,
    val prepInterval: Int,
    val color: Int,
    @PrimaryKey val id: Int? = null

){
    companion object{
        val colors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink )
    }
}

class InvalidDrillException(message: String): Exception(message)

