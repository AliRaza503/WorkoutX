package com.labz.workoutx.ui.navigator

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.labz.workoutx.ui.screens.DashBoardScreen
import com.labz.workoutx.ui.screens.LoginScreen
import com.labz.workoutx.ui.screens.LoginScreen.LoginScreenComposable
import com.labz.workoutx.ui.screens.PerformWorkoutScreen
import com.labz.workoutx.ui.screens.PermissionsScreen
import com.labz.workoutx.ui.screens.PermissionsScreen.PermissionsScreenComposable
import com.labz.workoutx.ui.screens.ProfileInitScreen
import com.labz.workoutx.ui.screens.ProfileInitScreen.ProfileInitComposable
import com.labz.workoutx.ui.screens.SignupScreen
import com.labz.workoutx.ui.screens.SignupScreen.SignupScreenComposable
import com.labz.workoutx.ui.screens.WorkoutFinishedCongratsScreen
import com.labz.workoutx.ui.screens.WorkoutFinishedCongratsScreen.WorkoutFinishedCongratsScreenComposable
import com.labz.workoutx.ui.screens.WorkoutPlanningScreen
import com.labz.workoutx.viewmodels.NavigatorObjViewModel
import kotlinx.serialization.Serializable

@Serializable
object NavigatorObj {
    @Composable
    fun NavigatorComposable(
        modifier: Modifier = Modifier,
        viewModel: NavigatorObjViewModel = hiltViewModel()
    ) {
        val isUserInfoLoaded by viewModel.isUserInfoLoaded.collectAsStateWithLifecycle()
        val areAllPermissionsGranted by viewModel.areAllPermissionsGranted.collectAsStateWithLifecycle()
        if (null == isUserInfoLoaded || null == areAllPermissionsGranted) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = if (!viewModel.isUserLoggedIn()) LoginScreen else {
                    if (true == isUserInfoLoaded) {
                        if (true == areAllPermissionsGranted) DashBoardScreen(permissionsGranted = true)
                        else PermissionsScreen
                    } else ProfileInitScreen
                }
            ) {
                composable<ProfileInitScreen> {
                    ProfileInitComposable(
                        navigateToDashboard = {
                            if (true == areAllPermissionsGranted) {
                                navController.navigate(DashBoardScreen(permissionsGranted = true)) {
                                    popUpTo(ProfileInitScreen) { inclusive = true }
                                }
                            } else {
                                navController.navigate(PermissionsScreen) {
                                    popUpTo(ProfileInitScreen) { inclusive = true }
                                }
                            }
                        }
                    )
                }
                composable<LoginScreen> {
                    LoginScreenComposable(
                        authenticationSucceeded = {
                            viewModel.checkIfUserInfoLoaded()
                            if (true == isUserInfoLoaded) {
                                if (true == areAllPermissionsGranted) {
                                    navController.navigate(DashBoardScreen(permissionsGranted = true)) {
                                        popUpTo(LoginScreen) { inclusive = true }
                                    }
                                } else {
                                    navController.navigate(PermissionsScreen) {
                                        popUpTo(LoginScreen) { inclusive = true }
                                    }
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
                            viewModel.checkIfUserInfoLoaded()
                            if (true == isUserInfoLoaded) {
                                if (true == areAllPermissionsGranted) {
                                    navController.navigate(DashBoardScreen(permissionsGranted = true)) {
                                        popUpTo(SignupScreen) { inclusive = true }
                                    }
                                } else {
                                    navController.navigate(PermissionsScreen) {
                                        popUpTo(SignupScreen) { inclusive = true }
                                    }
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

                composable<DashBoardScreen> { backStackEntry ->
                    val dashBoardScreen = backStackEntry.toRoute<DashBoardScreen>()
                    dashBoardScreen.DashBoardScreenComposable(
                        modifier = modifier,
                        navigateToLoginScreen = {
                            navController.navigate(LoginScreen) {
                                popUpTo(dashBoardScreen) { inclusive = true }
                            }
                        },
                        navigateToProfileInitScreen = {
                            navController.navigate(ProfileInitScreen) {
                                popUpTo(dashBoardScreen) { inclusive = false }
                            }
                        },
                        navigateToWorkoutPlanningScreen = { workoutId ->
                            navController.navigate(WorkoutPlanningScreen(workoutId)) {
                                popUpTo(dashBoardScreen) { inclusive = false }
                            }
                        }
                    )
                }
                composable<PermissionsScreen> {
                    PermissionsScreenComposable(
                        onPermissionsGranted = {
                            navController.navigate(
                                DashBoardScreen(permissionsGranted = true)
                            ) {
                                popUpTo(PermissionsScreen) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable<WorkoutPlanningScreen> { backStackEntry ->
                    val workoutPlanningScreen = backStackEntry.toRoute<WorkoutPlanningScreen>()
                    workoutPlanningScreen.WorkoutPlanningScreenComposable (
                        startWorkout = { workoutID ->
                            navController.navigate(PerformWorkoutScreen(workoutID)) {
                                popUpTo(workoutPlanningScreen) { inclusive = false }
                            }
                        }
                    )
                }
                composable<PerformWorkoutScreen> { backStackEntry ->
                    val performWorkoutScreen = backStackEntry.toRoute<PerformWorkoutScreen>()
                    performWorkoutScreen.PerformWorkoutComposable(
                        onWorkoutFinished = {
                            navController.navigate(WorkoutFinishedCongratsScreen) {
                                popUpTo(performWorkoutScreen) { inclusive = true }
                            }
                        },
                        onWorkoutCancelled = {
                            navController.navigateUp()
                        }
                    )
                }

                composable<WorkoutFinishedCongratsScreen> {
                    WorkoutFinishedCongratsScreenComposable(
                        backToHome = {
                            navController.navigate(DashBoardScreen(permissionsGranted = true)) {
                                popUpTo(WorkoutFinishedCongratsScreen) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}