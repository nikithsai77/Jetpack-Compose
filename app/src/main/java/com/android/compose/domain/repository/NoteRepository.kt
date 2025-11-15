package com.android.compose.domain.repository

import kotlinx.coroutines.flow.Flow
import com.android.compose.domain.model.Note

interface NoteRepository {
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun getNoteById(id: Int): Note?
    fun getNotes(): Flow<List<Note>>
}
