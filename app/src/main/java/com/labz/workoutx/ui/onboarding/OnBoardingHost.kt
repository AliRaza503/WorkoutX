package com.labz.workoutx.ui.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.labz.workoutx.ui.auth.LoginScreen
import com.labz.workoutx.ui.auth.SignupScreen
import com.labz.workoutx.ui.home.HomeScreen

@Preview(showBackground = true)
@Composable
fun OnBoardingHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "onboarding_screen1"
    ) {
        composable("onboarding_screen1") { OnboardingScreen(navController) }
        composable("Home") { HomeScreen() }
    }
}