package com.labz.workoutx.ui.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.labz.workoutx.ui.dashboard.NavigatorObj
import com.labz.workoutx.ui.dashboard.NavigatorObj.NavigatorComposable
import com.labz.workoutx.ui.onboarding.OnBoardingScreen.OnboardingScreenComposable

@Preview(showBackground = true)
@Composable
fun OnBoardingHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = OnBoardingScreen
    ) {
        composable<OnBoardingScreen> {
            OnboardingScreenComposable(
                navController = navController
            )
        }
        composable<NavigatorObj> { NavigatorComposable() }
    }
}