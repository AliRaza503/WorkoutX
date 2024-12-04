package com.labz.workoutx.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.labz.workoutx.R
import com.labz.workoutx.models.User
import com.labz.workoutx.models.Workout
import com.labz.workoutx.ui.exts.ProgressesComposable
import com.labz.workoutx.uistates.DashboardUiState
import com.labz.workoutx.viewmodels.DashboardViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class DashBoardScreen(
    val permissionsGranted: Boolean
) {
    private data class BottomBarItem(
        val label: String,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector,
        val onClickAction: () -> Unit
    )

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DashBoardScreenComposable(
        navigateToLoginScreen: () -> Unit,
        navigateToProfileInitScreen: () -> Unit,
        navigateToWorkoutPlanningScreen: (workoutId: String) -> Unit,
        modifier: Modifier = Modifier,
        viewModel: DashboardViewModel = hiltViewModel()
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val photoBgColor = Color(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DashBoardDrawer(
                    photoBgColor = photoBgColor,
                    logout = {
                        viewModel.signOut {
                            navigateToLoginScreen()
                        }
                    },
                    navigateToProfileInitScreen = navigateToProfileInitScreen
                )
            },
        ) {
            val bottomBarItems = listOf(
                BottomBarItem(
                    label = "Home",
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    onClickAction = {}
                ),
                BottomBarItem(
                    label = "Workouts",
                    selectedIcon = ImageVector.vectorResource(id = R.drawable.ic_workout),
                    unselectedIcon = ImageVector.vectorResource(id = R.drawable.ic_workout),
                    onClickAction = {}
                ),
                BottomBarItem(
                    label = "Workout History",
                    selectedIcon = ImageVector.vectorResource(id = R.drawable.ic_history_selected),
                    unselectedIcon = ImageVector.vectorResource(id = R.drawable.ic_history_unselected),
                    onClickAction = {}
                )
            )
            Scaffold(
                topBar = {
                    DashBoardTopAppBar(
                        openNavDrawer = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
                        title = if (uiState.bottomBarSelectedItemIndex == 0) "WorkoutX" else if (uiState.bottomBarSelectedItemIndex == 1) viewModel.getGoal() else "Workout History",
                        photoBgColor = photoBgColor,
                        modifier = modifier
                    )
                },
                bottomBar = {
                    NavigationBar {
                        bottomBarItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = uiState.bottomBarSelectedItemIndex == index,
                                icon = {
                                    Icon(
                                        imageVector = if (uiState.bottomBarSelectedItemIndex == index) {
                                            item.selectedIcon
                                        } else {
                                            item.unselectedIcon
                                        },
                                        contentDescription = item.label
                                    )
                                },
                                onClick = {
                                    viewModel.updateBottomBarSelectedItemIndex(index)
                                    item.onClickAction()
                                },
                                enabled = uiState.areProgressesLoaded
                            )
                        }
                    }
                }
            ) { innerPadding ->
                if (uiState.bottomBarSelectedItemIndex == 0) {
                    HomeScreen(
                        modifier = modifier.padding(innerPadding),
                        uiState = uiState,
                        viewModel = viewModel
                    )
                } else if (uiState.bottomBarSelectedItemIndex == 1) {
                    ChooseWorkoutScreen.ChooseWorkoutScreenComposable(
                        navigateToWorkoutPlanningScreen = { workoutId ->
                            navigateToWorkoutPlanningScreen(workoutId)
                        },
                        modifier = modifier.padding(innerPadding),
                    )
                } else if (uiState.bottomBarSelectedItemIndex == 2) {
                    viewModel.getWorkoutHistory()
                    WorkoutHistoryScreen(
                        modifier = modifier.padding(innerPadding),
                        history = uiState.workoutHistory
                    )
                }
            }
        }

    }

    @Composable
    private fun WorkoutHistoryScreen(
        modifier: Modifier = Modifier,
        history: List<Pair<String, Workout>>
    ) {
        Box(
            modifier = modifier
                .fillMaxSize(),
        ) {
            if (history.isEmpty()) {
                Text(
                    text = "No workout history available",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    history.forEach { (date, workout) ->
                        val workoutName = workout.name
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = workoutName,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            if (workout.isRepeatable && workout.repCount > 0) {
                                Text(
                                    text = "${workout.repCount} times",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Text(
                                    text = "${workout.targetMinutes} minutes",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Text(
                                text = date,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun HomeScreen(
        modifier: Modifier = Modifier,
        uiState: DashboardUiState,
        viewModel: DashboardViewModel
    ) {
        when {
            uiState.isCircularProgressIndicatorVisible -> {
                Box(
                    modifier = modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                // Your normal content when permissions are granted
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProgressesComposable(
                        onProgressesLoaded = {
                            viewModel.progressesAreLoaded()
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun DashBoardDrawer(
        photoBgColor: Color,
        logout: () -> Unit,
        navigateToProfileInitScreen: () -> Unit
    ) {
        ModalDrawerSheet {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserPhotoRoundComposable(color = photoBgColor, size = 100)
                    Text(
                        User.name ?: "Preferences",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text(text = "Update Profile") },
                        selected = false,
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = { navigateToProfileInitScreen() },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.profile),
                                contentDescription = "Update My Details",
                            )
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Logout") },
                        selected = false,
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            logout()
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_logout),
                                contentDescription = "Logout",
                            )
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun DashBoardTopAppBar(
        photoBgColor: Color,
        title: String,
        openNavDrawer: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(title)
            },
            navigationIcon = {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .padding(horizontal = 20.dp)
                        .clickable(
                            onClick = {
                                openNavDrawer()
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    UserPhotoRoundComposable(color = photoBgColor)
                }
            },
            modifier = modifier
        )
    }

    @Composable
    private fun UserPhotoRoundComposable(
        color: Color,
        size: Int = 40,
    ) {
        if (User.photoUrl != null) {
            AsyncImage(
                model = User.photoUrl,
                contentDescription = "User Image",
                modifier = Modifier
                    .size(size.dp)
                    .clip(CircleShape)
            )
        } else {
            val backgroundColor = remember {
                color
            }
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = User.name?.first()?.uppercase() ?: "X",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = size.sp / 2
                )
            }
        }
    }
}

