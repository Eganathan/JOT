@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)

package net.eknath.jot

import android.os.Bundle
import android.os.UserManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import net.eknath.jot.ui.componenets.NoteDisplayCard
import net.eknath.jot.ui.screens.HomeScreen
import net.eknath.jot.ui.theme.JOTTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)

            JOTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}


//
//data class TabbedOption(
//    val displayName: String,
//    val identifier: Int,
//    val shape: Shape,
//    val selectedShape: Shape = shape,
//    val color: Color,
//    val selectedColor: Color = color,
//    val onClick: () -> Unit
//)
//@Composable
//fun TabbedComponents(
//    count: Int = 3,
//    selected: State<TabbedOption>,
//    options: List<TabbedOption>,
//    tabIndicator: @Composable RowScope.(TabbedOption) =
//) {
//    val list = listOf("Crop", "Field", "Machinery")
//    val selected: MutableState<Int> = remember { mutableStateOf(0) }
//    val itemUI: @Composable RowScope.(TabbedOption) -> Unit = {
//        Card(
//            modifier = Modifier
//                .defaultMinSize(minHeight = 68.dp)
//                .weight(1f)
//                .clickable { it.onClick.invoke() },
//            shape = RoundedCornerShape(
//                topStart = 16.dp, topEnd = 16.dp, bottomEnd = 0.dp, bottomStart = 0.dp
//            ),
//            elevation = CardDefaults.cardElevation(0.dp),
//            colors = CardDefaults.cardColors(containerColor = it.color)
//        ) {
//            Text(text = it.displayName)
//        }
//    }
//
//    Column {
//        Row(
//            verticalAlignment = Alignment.Bottom,
//            horizontalArrangement = Arrangement.Start,
//            modifier = Modifier
//        ) {
//            list.forEachIndexed { index, item ->
//                itemUI(item, when (index) {
//                    0 -> Green
//                    1 -> Red
//                    2 -> Yellow
//                    else -> White
//                }, {
//                    selected.value = index
//                })
//            }
//        }
//        Column(
//            modifier = Modifier
//                .offset(y = (-10).dp)
//                .fillMaxWidth()
//                .fillMaxHeight()
//                .background(
//                    when (selected.value) {
//                        0 -> Green
//                        1 -> Red
//                        2 -> Yellow
//                        else -> White
//                    }
//                )
//        ) {
//
//            Text(text = "Selected Index: ${selected.value}")
//
//        }
//    }
//}
//
//@Preview
//@Composable
//fun TabbedComPrev() {
//    TabbedComponents()
//}

@Composable
fun CustomBottomAppBar(
    backgroundColor: Color = Color.White,
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .heightIn(max = 71.dp)
                .fillMaxWidth()
        ) {

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(35.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                shape = RoundedCornerShape(
                    bottomStart = 0.dp, bottomEnd = 0.dp
                )
            ) {

            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .heightIn(max = 71.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight(), shape = RoundedCornerShape(
                        topStart = 35.dp, topEnd = 40.dp
                    ), colors = CardDefaults.cardColors(containerColor = backgroundColor)
                ) {

                }
                Spacer(modifier = Modifier.width(50.dp))
                Card(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight(), shape = RoundedCornerShape(
                        topStart = 40.dp, topEnd = 35.dp
                    ), colors = CardDefaults.cardColors(containerColor = backgroundColor)
                ) {

                }
            }
        }

        Button(shape = CircleShape,
            modifier = Modifier
                .align(Alignment.Center)
                .size(85.dp)
                .offset(y = (-40).dp),
            onClick = {}) {
            Text(
                text = "+", color = Color.Red, modifier = Modifier
            )
        }
    }
}


