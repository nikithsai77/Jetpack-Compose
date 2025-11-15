package com.android.compose.feature_note

import kotlinx.coroutines.flow.Flow
import com.android.compose.domain.model.Note
import com.android.compose.domain.repository.NoteRepository
import kotlinx.coroutines.flow.flow

class FakeNoteRepository : NoteRepository {
    private val mutableList = mutableListOf<Note>()

    override suspend fun insertNote(note: Note) {
        mutableList.add(note)
    }

    override suspend fun deleteNote(note: Note) {
        mutableList.remove(element = note)
    }

    override suspend fun getNoteById(id: Int): Note? {
        return mutableList.find { it.id == id }
    }

    override fun getNotes(): Flow<List<Note>> {
        return flow {
            emit(value = mutableList)
        }
    }

}
