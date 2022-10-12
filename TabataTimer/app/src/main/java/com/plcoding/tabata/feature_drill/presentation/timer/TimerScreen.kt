package com.plcoding.tabata.feature_drill.presentation.timer

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.tabata.R
import com.plcoding.tabata.feature_drill.presentation.add_drill.AddEditDrillViewModel
import com.plcoding.tabata.feature_drill.presentation.util.Screen

@Composable
fun TimerScreen(
    navController: NavController,
    drillColor: Int,
    viewModel: TimerViewModel = hiltViewModel()
){
    val drillBackgroundAnimatable = remember {
        Animatable(
            Color(if (drillColor != -1) drillColor else viewModel.drillColor.value)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(drillBackgroundAnimatable.value),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = viewModel.allTime.value,
            style = MaterialTheme.typography.h4
        )


    }
}