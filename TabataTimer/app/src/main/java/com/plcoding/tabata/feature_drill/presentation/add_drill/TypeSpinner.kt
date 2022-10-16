package com.plcoding.tabata.feature_drill.presentation.add_drill

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
        stringResource(id = R.string.preparation),
        stringResource(id = R.string.work),
        stringResource(id = R.string.rest),
    )

//    val map: Map<String, String> = mapOf(
//        "Preparation" to stringResource(id = R.string.preparation),
//        "Подготовка" to stringResource(id = R.string.preparation),
//        "Work" to stringResource(id = R.string.work),
//        "Работа" to stringResource(id = R.string.work),
//        "Работа" to stringResource(id = R.string.work),
//        "Rest" to stringResource(id = R.string.rest),
//        "Отдых" to stringResource(id = R.string.rest),
//        )

    val text =
        remember { mutableStateOf(viewmodel.items[index].first.value) } // initial value
    var isOpen = remember { mutableStateOf(false) } // initial value
    val openCloseOfDropDownList: (Boolean) -> Unit = {
        isOpen.value = it
    }
    val userSelectedString: (String) -> Unit = {
        when(it){
            "Preparation"->  text.value = R.string.preparation
            "Work"->  text.value = R.string.work
            "Rest"->  text.value = R.string.rest
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

            DropDownList(
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