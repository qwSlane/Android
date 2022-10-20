package com.plcoding.tabata.feature_drill.presentation.timer

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.tabata.R
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun TimerScreen(
    navController: NavController,
    drillColor: Int,
    viewModel: TimerViewModel = hiltViewModel()
) {

    val drillBackgroundAnimatable = remember {
        Animatable(
            Color(if (drillColor != -1) drillColor else viewModel.drillColor.value)
        )
    }

    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    var isOpenDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = TimerSettings.currentTime.value, key2 = isTimerRunning) {
        if (isTimerRunning) {
            viewModel._allTime.value = viewModel.allTime - TimerSettings.elapsedTime.value
            viewModel.toMinutes()
            if(viewModel._allTime.value == 0){
                isOpenDialog = true
            }
        }
    }

    LaunchedEffect(
        key1 = TimerSettings.currentIndex.value,
        key2 = TimerSettings.currentTime.value
    ) {
        if (viewModel.isDrillInitialized() && TimerSettings.currentIndex.value < viewModel.actionsCount) {
                viewModel.title.value =
                    viewModel.currentDrill.actions[TimerSettings.currentIndex.value].first
        }
    }

    if (isOpenDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.exit()
                navController.navigateUp()
            },
            text = { Text("Finish") },
            buttons = {
                Button(
                    onClick = {
                        viewModel.exit()
                        navController.navigateUp()
                    }
                ) {
                    Text("OK", style = MaterialTheme.typography.h6)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(drillBackgroundAnimatable.value)
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    viewModel.exit()
                    navController.navigateUp()
                })
            {
                Icon(
                    modifier = Modifier
                        .size(50.dp),
                    imageVector = Icons.Default.ArrowBackIos,
                    contentDescription = "back"
                )
            }

            Text(
                text = viewModel.totalTime.value,
                style = MaterialTheme.typography.h4,
            )

            IconButton(
                onClick = {
                    if (!isTimerRunning) {
                        viewModel.start()
                    } else {
                        viewModel.stop()
                    }
                    isTimerRunning = !isTimerRunning
                }) {
                Icon(
                    modifier = Modifier
                        .size(50.dp),
                    imageVector = if (isTimerRunning) {
                        Icons.Default.Pause
                    } else {
                        Icons.Default.PlayArrow
                    }, contentDescription = "play"
                )
            }

        }

        Divider(
            color = Color(
                ColorUtils.blendARGB(
                    drillBackgroundAnimatable.value.toArgb(),
                    MaterialTheme.colors.primary.toArgb(), .1f
                )
            ),
            thickness = 3.dp
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = TimerSettings.currentIndex.value.toString() + "." + stringResource(id = viewModel.title.value),
            style = MaterialTheme.typography.h3,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.weight(1.5f).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = {
                viewModel.skipBack()
                isTimerRunning = true
            }) {
                Icon(
                    modifier = Modifier
                        .size(50.dp),
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "skip"
                )

            }
            Text(
                text = TimerSettings.currentTime.value.toString(),
                style = MaterialTheme.typography.h2,

                )
            IconButton(onClick = {
                viewModel.skipNext()
                isTimerRunning = true
            }) {
                Icon(
                    modifier = Modifier
                        .size(50.dp),
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "skip"
                )

            }
        }


        Box(modifier = Modifier.weight(5f)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.isDrillInitialized()) {
                    itemsIndexed(viewModel.currentDrill.actions) { index, item ->
                        ActionItem(
                            index = index,
                            time = item.second,
                            type = item.first,
                            color = drillBackgroundAnimatable.value
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            Divider(
                color = Color(
                    ColorUtils.blendARGB(
                        drillBackgroundAnimatable.value.toArgb(),
                        MaterialTheme.colors.primary.toArgb(), .1f
                    )
                ),
                thickness = 3.dp
            )
            Text(
                text = TimerSettings.currentIndex.value.toString() + "/" + viewModel.actionsCount,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }


}


