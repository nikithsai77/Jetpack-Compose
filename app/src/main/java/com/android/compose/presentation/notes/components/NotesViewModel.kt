package com.android.compose.presentation.notes.components

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.compose.domain.model.Note
import com.android.compose.domain.use_case.NoteUseCase
import com.android.compose.domain.util.NoteOrder
import com.android.compose.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val noteUseCase: NoteUseCase) : ViewModel() {
    private val _state = mutableStateOf(value = NoteState())
    val state: State<NoteState> = _state
    private var deletedNote: Note? = null
    private var getNotesJob: Job? = null

    init {
        getNotes(noteOrder = NoteOrder.Date(orderType = OrderType.ASCENDING))
    }

    fun onEvent(notesEvent: NotesEvent) {
        when (notesEvent) {
            is NotesEvent.Delete -> {
                viewModelScope.launch {
                    noteUseCase.deleteNote(note = notesEvent.note)
                    deletedNote = notesEvent.note
                }
            }

            is NotesEvent.Order -> {
                if (_state.value.noteOrder::class == notesEvent.noteOrder::class &&
                    _state.value.noteOrder.orderType == notesEvent.noteOrder.orderType
                ) return
                else getNotes(noteOrder = notesEvent.noteOrder)
            }

            is NotesEvent.RestoreNote -> {
                deletedNote?.let {
                    viewModelScope.launch {
                        noteUseCase.addNote(note = it)
                        deletedNote = null
                    }
                }
            }

            is NotesEvent.ToggleOrderSection -> {
                _state.value = _state.value.copy(
                    isOrderSelectionVisible = !_state.value.isOrderSelectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCase.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = _state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }

}