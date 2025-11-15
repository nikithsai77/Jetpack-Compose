package com.android.compose.domain

import com.android.compose.domain.model.InvalidNoteException
import com.android.compose.domain.model.Note
import com.android.compose.domain.repository.NoteRepository
import com.android.compose.domain.use_case.AddNote
import com.android.compose.feature_note.FakeNoteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class AddNoteTest {
    private lateinit var addNote: AddNote
    private lateinit var noteRepository: NoteRepository

    @Before
    fun setUp() {
        noteRepository = FakeNoteRepository()
        addNote = AddNote(noteRepository)
    }

    @Test
    fun `The Title of the Note can't be Empty if yes then throws exception`() {
        val note = Note(title = "", description = "content", timestamp = 100L, color = 101, id = 1)
        assertThrows(InvalidNoteException::class.java) {
            runTest {
                addNote(note)
            }
        }
    }

    @Test
    fun `The Description of the Note can't be Empty if yes then throws exception`() {
        val note = Note(title = "ABC", description = "", timestamp = 100L, color = 101, id = 1)
        assertThrows(InvalidNoteException::class.java) {
            runTest {
                addNote(note)
            }
        }
    }

    @Test
    fun `The Note must be inserted if title and description is non empty`() = runTest {
        val note = Note(title = "ABC", description = "Description", timestamp = 100L, color = 101, id = 1)
        try {
            addNote(note)
        } catch (e: Exception) {
            fail("Expected no exception, but got ${e::class.simpleName}: ${e.message}")
        }
    }

}
