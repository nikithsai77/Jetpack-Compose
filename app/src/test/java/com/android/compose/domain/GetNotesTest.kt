package com.android.compose.domain

import com.android.compose.domain.model.Note
import com.android.compose.domain.use_case.GetNotes
import com.android.compose.domain.util.NoteOrder
import com.android.compose.domain.util.OrderType
import com.android.compose.feature_note.FakeNoteRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetNotesTest {
    private lateinit var getNotes: GetNotes
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        getNotes = GetNotes(repository = fakeNoteRepository)

        val notesToInsert = mutableListOf<Note>()
        ('a'..'y').forEachIndexed { index, ch ->
            notesToInsert.add(
                Note(
                    title = ch.toString(),
                    description = ch.toString(),
                    timestamp = index.toLong(),
                    color = index
                )
            )
        }
        notesToInsert.shuffle()
        runBlocking {
            notesToInsert.forEach {
                fakeNoteRepository.insertNote(note = it)
            }
        }
    }

    @Test
    fun `Order notes by title ascending, correct order`() = runTest {
        val notes = getNotes(NoteOrder.Title(orderType = OrderType.ASCENDING)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].title).isLessThan(notes[i + 1].title)
        }
    }

    @Test
    fun `Order notes by title descending, correct order`() = runTest {
        val notes = getNotes(NoteOrder.Title(orderType = OrderType.DESCENDING)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].title).isGreaterThan(notes[i + 1].title)
        }
    }

    @Test
    fun `Order notes by Date ascending, correct order`() = runTest {
        val notes = getNotes(NoteOrder.Date(orderType = OrderType.ASCENDING)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isLessThan(notes[i + 1].timestamp)
        }
    }

    @Test
    fun `Order notes by Date descending, correct order`() = runTest {
        val notes = getNotes(NoteOrder.Title(orderType = OrderType.DESCENDING)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isGreaterThan(notes[i + 1].timestamp)
        }
    }

    @Test
    fun `Order notes by Color ascending, correct order`() = runTest {
        val notes = getNotes(NoteOrder.Color(orderType = OrderType.ASCENDING)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].color).isLessThan(notes[i + 1].color)
        }
    }

    @Test
    fun `Order notes by Color descending, correct order`() = runTest {
        val notes = getNotes(NoteOrder.Color(orderType = OrderType.DESCENDING)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].color).isGreaterThan(notes[i + 1].color)
        }
    }

}
