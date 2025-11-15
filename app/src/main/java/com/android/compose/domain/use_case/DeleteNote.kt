package com.android.compose.domain.use_case

import com.android.compose.domain.model.Note
import com.android.compose.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note = note)
    }

}
