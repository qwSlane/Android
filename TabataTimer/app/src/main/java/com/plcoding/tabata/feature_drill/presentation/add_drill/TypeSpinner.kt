package com.plcoding.tabata.feature_drill.presentation.add_drill

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.plcoding.tabata.R
import com.plcoding.tabata.feature_drill.presentation.Preferences.PreferencesSerializer
import com.plcoding.tabata.feature_drill.presentation.Preferences.presentation.PreferencesViewModel
import com.plcoding.tabata.feature_drill.presentation.drills.components.DropDownList

@Composable
fun TypeSpinner(
    viewmodel: AddEditDrillViewModel,
    index: Int
) {
    val sizes = listOf(
        Pair(stringResource(id = R.string.preparation), 0),
        Pair(stringResource(id = R.string.work), 1),
        Pair(stringResource(id = R.string.rest), 2)
    )

    val text =
        remember { mutableStateOf(viewmodel.items[index].first.value) } // initial value
    var isOpen = remember { mutableStateOf(false) } // initial value
    val openCloseOfDropDownList: (Boolean) -> Unit = {
        isOpen.value = it
    }
    val userSelectedString: (Int) -> Unit = {
        when (it) {
            0 -> text.value = R.string.preparation
            1 -> text.value = R.string.work
            2 -> text.value = R.string.rest
        }


        viewmodel.items[index].first.value = text.value
    }
    Box(
        contentAlignment = Alignment.Center,

        ) {
        Column {
            Text(
                text = "$index. " + stringResource(id = text.value),
                style = MaterialTheme.typography.h5,
                modifier = Modifier.clickable {
                    isOpen.value = !isOpen.value
                }
            )

            DropDownTypeList(
                requestToOpen = isOpen.value,
                list = sizes,
                openCloseOfDropDownList,
                userSelectedString,
                modifier = Modifier
                    .background(color = Color(viewmodel.drillColor.value))
            )
        }

    }
}


@Composable
fun DropDownTypeList(
    requestToOpen: Boolean = false,
    list: List<Pair<String, Int>>,
    request: (Boolean) -> Unit,
    selectedString: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    DropdownMenu(
        expanded = requestToOpen,
        onDismissRequest = { request(false) },
        modifier = modifier
    ) {
        list.forEach {
            DropdownMenuItem(
                onClick = {
                    request(false)
                    selectedString(it.second)
                }
            ) {
                Text(it.first, modifier = Modifier.wrapContentWidth())
            }
        }
    }
}