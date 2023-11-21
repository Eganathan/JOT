package net.eknath.jot.ui.screens.states

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import net.eknath.jot.domain.model.Note
import net.eknath.jot.ui.screens.NoteViewModel

class EditorState(val viewModel: NoteViewModel) {

    val titleTextFieldState = mutableStateOf(TextFieldValue("", TextRange.Zero))
    val entryTextFieldState = mutableStateOf(TextFieldValue("", TextRange.Zero))
    val searchTextField = mutableStateOf(TextFieldValue("", TextRange.Zero))

    fun createJot() {
        if (titleTextFieldState.value.text.isNotBlank() || entryTextFieldState.value.text.isNotBlank()) {
            viewModel.addNote(
                note = Note(
                    title = titleTextFieldState.value.text,
                    content = entryTextFieldState.value.text
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
                        content = entryTextFieldState.value.text
                    )
                )
            } else {
                viewModel.deleteNoteById(it)
            }
        }
    }

    fun bulkDelete(noteIds: List<Long>) {
        viewModel.deleteNotes(notes = noteIds)
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