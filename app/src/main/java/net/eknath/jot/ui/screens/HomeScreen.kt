package net.eknath.jot.ui.screens

import DrawerBottomCredits
import DrawerOptionsContent
import DrawerTitleContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import kotlinx.coroutines.launch
import net.eknath.jot.togglePresence
import net.eknath.jot.ui.componenets.NoteDisplayCard
import net.eknath.jot.ui.screens.states.EditorState

enum class MODE {
    VIEW, SELECTION;
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(editorState: EditorState) {

    val showCreation = remember { mutableStateOf(false) }
    val searchState = remember { mutableStateOf(false) }
    val searchFocusRequester = FocusRequester()


    val notes by editorState.viewModel.notes.asFlow().collectAsState(initial = emptyList())
    val searchedNotes by editorState.viewModel.searchResults.collectAsState(initial = emptyList())

    val sourceNotes = remember {
        derivedStateOf {
            when {
                (searchState.value && editorState.searchTextField.value.text.isNotBlank()) -> searchedNotes
                else -> notes
            }
        }
    }

    val screenMode = remember { mutableStateOf(MODE.VIEW) }
    val multiSelectedIds = remember { mutableStateOf(setOf<Long>()) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                        DrawerOptionsContent(
                            selected = "Notes",
                            options = listOf("Notes", "Lists", "Remainders"),
                            upcoming = listOf("Lists", "Remainders"),
                            onClick = {}
                        )
                    }

                    DrawerBottomCredits()
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                if (multiSelectedIds.value.isEmpty()) CenterAlignedTopAppBar(modifier = Modifier.shadow(
                    elevation = 10.dp
                ), title = {
                    Text(text = "Jot-Notes+", fontWeight = FontWeight.Medium)
                }, navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "")
                    }
                }, actions = {
                    if (searchState.value) TextField(
                        value = editorState.searchTextField.value,
                        onValueChange = {
                            editorState.searchTextField.value = it
                            editorState.setSearchQuery()
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .focusRequester(searchFocusRequester)
                    )

                    IconButton(onClick = {
                        if (searchState.value) {
                            editorState.searchTextField.value = TextFieldValue()
                            editorState.setSearchQuery()
                            searchState.value = false
                        } else {
                            editorState.searchTextField.value = TextFieldValue()
                            editorState.setSearchQuery()
                            searchState.value = true
                        }
                    }) {
                        Icon(
                            imageVector = if (searchState.value) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = ""
                        )
                    }
                })
                else TopAppBar(title = {}, navigationIcon = {
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
                        editorState.bulkDelete(multiSelectedIds.value.toList())
                        multiSelectedIds.value = setOf()
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                    }
                })
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
            LazyColumn(modifier = Modifier
                .padding(it)
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
                contentPadding = PaddingValues(5.dp),
                content = {
                    item { Spacer(modifier = Modifier.height(15.dp)) }

                    items(items = sourceNotes.value, key = { note -> note.id }) { note ->
                        NoteDisplayCard(title = note.title,
                            description = note.content,
                            isSelected = multiSelectedIds.value::contains.invoke(note.id),
                            onLongPress = {
                                screenMode.value = MODE.SELECTION
                                multiSelectedIds.value = multiSelectedIds.value.plus(note.id)
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
                })
        }
    }

    //CreationComponent
    AnimatedVisibility(
        visible = showCreation.value,
        enter = fadeIn() + slideInHorizontally(),
        exit = fadeOut() + slideOutHorizontally()
    ) {
        CreationComponent(
            visibility = showCreation,
            editorState = editorState,
            onBackPressed = {
                showCreation.value = false
            })
    }
}

