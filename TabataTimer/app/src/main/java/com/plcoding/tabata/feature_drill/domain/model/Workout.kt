package com.plcoding.tabata.feature_drill.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.plcoding.tabata.ui.theme.*

@Entity
data class Workout(
    val sets: Int,
    val title: String,
    val actions: ArrayList<Pair<Int, Int>>,
    val color: Int,
    @PrimaryKey val id: Int? = null

) {
    companion object {
        val colors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

public class WorkoutConverter {
    @TypeConverter
    fun fromActions(value: ArrayList<Pair<Int, Int>>): String {
        val gson = Gson()
        val type =
            object : TypeToken<ArrayList<Pair<Int, Int>>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toActions(value: String): ArrayList<Pair<Int, Int>> {
        val gson = Gson()
        val type =
            object : TypeToken<ArrayList<Pair<Int, Int>>>() {}.type
        return gson.fromJson(value, type)
    }
}

class InvalidDrillException(message: String) : Exception(message)

