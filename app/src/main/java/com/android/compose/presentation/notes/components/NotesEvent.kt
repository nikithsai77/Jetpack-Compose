package com.android.compose.presentation.notes.components

import com.android.compose.domain.model.Note
import com.android.compose.domain.util.NoteOrder

sealed interface NotesEvent {
    object RestoreNote : NotesEvent
    object ToggleOrderSection : NotesEvent
    data class Delete(val note: Note) : NotesEvent
    data class Order(val noteOrder: NoteOrder) : NotesEvent
}
