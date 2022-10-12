package com.plcoding.tabata.feature_drill.presentation.drills.components

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.room.FtsOptions
import com.plcoding.tabata.MainActivity
import com.plcoding.tabata.R
import com.plcoding.tabata.feature_drill.presentation.drills.DrillsEvent
import com.plcoding.tabata.feature_drill.presentation.drills.DrillsViewModel
import com.plcoding.tabata.feature_drill.domain.util.DrillItem
import com.plcoding.tabata.feature_drill.presentation.util.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DrillsScreen(
    navController: NavController,
    viewModel: DrillsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditDrillScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add drill")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.drills),
                    style = MaterialTheme.typography.h4
                )
                Row {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.PreferencesScreen.route)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "settings"
                        )

                    }
                    IconButton(
                        onClick = {
                            viewModel.onEvent(DrillsEvent.ToggleOrderSection)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = ""
                        )
                    }

                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    drillOrder = state.drillOrder,
                    onOrderChange = {
                        viewModel.onEvent(DrillsEvent.Order(it))
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.drills) { drill ->
                    DrillItem(
                        drill = drill,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditDrillScreen.route +
                                            "?drillId=${drill.id}&drillColor=${drill.color}"
                                )
                            },
                        onDeleteClick = {
                            viewModel.onEvent(DrillsEvent.DeleteDrill(drill))
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Drill deleted",
                                    actionLabel = "Undo"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(DrillsEvent.RestoreDrill)
                                }
                            }
                        },
                        onLaunchClick = {
                            navController.navigate(
                                Screen.TimerScreen.route +
                                        "?drillId=${drill.id}&drillColor=${drill.color}"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

    }
}