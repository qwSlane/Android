package com.plcoding.tabata.feature_drill.presentation.util

sealed class Screen(val route: String){
    object DrillsScreen: Screen("drills_screen")
    object AddEditDrillScreen: Screen("add_edit_drill_screen")
    object PreferencesScreen: Screen("preferences_screen")
    object TimerScreen: Screen("timer_screen")
}
