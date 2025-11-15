package com.android.compose.presentation.add_edit_note.components

sealed interface UiEvent {
    object SaveNote : UiEvent
    data class DisplaySnackBar(val message: String) : UiEvent
}
