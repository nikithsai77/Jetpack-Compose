package com.android.compose.presentation.notes.components

import com.android.compose.domain.model.Note
import com.android.compose.domain.util.NoteOrder
import com.android.compose.domain.util.OrderType

data class NoteState(
    val notes: List<Note> = emptyList(),
    val isOrderSelectionVisible: Boolean = false,
    val noteOrder: NoteOrder = NoteOrder.Date(orderType = OrderType.DESCENDING)
)
