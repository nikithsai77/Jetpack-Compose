package com.android.compose.domain.use_case

data class NoteUseCase(
    val addNote: AddNote,
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val getNote: GetNote
)
