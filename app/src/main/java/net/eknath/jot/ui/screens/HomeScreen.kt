package net.eknath.jot.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.eknath.jot.ui.componenets.NoteDisplayCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val showCreation = remember { mutableStateOf(false) }
    Scaffold(bottomBar = {
        BottomAppBar {}
    }, floatingActionButton = {
        FloatingActionButton(onClick = {
            showCreation.value = true
        }, shape = RoundedCornerShape(10.dp)) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "")
        }
    }, floatingActionButtonPosition = FabPosition.End
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(5.dp),
            content = {
                items(25) {
                    NoteDisplayCard(
                        title = "SOme Title Was here ${it}",
                        description = "Descripton ${it} of the title required!",
                        onClick = {})
                }
            })
    }

    //CreationComponent
    AnimatedVisibility(
        visible = showCreation.value,
        enter = fadeIn() + slideInHorizontally(),
        exit = fadeOut() + slideOutHorizontally()
    ) {
        CreationComponent(
            visibility = showCreation,
            onBackPressed = { showCreation.value = false })
    }
}
