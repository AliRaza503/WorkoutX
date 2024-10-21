package com.labz.workoutx.ui.dashboard

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.labz.workoutx.ui.screens.DashBoardScreen
import com.labz.workoutx.ui.screens.DashBoardScreen.DashBoardScreenComposable
import com.labz.workoutx.ui.screens.LoginScreen
import com.labz.workoutx.ui.screens.LoginScreen.LoginScreenComposable
import com.labz.workoutx.ui.screens.PermissionsScreen
import com.labz.workoutx.ui.screens.PermissionsScreen.PermissionsScreenComposable
import com.labz.workoutx.ui.screens.ProfileInitScreen
import com.labz.workoutx.ui.screens.ProfileInitScreen.ProfileInitComposable
import com.labz.workoutx.ui.screens.SignupScreen
import com.labz.workoutx.ui.screens.SignupScreen.SignupScreenComposable
import com.labz.workoutx.viewmodels.NavigatorObjViewModel
import kotlinx.serialization.Serializable

@Serializable

object NavigatorObj {
    @Composable
    fun NavigatorComposable(
        modifier: Modifier = Modifier,
        viewModel: NavigatorObjViewModel = hiltViewModel()
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = if (!viewModel.isUserLoggedIn()) LoginScreen else {
                if (viewModel.checkIfUserInfoLoaded()) DashBoardScreen else ProfileInitScreen
            }
        ) {
            composable<ProfileInitScreen> {
                ProfileInitComposable(
                    navigateToDashboard = {
                        navController.navigate(DashBoardScreen) {
                            // It must update the user profile
                            popUpTo(ProfileInitScreen) { inclusive = true }
                        }
                    }
                )
            }
            composable<LoginScreen> {
                LoginScreenComposable(
                    authenticationSucceeded = {
                        Log.d("NavigatorObj", "Auth Succeeded")
                        if (viewModel.checkIfUserInfoLoaded()) {
                            Log.d("NavigatorObj", "Navigating to DashBoardScreen")
                            navController.navigate(DashBoardScreen) {
                                popUpTo(LoginScreen) { inclusive = true }
                            }
                        } else {
                            Log.d("NavigatorObj", "Navigating to ProfileInitScreen")
                            navController.navigate(ProfileInitScreen) {
                                popUpTo(LoginScreen) { inclusive = true }
                            }
                        }
                    },
                    onSignupClicked = {
                        navController.navigate(SignupScreen) {
                            popUpTo(LoginScreen) { inclusive = true }
                        }
                    }
                )
            }
            composable<SignupScreen> {
                SignupScreenComposable(
                    authenticationSucceeded = {
                        if (viewModel.checkIfUserInfoLoaded()) {
                            navController.navigate(DashBoardScreen) {
                                popUpTo(SignupScreen) { inclusive = true }
                            }
                        } else {
                            navController.navigate(ProfileInitScreen) {
                                popUpTo(SignupScreen) { inclusive = true }
                            }
                        }
                    },
                    onLoginClicked = {
                        navController.navigate(LoginScreen) {
                            popUpTo(SignupScreen) { inclusive = true }
                        }
                    }
                )
            }
            composable<DashBoardScreen> {
                DashBoardScreenComposable(
                    signedOut = {
                        navController.navigate(LoginScreen) {
                            popUpTo(DashBoardScreen) { inclusive = true }
                        }
                    },
                    noPermissionsGrantedAction = {
                        navController.navigate(PermissionsScreen) {
                            popUpTo(DashBoardScreen) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable<PermissionsScreen> {
                PermissionsScreenComposable(
                    onPermissionsGranted = {
                        navController.navigate(
                            DashBoardScreen
                        ) {
                            popUpTo(PermissionsScreen) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}