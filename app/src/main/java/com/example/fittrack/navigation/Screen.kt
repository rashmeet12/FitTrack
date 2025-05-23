package com.example.fittrack.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Workout : Screen("workout")
    object History : Screen("history")
    object Profile : Screen("profile")
}