package com.android.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable

@Composable
fun CustomFlowRow(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        modifier = modifier,
        measurePolicy = { measurables, constraints ->

            val placeable = measurables.map {
                it.measure(constraints)
            }

            val groupPlaceable = mutableListOf<List<Placeable>>()
            var currentGroup = mutableListOf<Placeable>()
            var currentGroupWidth = 0

            placeable.forEach { placeable ->
                if (currentGroupWidth + placeable.width <= constraints.maxWidth) {
                    currentGroup.add(placeable)
                    currentGroupWidth += placeable.width
                } else {
                    groupPlaceable.add(currentGroup)
                    currentGroup = mutableListOf(placeable)
                    currentGroupWidth = placeable.width
                }
            }

            if (currentGroup.isNotEmpty()) {
                groupPlaceable.add(currentGroup)
            }

            layout(width = constraints.maxWidth, height = constraints.maxHeight) {
                var yPosition = 0

                groupPlaceable.forEach { row ->
                    var xPosition = 0
                    row.forEach { placeable ->
                        placeable.place(x = xPosition, y = yPosition)
                        xPosition += placeable.width
                    }
                    yPosition += row.maxOfOrNull { it.height } ?: 0
                }

            }
        },
        content = content
    )
}
