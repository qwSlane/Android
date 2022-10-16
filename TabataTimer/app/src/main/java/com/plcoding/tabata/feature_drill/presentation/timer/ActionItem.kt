package com.plcoding.tabata.feature_drill.presentation.timer

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils

@Composable
fun ActionItem(
    index: Int,
    time: Int,
    type: Int,
    color: Color
) {
    Spacer(modifier = Modifier.height(20.dp))
    Box(modifier = Modifier.fillMaxSize()){
        Text(
            text = "$index." + stringResource(id = type) +": "+ "$time",
            modifier = Modifier.fillMaxSize(),
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
    Divider(
        color = Color(
            ColorUtils.blendARGB(
                color.toArgb(),
                MaterialTheme.colors.primary.toArgb(), .05f
            )
        ),
        thickness = 2.dp
    )

}
