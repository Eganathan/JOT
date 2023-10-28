@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)

package net.eknath.jot

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import net.eknath.jot.ui.componenets.NoteDisplayCard
import net.eknath.jot.ui.theme.JOTTheme

@OptIn(ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JOTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(floatingActionButtonPosition = FabPosition.Center,
                        bottomBar = { CustomBottomAppBar() },
                        floatingActionButton = {

                        }) {
                        Text(text = "Sample App", modifier = Modifier.padding(it))
                    }

                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Scaffold(bottomBar = {
        BottomAppBar {}
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {}, shape = RoundedCornerShape(10.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
        }
    }, floatingActionButtonPosition = FabPosition.End
    ) {
        LazyVerticalStaggeredGrid(modifier = Modifier.padding(it),
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(5.dp),
            content = {
                item {
                    NoteDisplayCard(title = "SOme Title Was here ${it}",
                        description = "Descripton ${it} of the title required! Something, else and something else plus plus and plus lets seeeejdnf sdojn nnidjije jijwe9j dwewr jasdqwe9 odkdqwer",
                        onClick = {})
                }
                items(25) {
                    NoteDisplayCard(title = "SOme Title Was here ${it}",
                        description = "Descripton ${it} of the title required!",
                        onClick = {})
                }
            })
    }
}

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


