@file:OptIn(ExperimentalMaterial3Api::class)

package dev.eknath.jot.ui.screens

import android.provider.CalendarContract.Colors
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.eknath.jot.ColorsSelectors
import dev.eknath.jot.ContextMenuButton
import dev.eknath.jot.MenuItem
import dev.eknath.jot.R
import dev.eknath.jot.readOutLoud
import dev.eknath.jot.shareNote
import dev.eknath.jot.ui.constants.JOTColors
import dev.eknath.jot.ui.screens.states.EditorState
import dev.eknath.jot.wordCount
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()
    var lastJob: Job? = null

    val confirmationDialog = remember { mutableStateOf(false) }
    val isFavourite by remember { derivedStateOf { editorState.isFavorite.value } }

    val isDarkTheme = isSystemInDarkTheme()
    var selectedColor =
        MaterialTheme.colorScheme.background //by remember { mutableStateOf(if (isDarkTheme) JOTColors.GREEN.dark else JOTColors.GREEN.light) }


    val onShare = {
        context.shareNote(
            title = editorState.titleTextFieldState.value.text,
            content = editorState.entryTextFieldState.value.text
        )
    }

    val textFieldColor = TextFieldDefaults.textFieldColors(
        containerColor = selectedColor,

        disabledIndicatorColor = selectedColor,
        errorIndicatorColor = selectedColor,
        focusedIndicatorColor = selectedColor,
        unfocusedIndicatorColor = selectedColor
    )

    val onValueChange: () -> Unit = {
        lastJob?.cancel()
        lastJob = scope.launch {
            delay(300)
            if (editorState.viewModel.selectedNoteId.value == null) {
                editorState.createJot()
            } else {
                editorState.updateJot()
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                }
            }, title = {},
                actions = {

//                   IconButton(onClick = onToggleSelection) {
//                        Icon(
//                            imageVector = if (selected.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
//                            contentDescription = ""
//                        )
//                    }
                    IconButton(
                        enabled = editorState.entryTextFieldState.value.text.isNotBlank(),
                        onClick = {
                            readOutLoud(
                                context = context,
                                noteText = editorState.entryTextFieldState.value.text,
                                id = editorState.viewModel.selectedNote.value?.id.toString(),
                                onStart = {
                                },
                                onEnd = {
                                },
                                onForceStop = {
                                    it.stop()
                                    it.shutdown()
                                }
                            )
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    }
//                    IconButton(
//                        onClick = {
//
//                        }) {
//                        Card(
//                            modifier = Modifier
//                                .size(35.dp)
//                                .clip(CircleShape),
//                            colors = CardDefaults.cardColors(selectedColor)
//                        ) {}
//                    }

//                    ColorsSelectors(selectedJotColor = JOTColors.GREEN)

                    IconButton(
                        onClick = {
                            editorState.switchFavorite(isFav = !isFavourite)
                        }) {
                        Icon(
                            painter = painterResource(id = if (isFavourite) R.drawable.ic_selected else R.drawable.ic_unselected_star),
                            contentDescription = "",
                            modifier = Modifier.size(25.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    IconButton(
                        onClick = {
                            onValueChange()
                            onBackPressed()
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_save),
                            contentDescription = "",
                            modifier = Modifier.size(25.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    ContextMenuButton(
                        listOf(
                            MenuItem(
                                iconRes = R.drawable.ic_share,
                                title = "Share",
                                onClick = onShare
                            ),
                            MenuItem(
                                iconRes = R.drawable.ic_delete,
                                title = "Delete",
                                onClick = {
                                    confirmationDialog.value = true
                                }
                            )
                        )
                    )
                })
        },
        containerColor = selectedColor
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .imePadding(),
            verticalArrangement = Arrangement.Top
        ) {

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
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.alpha(0.5f)
                    )
                },
                colors = textFieldColor,
                textStyle = MaterialTheme.typography.titleLarge,
            )

            TextField(
                modifier = Modifier
                    .offset(y = (-17).dp)
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

    AnimatedVisibility(visible = confirmationDialog.value) {
        ConfirmationDialog(
            title = "Delete Note",
            description = "Are you sure you want to delete note?",
            onDismiss = {
                confirmationDialog.value = false
            },
            onConfirm = {
                confirmationDialog.value = false
                editorState.viewModel.selectedNoteId.value?.let {
                    editorState.deleteNote(it)
                    onBackPressed()
                }

            },
            confirmText = "Delete"
        )
    }


    LaunchedEffect(key1 = editorState.entryTextFieldState, block = {
        focusRequester.requestFocus()
    })

    DisposableEffect(key1 = visibility.value, effect = {
        onDispose { editorState.resetTextFieldAndSelection() }
    })
}
