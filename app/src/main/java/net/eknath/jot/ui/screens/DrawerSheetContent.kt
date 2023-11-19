import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DrawerBottomCredits() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            Text(
                text = "Made with ",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "",
                tint = Color.Red,
                modifier = Modifier.size(15.dp)
            )
        }
        Text(
            text = "Asmakam-AppStudio",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DrawerTitleContent() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 5.dp)
                .padding(start = 10.dp, top = 5.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "JOT |",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier,
                textAlign = TextAlign.Start,
            )
            Text(
                text = "Notes &\nmore",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .offset(y = 3.dp)
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 10.dp)
        )
    }
}

@Composable
internal fun DrawerOptionsContent(
    selected: String,
    options: List<String>,
    upcoming: List<String>,
    onClick: (String) -> Unit
) {
    options.forEach { option ->
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp, bottom = 5.dp),
            shape = RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected == option) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiaryContainer
            ),
            enabled = !upcoming.contains(option),
            onClick = { onClick.invoke(option) }
        ) {
            Text(
                text = if (upcoming.contains(option)) option.plus(" [Soon!]") else option,
                textAlign = TextAlign.Start,
                color = if (selected == option) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
            )

        }
    }
}