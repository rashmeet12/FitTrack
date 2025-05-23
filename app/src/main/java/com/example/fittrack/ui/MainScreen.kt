package com.example.fittrack.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fittrack.R
import com.example.fittrack.navigation.FitTrackNavGraph
import com.example.fittrack.navigation.Screen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                listOf(
                    Pair(Screen.Dashboard, R.drawable.ic_dashboard),
                    Pair(Screen.Workout, R.drawable.ic_workout),
                    Pair(Screen.History, R.drawable.ic_history),
                    Pair(Screen.Profile, R.drawable.ic_profile)
                ).forEach { (screen, iconResId) ->
                    NavigationBarItem(
                        icon = { Icon(painterResource(id = iconResId), contentDescription = null) },
                        label = { Text(screen.route.replaceFirstChar { it.uppercase() }) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        FitTrackNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}