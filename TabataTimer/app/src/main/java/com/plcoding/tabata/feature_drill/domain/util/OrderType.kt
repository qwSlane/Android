package com.plcoding.tabata.feature_drill.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}