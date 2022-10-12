package com.plcoding.tabata.feature_drill.domain.use_case


data class DrillUseCases(
    val getDrills: GetDrills,
    val deleteDrill: DeleteDrill,
    val addDrill: AddDrill,
    val getDrill: GetDrill
)
