package dev.eknath.jot.ui.screens.states

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import dev.eknath.jot.domain.model.Note
import dev.eknath.jot.getDate
import dev.eknath.jot.ui.screens.NoteViewModel

class EditorState(val viewModel: NoteViewModel) {

    val titleTextFieldState = mutableStateOf(TextFieldValue("", TextRange.Zero))
    val entryTextFieldState = mutableStateOf(TextFieldValue("", TextRange.Zero))
    val searchTextField = mutableStateOf(TextFieldValue("", TextRange.Zero))

    val isFavorite = mutableStateOf(false)

    fun createJot() {
        if (titleTextFieldState.value.text.isNotBlank() || entryTextFieldState.value.text.isNotBlank()) {
            viewModel.addNote(
                note = Note(
                    title = titleTextFieldState.value.text,
                    content = entryTextFieldState.value.text,
                    createdDate = System.currentTimeMillis().getDate(),
                    lastModifiedDate = System.currentTimeMillis().getDate(),
                    isFavorite = isFavorite.value
                )
            )
        }
    }

    fun getJot(id: Long, onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        viewModel.getNoteById(id).invokeOnCompletion {
            val selectedNote = viewModel.selectedNote.value
            if (selectedNote != null) {
                titleTextFieldState.value = TextFieldValue(
                    text = selectedNote.title,
                    TextRange(
                        selectedNote.title.length,
                        selectedNote.title.length,
                    )
                )
                entryTextFieldState.value = TextFieldValue(
                    text = selectedNote.content,
                    TextRange(
                        selectedNote.content.length,
                        selectedNote.content.length,
                    )
                )
                isFavorite.value = selectedNote.isFavorite
                onSuccess.invoke()
            } else {
                onFailure.invoke()
            }
        }
    }

    fun updateJot() {
        viewModel.selectedNoteId.value?.let {
            if (titleTextFieldState.value.text.isNotBlank() || entryTextFieldState.value.text.isNotBlank()) {
                viewModel.updateNote(
                    Note(
                        id = it,
                        title = titleTextFieldState.value.text,
                        content = entryTextFieldState.value.text,
                        createdDate = viewModel.selectedNote.value?.createdDate ?: "0L",
                        lastModifiedDate = System.currentTimeMillis().getDate(),
                        isFavorite = isFavorite.value
                    )
                )
            } else {
                viewModel.deleteNoteById(it)
            }
        }
    }

    fun switchFavorite(isFav: Boolean, selectedNoteId: Long? = null, isDetailCall: Boolean = true) {
        (selectedNoteId ?: viewModel.selectedNoteId.value)?.let {
            if (isDetailCall && titleTextFieldState.value.text.isNotBlank() || entryTextFieldState.value.text.isNotBlank()) {
                isFavorite.value = isFav
                viewModel.switchFav(id = it, isFav = isFav)
            } else
                viewModel.switchFav(id = it, isFav = isFav)
            Log.e("Test", "HERE")
        }
    }

    fun bulkDelete(noteIds: List<Long>) {
        viewModel.deleteNotes(notes = noteIds)
    }

    fun deleteNote(noteId: Long) {
        viewModel.deleteNoteById(id = noteId)
    }

    fun resetTextFieldAndSelection() {
        titleTextFieldState.value = TextFieldValue()
        entryTextFieldState.value = TextFieldValue()
        viewModel.resetSelection()
    }

    fun setSearchQuery() {
        viewModel.setSearchQuery(searchTextField.value.text)
    }
}