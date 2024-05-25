package dev.eknath.jot.ui.navigation

import android.app.Activity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.eknath.jot.ui.screens.HomeScreen
import dev.eknath.jot.ui.screens.notes.NoteViewModel
import dev.eknath.jot.ui.screens.states.EditorState

sealed class Screen(
    val route: String,
    val deepLinks: List<NavDeepLink> = emptyList(),
    val content: @Composable (NavBackStackEntry) -> Unit
) {

    //Jot Screens
    data object JotListScreen : Screen(route = "1",
        content = {
            HomeScreen(
                editorState = EditorState(
                    hiltViewModel<NoteViewModel>()
                )
            )
        })

    data object JotDetailScreen : Screen(route = "2", content = { EmptyScreen() })
    data object JotViewOnlyScreen : Screen(route = "3", content = { EmptyScreen() })

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
fun AppNav(activity: Activity) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.JotListScreen.route
    ) {
        Screen.all.forEach { screen ->
            composable(
                route = screen.route,
                deepLinks = screen.deepLinks,
                content = screen.content
            )
        }

    }

}

@Composable
fun EmptyScreen(modifier: Modifier = Modifier) {
    Text(text = "Empty")
}