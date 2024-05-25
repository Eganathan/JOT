package dev.eknath.jot.ui.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
internal fun AppNav(activity: Activity) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.JotListScreen.route
    ) {
        Screen.all.forEach { screen ->
            composable(
                route = screen.route,
                deepLinks = screen.deepLinks,
                content = screen.content
            )
        }

    }

}