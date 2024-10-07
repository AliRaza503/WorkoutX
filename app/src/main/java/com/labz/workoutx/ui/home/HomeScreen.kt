package com.labz.workoutx.ui.home

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.labz.workoutx.ui.auth.LoginScreen
import com.labz.workoutx.ui.auth.LoginScreen.LoginScreenComposable
import com.labz.workoutx.ui.auth.SignupScreen
import com.labz.workoutx.ui.auth.SignupScreen.SignupScreenComposable

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = LoginScreen
    ) {
        composable<LoginScreen> {
            LoginScreenComposable(
                onLoginSuccess = { /* TODO: Navigate to home navController.navigate()*/ },
                onSignupClicked = { navController.navigate(SignupScreen) }
            )
        }
        composable<SignupScreen> {
            SignupScreenComposable(
                onSignupSuccess = { /* TODO: Navigate to home navController.navigate()*/ },
                onLoginClicked = { navController.navigate(LoginScreen) }
            )
        }
    }
}