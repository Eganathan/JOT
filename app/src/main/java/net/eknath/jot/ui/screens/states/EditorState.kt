package net.eknath.jot.ui.screens.states

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import net.eknath.jot.ui.screens.EditorUseCase
import net.eknath.jot.ui.screens.EditorViewModel

class EditorState(private val viewModel: EditorViewModel) {

    val jotterList = viewModel.state

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

}