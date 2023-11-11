@file:OptIn(ExperimentalMaterial3Api::class)

package net.eknath.jot.ui.componenets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun NoteDisplayCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    color: Color = MaterialTheme.colorScheme.primary,
    borderColor: Color = Color.Red,//MaterialTheme.colorScheme.secondary,
    shape: Shape = RoundedCornerShape(10.dp),
    isSelected: Boolean,
    onClick: () -> Unit,
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


    Card(
        modifier = cardModifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = color),
//        onClick =
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
        )
        {
            if (title.isNotBlank())
                Text(text = title)
            Text(text = description)
        }

    }
}