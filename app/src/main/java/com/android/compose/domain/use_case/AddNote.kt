package com.android.compose.domain.use_case

import com.android.compose.domain.model.InvalidNoteException
import com.android.compose.domain.model.Note
import com.android.compose.domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNote(
    private val repository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException(message = "The title of the note can't be empty.")
        } else if (note.description.isBlank()) {
            throw InvalidNoteException(message = "The content of the note can't be empty.")
        }
        repository.insertNote(note = note)
    }

}
