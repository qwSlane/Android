package com.plcoding.tabata.feature_drill.presentation.add_drill

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.tabata.R
import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.feature_drill.presentation.drills.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
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
                hint = stringResource(id = R.string.hint),
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
            Divider(
                color = Color(
                    ColorUtils.blendARGB(
                        drillBackgroundAnimatable.value.toArgb(),
                        MaterialTheme.colors.primary.toArgb(), .1f
                    )
                ), modifier = Modifier
                    .fillMaxWidth()
                    .weight(.05f)
            )
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colors.background)
                    .padding(top = 10.dp)
                    .weight(2f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.cycles), style = TextStyle(
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary
                        ).plus(MaterialTheme.typography.h4)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(50.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = {
                                            viewModel.onEvent(AddEditDrillEvent.CycleDec(20))
                                        },
                                        onTap = {
                                            viewModel.onEvent(AddEditDrillEvent.CycleDec(1))
                                        }
                                    )
                                }
                                .background(
                                    color = drillBackgroundAnimatable.value,
                                    shape = CircleShape
                                )
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
                            color = MaterialTheme.colors.primary
                        )

                        Icon(
                            modifier = Modifier
                                .size(50.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = {
                                            viewModel.onEvent(AddEditDrillEvent.CycleInc(20))
                                        },
                                        onTap = {
                                            viewModel.onEvent(AddEditDrillEvent.CycleInc(1))
                                        }
                                    )
                                }
                                .background(
                                    color = drillBackgroundAnimatable.value,
                                    shape = CircleShape
                                )
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
                }
            }
            Divider(
                color = Color(
                    ColorUtils.blendARGB(
                        drillBackgroundAnimatable.value.toArgb(),
                        MaterialTheme.colors.primary.toArgb(), .2f
                    )
                ), modifier = Modifier
                    .fillMaxWidth()
                    .weight(.05f)
            )
            Box(
                modifier = Modifier
                    .weight(6.3f)
                    .background(color = MaterialTheme.colors.background)
                    .align(Alignment.CenterHorizontally)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp)
                        .background(color = MaterialTheme.colors.background)
                ) {
                    itemsIndexed(viewModel.items) { index, item ->
                        DrillListItem(
                            changedValue = item.second,
                            index = index,
                            viewModel = viewModel,
                            color = drillBackgroundAnimatable.value,
                        )

                    }
                }
            }

            Divider(
                color = Color(
                    ColorUtils.blendARGB(
                        drillBackgroundAnimatable.value.toArgb(),
                        MaterialTheme.colors.primary.toArgb(), .1f
                    )
                ), modifier = Modifier
                    .fillMaxWidth()
                    .weight(.05f)
            )
            Box(modifier = Modifier.weight(.15f)) {}
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .align(Alignment.CenterHorizontally)
            ) {
                ExtendedFloatingActionButton(
                    text = {
                        Text(
                            text = stringResource(id = R.string.add_action),
                            style = MaterialTheme.typography.h5
                        )
                    },
                    onClick = { viewModel.onEvent(AddEditDrillEvent.AddDrill) },
                    modifier = Modifier
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    backgroundColor = Color(
                        ColorUtils.blendARGB(
                            drillBackgroundAnimatable.value.toArgb(),
                            MaterialTheme.colors.primary.toArgb(), .1f
                        )
                    )
                )
            }
            Box(modifier = Modifier.weight(.15f)) {

            }
        }
    }
}