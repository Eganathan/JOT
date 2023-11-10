package net.eknath.jot.ui.screens

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.eknath.jot.domain.model.Note
import net.eknath.jot.domain.usecase.AddNoteUseCase
import net.eknath.jot.domain.usecase.DeleteNoteUseCase
import net.eknath.jot.domain.usecase.GetAllNotesUseCase
import net.eknath.jot.domain.usecase.GetNoteUseCase
import net.eknath.jot.domain.usecase.UpdateNoteUseCase


class NoteViewModel(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    val selectedNoteId: MutableState<Long?> = mutableStateOf(null)
    val selectedNote: MutableState<Note?> = mutableStateOf(null)

    val notes = getAllNotesUseCase().asLiveData()

    fun addNote(note: Note) = viewModelScope.launch {
        selectedNoteId.value = addNoteUseCase(note)
        Log.e("VM", "SelectedNoteID: ${selectedNoteId.value}")
    }

    fun getNoteById(id: Long) = viewModelScope.launch {
        val note = getNoteUseCase(id)
        if (note != null) {
            selectedNoteId.value = note.id
            selectedNote.value = note
            Log.e("VM", "GOT: ${selectedNote}")
        }
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        updateNoteUseCase(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        deleteNoteUseCase(note)
    }

    fun resetSelection() {
        selectedNoteId.value = null
    }
}


//
//import androidx.compose.runtime.State
//import androidx.compose.runtime.mutableStateOf
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//class EditorViewModel() : ViewModel() {
//    private val _jotterList = MutableStateFlow<List<JotNote>>(listOf())
//    val state: StateFlow<List<JotNote>>
//        get() = _jotterList
//
//    private val _selectedJot = MutableStateFlow<JotNote?>(null)
//    val selectedJot: StateFlow<JotNote?>
//        get() = _selectedJot
//
//
//    fun getNote(id: Long): Status {
//        val note = state.value.filter { it.id == id }
//        _selectedJot.value = note.firstOrNull()
//
//        return if (_selectedJot.value?.id == id) Status.Success("") else Status.Failed("")
//    }
//
//    fun createNote(useCase: EditorUseCase.Create): Status {
//        val newNote = JotNote(
//            id = System.currentTimeMillis(),
//            title = useCase.title,
//            note = useCase.note,
//            color = useCase.color
//        )
//
//        if (newNote.note.isNotBlank()) {
//            _jotterList.value = state.value.plus(newNote)
//        }
//        _selectedJot.value = newNote
//        return if (state.value.contains(newNote)) Status.Success("") else Status.Failed("")
//    }
//
//    fun updateNote(useCase: EditorUseCase.Update): Status {
//        val newNote = JotNote(
//            id = useCase.id,
//            title = useCase.title,
//            note = useCase.note,
//            color = useCase.color
//        )
//
//        getNote(id = useCase.id)
//        val currentJot = selectedJot.value
//        if (currentJot != null && currentJot.id == newNote.id) {
//            //delete jot
//            _jotterList.value = _jotterList.value.minus(currentJot)
//
//            //to not have empty jot
//            if (newNote.title.isNotBlank() || newNote.note.isNotBlank()) {
//                _jotterList.value = _jotterList.value.plus(newNote).asReversed()
//            }
//        }
//        return if (state.value.contains(newNote)) Status.Success("") else Status.Failed("")
//    }
//
//
//}
