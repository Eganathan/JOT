package dev.eknath.jot

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.eknath.jot.ui.constants.JOTColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
data class MenuItem(val iconRes: Int, val title: String, val onClick: () -> Unit)

@Composable
private fun MenuItem(menuItem: MenuItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = menuItem.onClick)
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            painter = painterResource(id = menuItem.iconRes), contentDescription = menuItem.title,
            modifier = Modifier
                .size(25.dp)
                .offset(y = 3.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = menuItem.title,
            modifier = Modifier
                .padding(end = 20.dp, bottom = 10.dp),
        )
    }
}

@Composable
fun ContextMenuButton(enabled: Boolean, state: MutableState<Boolean>, menuItems: List<MenuItem>) {
    //remove state from Mutablestate to state and add a callback
    val isOpen by remember { derivedStateOf { state.value } }

    IconButton(enabled = enabled, onClick = { state.value = !state.value }) {
        Icon(
            Icons.Filled.MoreVert, contentDescription = "More options",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.graphicsLayer {
                this.alpha = if (enabled) 1f else 0.3f
            }
        )
    }

    DropdownMenu(
        modifier = Modifier.padding(bottom = 5.dp, top = 10.dp),
        expanded = isOpen,
        onDismissRequest = { state.value = false }
    ) {
        menuItems.forEach { item ->
            MenuItem(item)
        }
    }
}

//vertical Selection
@Composable
fun ColorsSelectors(selectedJotColor: JOTColors) {
    var state by remember { mutableStateOf(false) }
    val isOpen by remember { derivedStateOf { state } }
    val isDarkTheme = isSystemInDarkTheme()

    IconButton(onClick = { state = !state }) {
        Card(
            modifier = Modifier
                .size(35.dp)
                .clip(CircleShape),
            colors = CardDefaults.cardColors(if (isDarkTheme) selectedJotColor.dark else selectedJotColor.light)
        ) {}
    }

    if (isOpen)
        Dialog(onDismissRequest = { state = false }) {
            Card(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Choose a color:")
                Row(modifier = Modifier.padding(vertical = 5.dp)) {
                    JOTColors.entries.forEach {
                        Spacer(modifier = Modifier.width(5.dp))
                        Card(
                            modifier = Modifier
                                .size(50.dp),
                            shape = RectangleShape,
                            border = BorderStroke(width = 2.dp, color = Color.Red),
                            colors = CardDefaults.cardColors(if (isDarkTheme) it.dark else it.light)
                        ) {}
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                }
            }
        }
}

fun Long.getDate(): String {
    if (this == 0L) return ""

    val date = Date(this)
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    return formatter.format(date)
}

fun String.toLongTimestamp(): Long {
    val format = SimpleDateFormat("dd-MM-yyyy") // Assuming the same format used for conversion
    try {
        val date = format.parse(this)
        return date?.time ?: 0L // Return 0L if parsing fails
    } catch (e: Exception) {
        e.printStackTrace()
        return 0L // Handle parsing exceptions (optional)
    }
}


fun formatDateForUi(inputDate: String): String {
    // Define the input and output date formats
    val inputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val outputDateFormat = SimpleDateFormat("dd MMMM yy", Locale.getDefault())

    // Parse the input date
    val date: Date = inputDateFormat.parse(inputDate) ?: return "-"

    // Get today's date
    val today = Calendar.getInstance()

    // Get the date for comparison
    val inputCalendar = Calendar.getInstance().apply { time = date }

    // Check if the input date is today
    if (isSameDay(inputCalendar, today)) {
        return "Today"
    }

    // Check if the input date is yesterday
    today.add(Calendar.DATE, -1)
    if (isSameDay(inputCalendar, today)) {
        return "Yesterday"
    }

    // Return the formatted date for other days
    return outputDateFormat.format(date)
}

// Helper function to compare if two calendars are on the same day
private fun isSameDay(calendar1: Calendar, calendar2: Calendar): Boolean {
    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
}
