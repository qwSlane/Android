package com.plcoding.tabata.feature_drill.presentation.add_drill

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DrillListItem(
    changedValue: State<Int>,
    index: Int,
    viewModel: AddEditDrillViewModel,
    color: Color
) {
    Column(
        modifier =  Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        viewModel.onEvent(AddEditDrillEvent.Delete(index = index))
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TypeSpinner(viewmodel = viewModel, index = index)

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

                            },
                            onTap = {
                                viewModel.onEvent(AddEditDrillEvent.Dec(index))
                            }
                        )
                    }
                    .background(
                        color = color,
                        shape = CircleShape
                    )
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
                text = changedValue.value.toString(),
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.primary
            )

            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
//                            onLongPress(AddEditDrillEvent.Inc("Preparation", 20))
                            },
                            onTap = {
                                viewModel.onEvent(AddEditDrillEvent.Inc(index))
                            }
                        )
                    }
                    .background(
                        color = color,
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