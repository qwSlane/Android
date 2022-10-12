package com.plcoding.tabata.feature_drill.presentation.Preferences.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.plcoding.tabata.R
import com.plcoding.tabata.feature_drill.presentation.Preferences.Language
import com.plcoding.tabata.feature_drill.presentation.Preferences.PreferencesSerializer
import com.plcoding.tabata.feature_drill.presentation.drills.components.DefaultRadioButton
import com.plcoding.tabata.feature_drill.presentation.drills.components.LanguageList

@Composable
fun PreferencesScreen(
    navController: NavController,
    viewModel: PreferencesViewModel
) {
    val mode = remember {
        mutableStateOf(PreferencesSerializer.preferences.theme)
    }

    var language: MutableState<Language> = remember {
        mutableStateOf(PreferencesSerializer.preferences.language)
    }

    val darkModeChecked by remember(mode.value) {
        val checked = when (mode.value) {
            0 -> false
            else -> true
        }
        mutableStateOf(checked)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.settings),
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.mode), style = MaterialTheme.typography.h5)
                Switch(
                    modifier = Modifier
                        .fillMaxWidth(),
                    checked = darkModeChecked,
                    onCheckedChange = {
                        viewModel.switchTheme()
                        mode.value = PreferencesSerializer.preferences.theme
                    })
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.lang), style = MaterialTheme.typography.h5)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        DefaultRadioButton(
                            text = "ru",
                            selected = language.value == Language.RUSSIAN,
                            onSelected = {
                                setLanguage(lang = Language.RUSSIAN)
                                language.value = Language.RUSSIAN
                            })
                        Spacer(modifier = Modifier.width(10.dp))
                        DefaultRadioButton(
                            text = "en",
                            selected = language.value == Language.ENGLISH,
                            onSelected = {
                                setLanguage(lang = Language.ENGLISH)
                                language.value = Language.ENGLISH
                            })
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(id = R.string.font_size),
                    style = MaterialTheme.typography.h5
                )
                Spacer(modifier = Modifier.width(100.dp))
                LanguageList(viewModel)
            }
        }
    }
}



