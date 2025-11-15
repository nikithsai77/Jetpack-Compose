package com.android.compose.presentation.notes.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.android.compose.domain.util.NoteOrder
import com.android.compose.domain.util.OrderType

@Composable
fun OrderSelection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(orderType = OrderType.DESCENDING),
    onOrderChange: (NoteOrder) -> Unit
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text = "Title",
                selected = noteOrder is NoteOrder.Title,
                onClick = {
                    onOrderChange(NoteOrder.Title(orderType = noteOrder.orderType))
                }
            )

            Spacer(modifier = Modifier.width(width = 8.dp))

            DefaultRadioButton(
                text = "Date",
                selected = noteOrder is NoteOrder.Date,
                onClick = {
                    onOrderChange(NoteOrder.Date(orderType = noteOrder.orderType))
                }
            )

            Spacer(modifier = Modifier.width(width = 8.dp))

            DefaultRadioButton(
                text = "Color",
                selected = noteOrder is NoteOrder.Color,
                onClick = {
                    onOrderChange(NoteOrder.Color(orderType = noteOrder.orderType))
                }
            )
        }

        Spacer(modifier = Modifier.width(width = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = noteOrder.orderType is OrderType.ASCENDING,
                onClick = {
                    onOrderChange(noteOrder.copy(orderType = OrderType.ASCENDING))
                }
            )

            Spacer(modifier = Modifier.width(width = 8.dp))

            DefaultRadioButton(
                text = "Descending",
                selected = noteOrder.orderType is OrderType.DESCENDING,
                onClick = {
                    onOrderChange(noteOrder.copy(orderType = OrderType.DESCENDING))
                }
            )
        }
    }
}
