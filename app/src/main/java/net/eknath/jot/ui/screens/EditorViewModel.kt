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


    fun createNote(useCase: EditorUseCase.Create): Status {
        val newNote = JotNote(
            id = System.currentTimeMillis(),
            title = useCase.title,
            note = useCase.note,
            color = useCase.color
        )
        _jotterList.value = state.value.plus(newNote)

        return if (state.value.contains(newNote)) Status.Success("") else Status.Failed("")
    }
}