package net.eknath.jot

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