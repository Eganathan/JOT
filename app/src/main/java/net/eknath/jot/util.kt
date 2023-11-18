package net.eknath.jot

import android.content.Context
import android.content.Intent
import android.util.Log
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