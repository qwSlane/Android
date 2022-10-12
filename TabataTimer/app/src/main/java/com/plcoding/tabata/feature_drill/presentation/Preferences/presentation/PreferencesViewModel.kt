package com.plcoding.tabata.feature_drill.presentation.Preferences.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.plcoding.tabata.feature_drill.presentation.Preferences.Language
import com.plcoding.tabata.feature_drill.presentation.Preferences.PreferencesSerializer


class PreferencesViewModel(
    private val darkMode: MutableState<Int>,
    private val fontSize: MutableState<Int>
) : ViewModel(
) {

    var state = MutableLiveData<Boolean>(null)
    fun switchTheme() {
        PreferencesSerializer.savePreferences(
            theme = if (PreferencesSerializer.preferences.theme == 1) 0 else 1
        )
        PreferencesSerializer.readPreferences()
        darkMode.value = PreferencesSerializer.preferences.theme
    }

    fun swtichFontSize(size: Int) {
        PreferencesSerializer.savePreferences(
            fontSize = size
        )
        PreferencesSerializer.readPreferences()
        fontSize.value = PreferencesSerializer.preferences.fontSize
    }
}

fun setLanguage(lang: Language) {
    val locale = java.util.Locale(
        when (lang) {
            Language.ENGLISH -> "en"
            Language.RUSSIAN -> "ru"
            else -> "en"
        }
    )
    val configuration = PreferencesSerializer.configuration
    configuration!!.setLocale(locale)
    val resources = PreferencesSerializer.context!!.resources
    resources.updateConfiguration(configuration, resources.displayMetrics)
    PreferencesSerializer.savePreferences(language = lang)
}