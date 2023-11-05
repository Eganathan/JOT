package net.eknath.jot.ui.screens.states

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import net.eknath.jot.ui.screens.EditorUseCase
import net.eknath.jot.ui.screens.EditorViewModel

class EditorState(private val viewModel: EditorViewModel) {

    val jotterList = viewModel.state
    val selectedJot = viewModel.selectedJot

    /**
     *
     * */

    val titleTextFieldState = mutableStateOf(TextFieldValue("", TextRange.Zero))
    val entryTextFieldState = mutableStateOf(TextFieldValue("", TextRange.Zero))

    fun createJot() {
        viewModel.createNote(
            useCase = EditorUseCase.Create(
                title = titleTextFieldState.value.text,
                note = entryTextFieldState.value.text,
                color = 0L
            )
        )
    }

    fun getJot(id: Long, onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        viewModel.getNote(id = id)
        if (selectedJot.value != null) {
            titleTextFieldState.value = TextFieldValue(
                text = selectedJot.value?.title.orEmpty(),
                TextRange(
                    selectedJot.value?.title.orEmpty().length,
                    selectedJot.value?.title.orEmpty().length
                )
            )
            entryTextFieldState.value = TextFieldValue(
                text = selectedJot.value?.note.orEmpty(),
                TextRange(
                    selectedJot.value?.note.orEmpty().length,
                    selectedJot.value?.note.orEmpty().length
                )
            )
            onSuccess.invoke()
        } else {
            onFailure.invoke()
        }
    }

    fun updateJot() {
        selectedJot.value?.id?.let { id ->
            viewModel.updateNote(
                useCase = EditorUseCase.Update(
                    id = id,
                    title = titleTextFieldState.value.text,
                    note = entryTextFieldState.value.text,
                    color = 0L
                )
            )
        }
    }

    fun resetTextFieldAndSelection() {
        titleTextFieldState.value = TextFieldValue()
        entryTextFieldState.value = TextFieldValue()
        viewModel.resetSelection()
    }
}