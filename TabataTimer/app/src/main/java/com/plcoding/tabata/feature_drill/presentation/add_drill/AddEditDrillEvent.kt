package com.plcoding.tabata.feature_drill.presentation.add_drill

import androidx.compose.ui.focus.FocusState

sealed class AddEditDrillEvent{

    data class EnteredTitle(val value: String): AddEditDrillEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditDrillEvent()

    data class ChangeColor(val color: Int): AddEditDrillEvent()

    object SaveDrill: AddEditDrillEvent()

    data class Inc(val type: String, val value: Int): AddEditDrillEvent()
    data class Dec(val type: String, val value: Int): AddEditDrillEvent()





}
