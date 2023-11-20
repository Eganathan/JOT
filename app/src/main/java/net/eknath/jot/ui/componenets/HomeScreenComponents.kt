package net.eknath.jot.ui.componenets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HomeTopBarSearchComponent(
    modifier: Modifier,
    searchTextField: TextFieldValue,
    onValueChanged: (TextFieldValue) -> Unit,
    focusRequester: FocusRequester,
    onOptionsClick: () -> Unit,
    onSearchFocused: () -> Unit,
    onSearchAndClear: () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {

    val searchTextFieldInteractionSource = MutableInteractionSource()
    val searchIsFocused = searchTextFieldInteractionSource.collectIsFocusedAsState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 50.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.wrapContentWidth(Alignment.Start), onClick = onOptionsClick
            ) {
                Icon(
                    imageVector = Icons.Default.Menu, contentDescription = ""
                )
            }

            SearchTextField(
                modifier = Modifier
                    .weight(1f, fill = true)
                    .fillMaxHeight()
                    .padding(start = 5.dp),
                value = searchTextField,
                onValueChanged = onValueChanged,
                interactionSource = searchTextFieldInteractionSource,
                onSearchFocused = onSearchFocused,
                focusRequester = focusRequester
            )

            IconButton(
                modifier = Modifier.wrapContentWidth(Alignment.End), onClick = onSearchAndClear
            ) {
                Icon(
                    imageVector = if (searchIsFocused.value) Icons.Default.Close else Icons.Default.Search,
                    contentDescription = ""
                )
            }
        }

    }
}

@Composable
internal fun SearchTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChanged: (TextFieldValue) -> Unit,
    placeholder: String = "Search Your Jot here...",
    title: String = "Jot-Notes and more",
    showTitle: Boolean = false,
    interactionSource: MutableInteractionSource,
    onSearchFocused: () -> Unit,
    focusRequester: FocusRequester
) {
    val isTextFieldFocused = interactionSource.collectIsFocusedAsState()

    BasicTextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (isTextFieldFocused.value) onSearchFocused.invoke()
            },
        value = value,
        onValueChange = onValueChanged,
        textStyle = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onBackground),
        singleLine = true,
        keyboardActions = KeyboardActions(onSearch = { onValueChanged.invoke(value) }),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Search
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        interactionSource = interactionSource,
        decorationBox = { content ->
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart
            ) {
                AnimatedVisibility(value.text.isEmpty()) {
                    Text(
                        text = if (!isTextFieldFocused.value && showTitle) title else placeholder,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (!isTextFieldFocused.value && showTitle) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = (-5).dp)// because the tf has 5dp padding for input
                    )
                }
                content()
            }
        })
}