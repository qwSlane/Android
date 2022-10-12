package com.plcoding.tabata.feature_drill.presentation.drills.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.plcoding.tabata.feature_drill.presentation.Preferences.PreferencesSerializer
import com.plcoding.tabata.feature_drill.presentation.Preferences.presentation.PreferencesViewModel

@Composable
fun DropDownList(
    requestToOpen: Boolean = false,
    list: List<String>,
    request: (Boolean) -> Unit,
    selectedString: (String) -> Unit
) {

    DropdownMenu(
        expanded = requestToOpen,
        onDismissRequest = { request(false) },

        ) {
        list.forEach {
            DropdownMenuItem(
                onClick = {
                    request(false)
                    selectedString(it)
                }
            ) {
                Text(it, modifier = Modifier.wrapContentWidth())
            }
        }
    }
}

@Composable
fun LanguageList(viewmodel: PreferencesViewModel) {
    val sizes = listOf(
        "Small",
        "Medium",
        "Large",
    )
    val text =
        remember { mutableStateOf(sizes[PreferencesSerializer.preferences.fontSize]) } // initial value
    var isOpen = remember { mutableStateOf(false) } // initial value
    val openCloseOfDropDownList: (Boolean) -> Unit = {
        isOpen.value = it
    }
    val userSelectedString: (String) -> Unit = {
        text.value = it
        val index = when (it) {
            "Small" -> 0
            "Medium" -> 1
            else -> 2
        }
        viewmodel.swtichFontSize(index)
    }
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Column {
            Text(
                text = text.value,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.clickable {
                    isOpen.value = !isOpen.value
                }
            )

            DropDownList(
                requestToOpen = isOpen.value,
                list = sizes,
                openCloseOfDropDownList,
                userSelectedString
            )
        }

    }
}