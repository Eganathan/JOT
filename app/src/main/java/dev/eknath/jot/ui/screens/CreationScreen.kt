@file:OptIn(ExperimentalMaterial3Api::class)

package dev.eknath.jot.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.eknath.jot.R
import dev.eknath.jot.shareNote
import dev.eknath.jot.ui.screens.states.EditorState
import dev.eknath.jot.wordCount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationComponent(
    visibility: State<Boolean>,
    editorState: EditorState,
    onBackPressed: () -> Unit,
    background: Color = MaterialTheme.colorScheme.background,
    focusRequester: FocusRequester = FocusRequester()
) {
    val context = LocalContext.current

    val selected = remember { mutableStateOf(true) }

//    val onToggleSelection = { //todo impl pinnable
//        selected.value = !selected.value
//    }

    val onShare = {
        context.shareNote(
            title = editorState.titleTextFieldState.value.text,
            content = editorState.entryTextFieldState.value.text
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                }
            }, title = {}, actions = {

                /*IconButton(onClick = onToggleSelection) {
                    Icon(
                        imageVector = if (selected.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = ""
                    )
                }*/

                IconButton(onClick = onShare) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "")
                }
            })
        }) {

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .imePadding()
        ) {
            val textFieldColor = TextFieldDefaults.textFieldColors(
                containerColor = background,

                disabledIndicatorColor = background,
                errorIndicatorColor = background,
                focusedIndicatorColor = background,
                unfocusedIndicatorColor = background
            )

            val onValueChange: () -> Unit = {
                if (editorState.viewModel.selectedNoteId.value == null) {
                    editorState.createJot()
                } else {
                    editorState.updateJot()
                }
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                value = editorState.titleTextFieldState.value,
                onValueChange = {
                    editorState.titleTextFieldState.value = it
                    onValueChange.invoke()
                },
                placeholder = {
                    Text(
                        stringResource(id = R.string.note_title_placeholder),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = textFieldColor,
                textStyle = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${editorState.entryTextFieldState.value.wordCount()} Words",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(start = 25.dp)
                        .padding(vertical = 5.dp)
                )
            }
            TextField(
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester),
                value = editorState.entryTextFieldState.value,
                onValueChange = {
                    editorState.entryTextFieldState.value = it
                    onValueChange.invoke()
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.note_content_placeholder),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                colors = textFieldColor,
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }

        BackHandler(enabled = true, onBack = onBackPressed)
    }

    LaunchedEffect(key1 = editorState.entryTextFieldState, block = {
        focusRequester.requestFocus()
    })

    DisposableEffect(key1 = visibility.value, effect = {
        onDispose { editorState.resetTextFieldAndSelection() }
    })
}
