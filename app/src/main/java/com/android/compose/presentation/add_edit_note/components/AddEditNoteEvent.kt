package com.android.compose.presentation.add_edit_note.components

import androidx.compose.ui.focus.FocusState

sealed interface AddEditNoteEvent {
    object SaveNote : AddEditNoteEvent
    data class EnteredTitle(val text: String) : AddEditNoteEvent
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditNoteEvent

    data class EnterDescription(val text: String) : AddEditNoteEvent
    data class ChangeDescriptionFocus(val focusState: FocusState) : AddEditNoteEvent

    data class ChangeColor(val color: Int) : AddEditNoteEvent
}
