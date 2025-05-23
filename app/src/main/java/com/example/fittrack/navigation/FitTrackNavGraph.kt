package com.example.fittrack.navigation



import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fittrack.ui.screens.dashboard.DashboardScreen
import com.example.fittrack.ui.screens.history.HistoryScreen
import com.example.fittrack.ui.screens.profile.ProfileScreen
import com.example.fittrack.ui.screens.workout.WorkoutScreen

@Composable
fun FitTrackNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Screen.Workout.route) {
            WorkoutScreen(navController = navController)
        }
        composable(Screen.History.route) {
            HistoryScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}