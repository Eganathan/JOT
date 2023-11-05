package net.eknath.jot.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


data class JotNote(
    val id: Long = 0,
    val title: String,
    val note: String,
    val color: Long = 0
)

sealed class EditorUseCase(val title: String, val note: String, val color: Long) {
    class Create(val title: String, val note: String, val color: Long)
    class Update(val id: Long, val title: String, val note: String, val color: Long)
    class Delete(val id: Long)
}


sealed class Status {
    class Success(message: String) : Status()
    class Failed(message: String) : Status()
}

class EditorViewModel() : ViewModel() {
    private val _jotterList = MutableStateFlow<List<JotNote>>(listOf())
    val state: StateFlow<List<JotNote>>
        get() = _jotterList

    private val _selectedJot = MutableStateFlow<JotNote?>(null)
    val selectedJot: StateFlow<JotNote?>
        get() = _selectedJot


    fun getNote(id: Long): Status {
        val note = state.value.filter { it.id == id }
        _selectedJot.value = note.firstOrNull()

        return if (_selectedJot.value?.id == id) Status.Success("") else Status.Failed("")
    }

    fun createNote(useCase: EditorUseCase.Create): Status {
        val newNote = JotNote(
            id = System.currentTimeMillis(),
            title = useCase.title,
            note = useCase.note,
            color = useCase.color
        )

        if (newNote.note.isNotBlank()) {
            _jotterList.value = state.value.plus(newNote)
        }
        return if (state.value.contains(newNote)) Status.Success("") else Status.Failed("")
    }

    fun updateNote(useCase: EditorUseCase.Update): Status {
        val newNote = JotNote(
            id = useCase.id,
            title = useCase.title,
            note = useCase.note,
            color = useCase.color
        )

        getNote(id = useCase.id)
        val currentJot = selectedJot.value
        if (currentJot != null && currentJot.id == newNote.id) {
            //delete jot
            _jotterList.value = _jotterList.value.minus(currentJot)

            //to not have empty jot
            if (newNote.title.isNotBlank() || newNote.note.isNotBlank()) {
                _jotterList.value = _jotterList.value.plus(newNote).asReversed()
            }
        }
        return if (state.value.contains(newNote)) Status.Success("") else Status.Failed("")
    }

    fun resetSelection() {
        _selectedJot.value = null
    }


}