package dev.eknath.jot.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import dev.eknath.jot.ui.screens.HomeScreen
import dev.eknath.jot.ui.screens.notes.NoteViewModel
import dev.eknath.jot.ui.screens.states.EditorState

internal sealed class Screen(
    val route: String,
    val deepLinks: List<NavDeepLink> = emptyList(),
    val content: @Composable (NavBackStackEntry) -> Unit
) {
    data object JotListScreen : Screen(route = "JOT_NOTE_LIST_SCREEN",
        content = {
            HomeScreen(
                editorState = EditorState(
                    hiltViewModel<NoteViewModel>()
                )
            )
        })

    data object JotEditScreen : Screen(route = "JOT_NOTE_DETAIL_SCREEN",
        content = { EmptyScreen() })

    data object JotViewOnlyScreen :
        Screen(route = "JOT_NOTE_VIEW_MODE", content = { EmptyScreen() })

    companion object {
        //gettingViaReflection
        val all: List<Screen> by lazy {
            Screen::class.sealedSubclasses.mapNotNull { subclass ->
                subclass.objectInstance
            }
        }
    }
}

@Composable
fun EmptyScreen(modifier: Modifier = Modifier) {
    Text(text = "Empty")
}