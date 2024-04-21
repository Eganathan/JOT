@file:OptIn(ExperimentalMaterial3Api::class)

package dev.eknath.jot.ui.componenets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.eknath.jot.onLongPressDetect

@Composable
fun NoteDisplayCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    createdTime: String,
    modifiedTime: String,
    color: Color = MaterialTheme.colorScheme.primary,
    borderColor: Color = Color.Red,//MaterialTheme.colorScheme.secondary,
    shape: Shape = RoundedCornerShape(10.dp),
    isSelected: Boolean,
    onTap: () -> Unit,
    onLongPress: () -> Unit,
    minSize: Boolean = false,
) {

    val cardModifier = modifier
        .padding(2.5.dp)
        .then(
            if (minSize) Modifier.wrapContentSize()
            else Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) then (if (isSelected) Modifier.border(
        width = 3.dp,
        color = borderColor,
        shape = shape
    ) else Modifier)
        .onLongPressDetect(
            onLongPress = onLongPress,
            onTap = onTap
        )


    Card(
        modifier = cardModifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = color),
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        )
        {
            if (title.isNotBlank())
                Text(
                    text = title.trimStart(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            Text(
                text = description.trimStart(),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Modified ${modifiedTime}",
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.8f)
                )
        }

    }
}

fun String.trimStart(): String {
    return replace("\\s+".toRegex(), "")
}