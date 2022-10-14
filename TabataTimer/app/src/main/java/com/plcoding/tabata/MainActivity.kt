package com.plcoding.tabata

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.plcoding.tabata.feature_drill.presentation.Preferences.PreferencesSerializer
import com.plcoding.tabata.feature_drill.presentation.Preferences.presentation.PreferencesScreen
import com.plcoding.tabata.feature_drill.presentation.Preferences.presentation.PreferencesViewModel
import com.plcoding.tabata.feature_drill.presentation.Preferences.presentation.setLanguage
import com.plcoding.tabata.feature_drill.presentation.add_drill.AddEditDrillScreen
import com.plcoding.tabata.feature_drill.presentation.drills.components.DrillsScreen
import com.plcoding.tabata.feature_drill.presentation.timer.TimerScreen
import com.plcoding.tabata.feature_drill.presentation.timer.TimerService
import com.plcoding.tabata.feature_drill.presentation.timer.TimerSettings
import com.plcoding.tabata.feature_drill.presentation.util.Screen
import com.plcoding.tabata.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setPreferences()
            var theme = remember { mutableStateOf(PreferencesSerializer.preferences.theme) }
            var fontSize = remember { mutableStateOf(PreferencesSerializer.preferences.fontSize) }

            AppTheme(theme.value, fontSize.value) {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.DrillsScreen.route
                    ) {
                        composable(route = Screen.DrillsScreen.route) {
                            DrillsScreen(navController = navController)
                        }
                        composable(route = Screen.PreferencesScreen.route) {
                            PreferencesScreen(
                                navController = navController,
                                PreferencesViewModel(theme, fontSize)
                            )
                        }
                        composable(
                            route = Screen.AddEditDrillScreen.route +
                                    "?drillId={drillId}&drillColor={drillColor}",
                            arguments = listOf(
                                navArgument(
                                    name = "drillId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "drillColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            val color = it.arguments?.getInt("drillColor") ?: -1
                            AddEditDrillScreen(
                                navController = navController,
                                drillColor = color
                            )
                        }
                        composable(
                            route = Screen.TimerScreen.route +
                                    "?drillId={drillId}&drillColor={drillColor}",
                            arguments = listOf(
                                navArgument(
                                    name = "drillId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "drillColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            val color = it.arguments?.getInt("drillColor") ?: -1
                            TimerScreen(navController = navController, drillColor = color)
                        }

                    }
                }
            }
        }
    }

    @Composable
    fun setPreferences() {
        val context = LocalContext.current
        val config = LocalConfiguration.current
        PreferencesSerializer.saveContext(context, config)
        PreferencesSerializer.readPreferences()
        setLanguage(PreferencesSerializer.preferences.language)

        registerReceiver(updateTime, IntentFilter(TimerService.TIME_UPDATED))
        TimerSettings.appcontext = applicationContext
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            TimerSettings.currentTime.value =
                intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
        }


    }
}