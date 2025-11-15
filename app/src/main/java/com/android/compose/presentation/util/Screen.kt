package com.android.compose.presentation.util

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object NotesScreen

    @Serializable
    data class AddEditNoteScreen(val noteId: Int?)
}
