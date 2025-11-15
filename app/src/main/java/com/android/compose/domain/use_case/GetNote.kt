package com.android.compose.domain.use_case

import com.android.compose.domain.model.Note
import com.android.compose.domain.repository.NoteRepository

data class GetNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id = id)
    }

}
