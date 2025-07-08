package com.example.fittrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fittrack.ui.activity.ActivityScreen
import com.example.fittrack.ui.article.ArticleDetailScreen
import com.example.fittrack.ui.article.ArticlesScreen
import com.example.fittrack.ui.article.sampleArticles
import com.example.fittrack.ui.auth.AuthState
import com.example.fittrack.ui.auth.AuthViewModel
import com.example.fittrack.ui.auth.LoginScreen
import com.example.fittrack.ui.auth.RegisterScreen
import com.example.fittrack.ui.bmi.BmiScreen
import com.example.fittrack.ui.history.HistoryScreen
import com.example.fittrack.ui.home.HomeScreen
import com.example.fittrack.ui.profile.ProfileScreen
import com.example.fittrack.ui.profile.ProfileSetupScreen
import com.example.fittrack.ui.route.RouteScreen
import com.example.fittrack.ui.step.StepScreen
import com.example.fittrack.ui.theme.FitTrackTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            FitTrackTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                        AppNavHost()
                    }
                }


            }
        }
    }
}

@Composable
fun AppNavHost(authViewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            LaunchedEffect(isAuthenticated) {
                when (isAuthenticated) {
                    true -> {
                        // Check auth state to determine destination
                        val authState = authViewModel.uiState.value.authState
                        when (authState) {
                            AuthState.Authenticated -> {
                                navController.navigate(route = "home") {
                                    popUpTo(route = "splash") { inclusive = true }
                                }
                            }
                            AuthState.ProfileSetupRequired -> {
                                navController.navigate(route = "profile_setup") {
                                    popUpTo(route = "splash") { inclusive = true }
                                }
                            }
                            else -> {
                                navController.navigate(route = "login") {
                                    popUpTo(route = "splash") { inclusive = true }
                                }
                            }
                        }
                    }
                    false -> {
                        navController.navigate(route = "login") {
                            popUpTo(route = "splash") { inclusive = true }
                        }
                    }
                    else -> Unit // Still loading
                }
            }
        }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("profile_setup") { ProfileSetupScreen(navController, hiltViewModel())}
        composable("home") { HomeScreen(navController) }
        composable("step") { StepScreen(navController) }
        composable("bmi") { BmiScreen(navController) }
        composable("route") { RouteScreen(navController) }
        composable("activity") { ActivityScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("articles") { ArticlesScreen(navController) }
        composable("article_detail/{articleId}") { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId")?.toIntOrNull()
            val article = sampleArticles.find { it.id == articleId }
            if (article != null) {
                ArticleDetailScreen(article = article, navController = navController)
            }
        }
        composable("profile") {
            ProfileScreen(
                onNavigateBack = { navController.navigateUp() },
                onEditProfile = { navController.navigate("profile_setup") },
                viewModel = hiltViewModel()
            )
        }


    }
}

