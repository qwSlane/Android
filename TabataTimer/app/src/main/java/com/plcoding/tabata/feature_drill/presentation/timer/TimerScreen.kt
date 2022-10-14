package com.plcoding.tabata.feature_drill.presentation.timer

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun TimerScreen(
    navController: NavController,
    drillColor: Int,
    viewModel: TimerViewModel = hiltViewModel()
) {
    val serviceIntent = Intent(TimerSettings.appcontext, TimerService::class.java)

    val drillBackgroundAnimatable = remember {
        Animatable(
            Color(if (drillColor != -1) drillColor else viewModel.drillColor.value)
        )
    }

    var currentTime by remember {
        mutableStateOf(TimerSettings.currentTime)
    }

    var value by remember {
        mutableStateOf(viewModel.initialValue)
    }

    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = TimerSettings.currentTime.value, key2 = isTimerRunning) {
        if (isTimerRunning) {
            viewModel._allTime.value -=
                if (TimerSettings.initialTime.toInt() - TimerSettings.currentTime.value.toInt() == 0) 0 else 1
            viewModel.toMinutes()
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(drillBackgroundAnimatable.value),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = viewModel.totalTime.value,
            style = MaterialTheme.typography.h4
        )
        Text(text = TimerSettings.currentTime.value.toString())
        IconButton(onClick = {
            serviceIntent.putExtra(TimerService.TIME_EXTRA, TimerSettings.initialTime)
            TimerSettings.appcontext.startService(serviceIntent)
            isTimerRunning = true

        }) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "play")
        }
    }
}


