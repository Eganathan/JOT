package dev.eknath.jot.ui.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import dev.eknath.jot.di.IoDispatcher
import dev.eknath.jot.domain.model.Note
import dev.eknath.jot.domain.repository.NoteRepository
import dev.eknath.jot.domain.usecase.AddNoteUseCase
import dev.eknath.jot.domain.usecase.BulkDeleteUseCase
import dev.eknath.jot.domain.usecase.DeleteNoteUseCase
import dev.eknath.jot.domain.usecase.GetAllNotesUseCase
import dev.eknath.jot.domain.usecase.GetNoteUseCase
import dev.eknath.jot.domain.usecase.SearchNotesUseCase
import dev.eknath.jot.domain.usecase.SwitchFavUseCase
import dev.eknath.jot.domain.usecase.UpdateNoteUseCase
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val bulkDeleteUseCase: BulkDeleteUseCase,
    private val searchUseCase: SearchNotesUseCase,
    private val switchFavUseCase: SwitchFavUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val selectedNoteId: MutableState<Long?> = mutableStateOf(null)
    val selectedNote: MutableState<Note?> = mutableStateOf(null)

    val notes = getAllNotesUseCase().asLiveData()
    private val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults = searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            searchUseCase(query)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )


    fun addNote(note: Note) = viewModelScope.launch {
        selectedNoteId.value = addNoteUseCase(note)
        getNoteById(selectedNoteId.value!!)
    }

    fun getNoteById(id: Long) = viewModelScope.launch {
        val note = getNoteUseCase(id)
        if (note != null) {
            selectedNoteId.value = note.id
            selectedNote.value = note
        }
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        updateNoteUseCase(note)
    }

    fun switchFav(id: Long, isFav: Boolean) = viewModelScope.launch {
        switchFavUseCase(id, isFav = isFav)
    }

    fun deleteNoteById(id: Long) = viewModelScope.launch {
        deleteNoteUseCase(id)
        selectedNoteId.value = null
    }

    fun deleteNotes(notes: List<Long>) = viewModelScope.launch {
        bulkDeleteUseCase(notes)
    }

    fun resetSelection() {
        selectedNoteId.value = null
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }
}
