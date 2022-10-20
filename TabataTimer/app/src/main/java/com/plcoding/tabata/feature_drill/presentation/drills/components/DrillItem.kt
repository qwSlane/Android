package com.plcoding.tabata.feature_drill.domain.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.plcoding.tabata.feature_drill.domain.model.Workout
import com.plcoding.tabata.R

@Composable
fun DrillItem(
    drill: Workout,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit,
    onLaunchClick: () -> Unit
) {

    val total: String = stringResource(id = R.string.total_time)
    val actions: String = stringResource(id = R.string.actions_count)

    val cycles: String = stringResource(id = R.string.cycles)
    val calcTime: String = calculateTime(drill)

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(drill.color))
                .padding(16.dp)
                .padding(end = 32.dp)
        ) {

            Text(
                text = drill.title,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )


            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = cycles + ": " + drill.sets.toString(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = actions + ": " + (drill.actions.count() * drill.sets).toString(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$total: $calcTime",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete note"
            )
        }

        IconButton(
            onClick = onLaunchClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = 100.dp)

        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

fun calculateTime(drill: Workout): String {
    var time: Int = 0
    for (item in drill.actions) {
        time += item.second
    }
    time *= drill.sets
    var sec: String = ""
    if (time % 60 < 10) {
        sec = "0" + (time % 60).toString()
    } else {
        sec = (time % 60).toString()
    }
    return (time / 60).toString() + ":" + sec
}

