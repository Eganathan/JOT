package dev.eknath.jot.ui.screens

import DrawerBottomCredits
import DrawerOptionsContent
import DrawerTitleContent
import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.asFlow
import dev.eknath.jot.R
import kotlinx.coroutines.launch
import dev.eknath.jot.togglePresence
import dev.eknath.jot.ui.componenets.HomeTopBarSearchComponent
import dev.eknath.jot.ui.componenets.NoteDisplayCard
import dev.eknath.jot.ui.componenets.NoteDisplayGridCard
import dev.eknath.jot.ui.screens.states.EditorState

enum class MODE {
    VIEW, SELECTION;
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(editorState: EditorState) {

    val showCreation = remember { mutableStateOf(false) }
    val searchState = remember { mutableStateOf(false) }
    val searchFocusRequester = FocusRequester()


    val notes by editorState.viewModel.notes.asFlow().collectAsState(initial = null)
    val searchedNotes by editorState.viewModel.searchResults.collectAsState(initial = emptyList())

    val sourceNotes = remember {
        derivedStateOf {
            when {
                (searchState.value && editorState.searchTextField.value.text.isNotBlank()) -> searchedNotes
                else -> notes
            }
        }
    }

    val useGridLayout = remember { mutableStateOf(false) }

    val screenMode = remember { mutableStateOf(MODE.VIEW) }
    val confirmationDialog = remember { mutableStateOf(false) }
    val multiSelectedIds = remember { mutableStateOf(setOf<Long>()) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val focusRequester = LocalFocusManager.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.5f)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {

                        DrawerTitleContent()
                        Spacer(modifier = Modifier.height(10.dp))
                        DrawerOptionsContent(selected = "Notes",
                            options = listOf("Notes", "Lists", "Remainders"),
                            upcoming = listOf("Lists", "Remainders"),
                            onClick = {})
                    }

                    DrawerBottomCredits()
                }
            }
        },
    ) {

        if (sourceNotes.value == null) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(300.dp)
                )
                Text(text = "Loading Jots....", modifier = Modifier.align(Alignment.Center))

            }
        } else
            Scaffold(
                modifier = Modifier.statusBarsPadding(),
                topBar = {
                    if (multiSelectedIds.value.isEmpty()) {
                        HomeTopBarSearchComponent( //todo need to look at this again too many parms
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .padding(top = 20.dp),
                            searchTextField = editorState.searchTextField.value,
                            onValueChanged = {
                                searchState.value = true
                                editorState.searchTextField.value = it
                                editorState.setSearchQuery()
                            },
                            onOptionsClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            },
                            focusRequester = searchFocusRequester,
                            onSearchFocused = {
                                searchState.value = true
                                editorState.searchTextField.value = TextFieldValue()
                            },
                            onSearchAndClear = { isActive ->
                                if (!isActive) {
                                    searchFocusRequester.requestFocus()
                                    searchState.value = true
                                } else {
                                    focusRequester.clearFocus()
                                    editorState.searchTextField.value = TextFieldValue()
                                    searchState.value = false
                                }

                            })
                    } else {
                        TopAppBar(title = {}, navigationIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = {
                                    multiSelectedIds.value = emptySet()
                                    screenMode.value = MODE.VIEW
                                }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "")
                                }
                                Text(
                                    text = if (multiSelectedIds.value.isEmpty()) "" else "${multiSelectedIds.value.size}",
                                    modifier = Modifier.offset(y = (-3).dp),
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        }, actions = {
                            IconButton(onClick = {
                                confirmationDialog.value = true
                            }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                            }
                        })
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            editorState.resetTextFieldAndSelection()
                            showCreation.value = true
                        }, shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }
                },
                floatingActionButtonPosition = FabPosition.End,
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    //Switches from LIST to GRID View vise versa
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 10.dp, vertical = 10.dp),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    IconButton(onClick = {
//                        useGridLayout.value = !useGridLayout.value
//                    }) {
//                        Icon(
//                            painter = painterResource(id = if (useGridLayout.value) R.drawable.ic_list else R.drawable.ic_grid),
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.onBackground,
//                            modifier = Modifier.size(30.dp)
//                        )
//                    }
//                }

                    if (useGridLayout.value)
                        LazyVerticalStaggeredGrid(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .padding(horizontal = 10.dp)
                                .fillMaxSize(),
                            columns = StaggeredGridCells.Fixed(2),
                            verticalItemSpacing = 5.dp,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                        ) {
                            items(
                                items = sourceNotes.value.orEmpty(),
                                key = { note -> note.id }) { note ->
                                NoteDisplayGridCard(title = note.title,
                                    description = note.content,
                                    createdTime = note.createdDate,
                                    modifiedTime = note.lastModifiedDate,
                                    isSelected = multiSelectedIds.value::contains.invoke(note.id),
                                    onLongPress = {
                                        screenMode.value = MODE.SELECTION
                                        multiSelectedIds.value =
                                            multiSelectedIds.value.plus(note.id)
                                    },
                                    onTap = {
                                        when (screenMode.value) {
                                            MODE.VIEW -> {
                                                editorState.getJot(id = note.id,
                                                    onSuccess = { showCreation.value = true },
                                                    onFailure = {

                                                    })
                                            }

                                            MODE.SELECTION -> {
                                                multiSelectedIds.value =
                                                    multiSelectedIds.value.togglePresence(note.id)
                                                if (multiSelectedIds.value.isEmpty()) screenMode.value =
                                                    MODE.VIEW
                                            }
                                        }
                                    })
                            }

                        }
                    else
                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxSize(),
                            contentPadding = PaddingValues(5.dp),
                            content = {
                                item { Spacer(modifier = Modifier.height(15.dp)) }

                                items(
                                    items = sourceNotes.value.orEmpty(),
                                    key = { note -> note.id }) { note ->
                                    NoteDisplayCard(
                                        title = note.title,
                                        description = note.content,
                                        createdTime = note.createdDate,
                                        modifiedTime = note.lastModifiedDate,
                                        isSelected = multiSelectedIds.value::contains.invoke(note.id),
                                        onLongPress = {
                                            screenMode.value = MODE.SELECTION
                                            multiSelectedIds.value =
                                                multiSelectedIds.value.plus(note.id)
                                        },
                                        onTap = {
                                            when (screenMode.value) {
                                                MODE.VIEW -> {
                                                    editorState.getJot(id = note.id,
                                                        onSuccess = { showCreation.value = true },
                                                        onFailure = {

                                                        })
                                                }

                                                MODE.SELECTION -> {
                                                    multiSelectedIds.value =
                                                        multiSelectedIds.value.togglePresence(note.id)
                                                    if (multiSelectedIds.value.isEmpty()) screenMode.value =
                                                        MODE.VIEW
                                                }
                                            }
                                        })
                                }

                                if (notes?.isEmpty() == true)
                                    item {
                                        ErrorContent()
                                    }
                            })
                }
            }

        //CreationComponent
        AnimatedVisibility(
            visible = showCreation.value, enter = fadeIn() + slideInHorizontally(), exit = fadeOut()
        ) {
            CreationComponent(visibility = showCreation,
                editorState = editorState,
                onBackPressed = {
                    showCreation.value = false
                })
        }

        AnimatedVisibility(visible = confirmationDialog.value) {
            ConfirmationDialog(
                title = "Delete Multiple notes",
                description = "Are you sure you want to delete selected ${
                    multiSelectedIds.value.toList().count()
                } notes?",
                onDismiss = {
                    confirmationDialog.value = false
                },
                onConfirm = {
                    editorState.bulkDelete(multiSelectedIds.value.toList())
                    multiSelectedIds.value = setOf()
                    confirmationDialog.value = false
                },
                confirmText = "Delete"
            )
        }
    }
}

@Composable
private fun ErrorContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_empty),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = stringResource(id = R.string.empty_note_title), fontWeight = FontWeight.Bold)
        Text(
            text = stringResource(id = R.string.empty_note_description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 5.dp, start = 30.dp, end = 30.dp)
        )
    }
}

@Composable
fun ConfirmationDialog(
    title: String,
    description: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = title, fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = description)
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Button(
                        onClick = onDismiss
                    ) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        onClick = onConfirm
                    ) {
                        Text(text = confirmText)
                    }
                }
            }
        }
    }
}

