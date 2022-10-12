package com.plcoding.tabata.feature_drill.presentation.drills.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.plcoding.tabata.feature_drill.domain.util.DrillOrder
import com.plcoding.tabata.feature_drill.domain.util.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    drillOrder: DrillOrder = DrillOrder.Date(OrderType.Descending),
    onOrderChange: (DrillOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            DefaultRadioButton(
                text = "Title",
                selected = drillOrder is DrillOrder.Title ,
                onSelected = { onOrderChange(DrillOrder.Title(drillOrder.orderType))}
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Color",
                selected = drillOrder is DrillOrder.Color ,
                onSelected = { onOrderChange(DrillOrder.Color(drillOrder.orderType))}
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            DefaultRadioButton(
                text = "Ascending",
                selected = drillOrder.orderType is OrderType.Ascending ,
                onSelected = {
                    onOrderChange(drillOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = drillOrder.orderType is OrderType.Descending ,
                onSelected = {
                    onOrderChange(drillOrder.copy(OrderType.Descending))
                }
            )
        }


    }
}