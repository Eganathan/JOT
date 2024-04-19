@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package dev.eknath.jot.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.eknath.jot.ui.screens.HomeScreen
import dev.eknath.jot.ui.screens.NoteViewModel
import dev.eknath.jot.ui.screens.states.EditorState
import dev.eknath.jot.ui.theme.JOTTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)

            JOTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(editorState = EditorState(hiltViewModel<NoteViewModel>()))
                }
            }
        }
    }
}

