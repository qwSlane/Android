package com.plcoding.tabata.feature_drill.presentation.Preferences

data class Preferences(
    var theme: Int,
    var language: Language,
    var fontSize: Int
)

enum class Language {
    ENGLISH, RUSSIAN
}
