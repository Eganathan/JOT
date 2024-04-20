package dev.eknath.jot

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.TextFieldValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

// A Helper function that enables the user to share his notes
fun Context.shareNote(title: String, content: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "${title}\n${content}")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share your JOT-Note")
    this.startActivity(shareIntent)
}

//The helper function for the work count
fun TextFieldValue.wordCount(): Int {
    return if (this.text.isBlank()) {
        0
    } else {
        this.text.split("\\b[a-zA-Z]+[ ]".toRegex()).count().minus(1)
    }
}

//helps in converting db date to string for ui
fun Long.toDateString(): String {
    return try {
        val dateFormatter = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.getDefault())
        dateFormatter.format(Date(this))
    } catch (e: Exception) {
        Log.e("Date", e.localizedMessage ?: e.stackTraceToString())
        ""
    }
}

// detects long press or tap and handles the on click
fun Modifier.onLongPressDetect(
    onLongPress: () -> Unit, onTap: () -> Unit
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onLongPress = { onLongPress() }, onTap = {
            onTap.invoke()
        })
    }
}

//returns a new set with the provided element if the set does not already have it else returns a new set removing the provided element...it is used for multi select
fun Set<Long>.togglePresence(element: Long): Set<Long> {
    return if (this.contains(element)) this.minus(element) else this.plus(element)
}

fun readOutLoud(
    context: Context,
    noteText: String,
    id: String,
    onStart: () -> Unit,
    onEnd: (isError: Boolean) -> Unit,
    onForceStop: (TextToSpeech) -> Unit
) {
    var tts: TextToSpeech? = null
    tts = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            //the voice settings here -- will add later

            //reads the text on Success
            tts?.speak(noteText, TextToSpeech.QUEUE_FLUSH, null, id)

            //progress listener
            tts?.setOnUtteranceProgressListener(object :
                android.speech.tts.UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    onStart()
                    Log.e("TTS", "Started")
                }

                override fun onDone(utteranceId: String?) {
                    Log.e("TTS", "Completed")
                    onEnd(false)
                    tts?.shutdown()
                }

                override fun onError(utteranceId: String?) {
                    Log.e("TTS", "Error")
                    onEnd(true)
                    tts?.shutdown()
                }
            })
        } else {
            Log.e("TTS", "FAILED")
            onEnd(true)
            tts?.shutdown()
        }
    }
    onForceStop(tts)
}


//Menu
@Stable
data class MenuItem(val title: String, val onClick: () -> Unit)

@Composable
private fun MenuItem(menuItem: MenuItem) {
    Text(
        text = menuItem.title,
        modifier = Modifier
            .clickable(onClick = menuItem.onClick)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
    )
}

@Composable
fun ContextMenuButton(menuItems: List<MenuItem>) {
    var state by remember { mutableStateOf(false) }
    val isOpen by remember { derivedStateOf { state } }

    IconButton(onClick = { state = !state }) {
        Icon(Icons.Filled.MoreVert, contentDescription = "More options")
    }

    DropdownMenu(
        expanded = isOpen,
        onDismissRequest = { state = false }
    ) {
        menuItems.forEach { item ->
            MenuItem(item)
        }
    }
}