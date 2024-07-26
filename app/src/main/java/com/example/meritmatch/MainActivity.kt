package com.example.meritmatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.meritmatch.ui.HomeScreen
import com.example.meritmatch.ui.LoginPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MyApp(navController)
        }
    }
}

@Composable
fun MyApp(navController: NavHostController) {
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginPage(onAuthSuccess = { token, username ->
                if (token.isNotEmpty() && username.isNotEmpty()) {
                    navController.navigate("home/$token/$username")
                }
            })
        }
        composable("home/{token}/{username}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val points = backStackEntry.arguments?.getString("points")?.toIntOrNull() ?: 0
            HomeScreen(token = token, username = username,navController = navController )
        }
        composable("mytasks/{token}/{username}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val username = backStackEntry.arguments?.getString("username") ?: ""
            MyTasksScreen(token = token, username = username)

        }
    }
}
