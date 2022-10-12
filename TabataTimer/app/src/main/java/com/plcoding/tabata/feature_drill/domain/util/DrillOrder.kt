package com.plcoding.tabata.feature_drill.domain.util

sealed class DrillOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): DrillOrder(orderType)
    class Date(orderType: OrderType): DrillOrder(orderType)
    class Color(orderType: OrderType): DrillOrder(orderType)

    fun copy(orderType: OrderType): DrillOrder{
        return when(this){
            is Title -> Title(orderType)
            is Date -> Date(orderType)
            is Color -> Color(orderType)
        }
    }
}