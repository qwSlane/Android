package com.plcoding.tabata.feature_drill.presentation.add_drill

import android.widget.TextView
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.presentation.drills.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditDrillScreen(
    navController: NavController,
    drillColor: Int,
    viewModel: AddEditDrillViewModel = hiltViewModel()
) {
    val titleState = viewModel.title.value
    val scaffoldState = rememberScaffoldState()

    val drillBackgroundAnimatable = remember {
        Animatable(
            Color(if (drillColor != -1) drillColor else viewModel.drillColor.value)
        )
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditDrillViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditDrillViewModel.UiEvent.SaveDrill -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditDrillEvent.SaveDrill)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save drill")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(drillBackgroundAnimatable.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Workout.colors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.drillColor.value == colorInt) {
                                    Color.Black
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    drillBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                viewModel.onEvent(AddEditDrillEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }

            TransparentHintTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
                    .weight(1f),
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditDrillEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditDrillEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Preparation", style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ).plus(MaterialTheme.typography.h4)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    viewModel.onEvent(AddEditDrillEvent.Dec("Preparation", 20))
                                },
                                onTap = {
                                    viewModel.onEvent(AddEditDrillEvent.Dec("Preparation", 1))
                                }
                            )
                        }
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Dec",
                    tint = Color.Black
                )

                Text(
                    text = viewModel.drillPrepare.value.toString(),
                    style = MaterialTheme.typography.h4,
                    color = Color.Black
                )

                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    viewModel.onEvent(AddEditDrillEvent.Inc("Preparation", 20))
                                },
                                onTap = {
                                    viewModel.onEvent(AddEditDrillEvent.Inc("Preparation", 1))
                                }
                            )
                        }
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Inc",
                    tint = Color.Black
                )

            }

            Text(
                text = "Work", style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ).plus(MaterialTheme.typography.h4)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    viewModel.onEvent(AddEditDrillEvent.Dec("Work", 20))
                                },
                                onTap = {
                                    viewModel.onEvent(AddEditDrillEvent.Dec("Work", 1))
                                }
                            )
                        }
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Dec",
                    tint = Color.Black
                )

                Text(
                    text = viewModel.drillWork.value.toString(),
                    style = MaterialTheme.typography.h4,
                    color = Color.Black
                )

                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    viewModel.onEvent(AddEditDrillEvent.Inc("Work", 20))
                                },
                                onTap = {
                                    viewModel.onEvent(AddEditDrillEvent.Inc("Work", 1))
                                }
                            )
                        }
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Inc",
                    tint = Color.Black
                )

            }

            Text(
                text = "Rest", style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ).plus(MaterialTheme.typography.h4)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    viewModel.onEvent(AddEditDrillEvent.Dec("Rest", 20))
                                },
                                onTap = {
                                    viewModel.onEvent(AddEditDrillEvent.Dec("Rest", 1))
                                }
                            )
                        }
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Dec",
                    tint = Color.Black
                )


                Text(
                    text = viewModel.drillRest.value.toString(),
                    style = MaterialTheme.typography.h4,
                    color = Color.Black
                )

                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    viewModel.onEvent(AddEditDrillEvent.Inc("Rest", 20))
                                },
                                onTap = {
                                    viewModel.onEvent(AddEditDrillEvent.Inc("Rest", 1))
                                }
                            )
                        }
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Inc",
                    tint = Color.Black
                )

            }

            Text(
                text = "RestCount", style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ).plus(MaterialTheme.typography.h4)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    viewModel.onEvent(AddEditDrillEvent.Dec("RestCount", 20))
                                },
                                onTap = {
                                    viewModel.onEvent(AddEditDrillEvent.Dec("RestCount", 1))
                                }
                            )
                        }
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Dec",
                    tint = Color.Black
                )


                Text(
                    text = viewModel.drillRestPeriods.value.toString(),
                    style = MaterialTheme.typography.h4,
                    color = Color.Black
                )

                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    viewModel.onEvent(AddEditDrillEvent.Inc("RestCount", 20))
                                },
                                onTap = {
                                    viewModel.onEvent(AddEditDrillEvent.Inc("RestCount", 1))
                                }
                            )
                        }
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Inc",
                    tint = Color.Black
                )

            }



            Text(
                text = "Cycles", style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ).plus(MaterialTheme.typography.h4)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    viewModel.onEvent(AddEditDrillEvent.Dec("Sets", 20))
                                },
                                onTap = {
                                    viewModel.onEvent(AddEditDrillEvent.Dec("Sets", 1))
                                }
                            )
                        }
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Inc",
                    tint = Color.Black
                )

                Text(
                    text = viewModel.drillSets.value.toString(),
                    style = MaterialTheme.typography.h4,
                    color = Color.Black
                )

                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    viewModel.onEvent(AddEditDrillEvent.Inc("Sets", 20))
                                },
                                onTap = {
                                    viewModel.onEvent(AddEditDrillEvent.Inc("Sets", 1))
                                }
                            )
                        }
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Inc",
                    tint = Color.Black
                )

            }

            Box(
                modifier = Modifier
                    .weight(1f)
            )


        }
    }
}