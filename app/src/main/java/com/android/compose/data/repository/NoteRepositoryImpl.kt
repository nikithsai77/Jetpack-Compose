package com.android.compose.data.repository

import kotlinx.coroutines.flow.Flow
import com.android.compose.domain.model.Note
import com.android.compose.data.data_source.NoteDao
import com.android.compose.domain.repository.NoteRepository

class NoteRepositoryImpl(private val dao: NoteDao) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
       return dao.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id = id)
    }

    override suspend fun insertNote(note: Note) {
       dao.insertNote(note = note)
    }

    override suspend fun deleteNote(note: Note) {
       dao.deleteNote(note = note)
    }

}
