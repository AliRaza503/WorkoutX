package com.labz.workoutx.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.labz.workoutx.R
import com.labz.workoutx.models.Workout
import com.labz.workoutx.uistates.ChooseWorkoutUiState
import com.labz.workoutx.viewmodels.ChooseWorkoutViewModel
import kotlinx.serialization.Serializable

@Serializable
object ChooseWorkoutScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ChooseWorkoutScreenComposable(
        navigateToWorkoutPlanningScreen: (workoutId: String) -> Unit,
        modifier: Modifier = Modifier,
        viewModel: ChooseWorkoutViewModel = hiltViewModel()
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        if (uiState.isCircularProgressIndicatorVisible) {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp) // Add spacing between items
                    ) {
                        items(uiState.allWorkouts.keys.toList().size) { idx ->
                            val goal = uiState.allWorkouts.keys.toList()[idx]
                            Text(
                                text = goal.name,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            viewModel.filterWorkoutsListForGoal(goal)
                                        }
                                    )
                                    .padding(8.dp)
                                    .border(
                                        width = 1.dp,
                                        color = if (viewModel.isSelectedGoal(goal)) Color(0xFF7B6F72) else Color.Transparent,
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .padding(4.dp)
                            )
                        }
                    }
                    // Lazy column to display workouts
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp)
                    ) {
                        items(uiState.filteredWorkouts.size) { idx ->
                            WorkoutItem(
                                workout = uiState.filteredWorkouts[idx],
                                onSelected = { workoutId ->
                                    navigateToWorkoutPlanningScreen(workoutId)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun WorkoutItem(
        workout: Workout,
        onSelected: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(workout.imagePath),
                    contentDescription = workout.name,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(
                            RoundedCornerShape(8.dp)
                        ),
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        text = workout.name,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = workout.benefit,
                        fontSize = 12.sp
                    )
                }

                IconButton(
                    onClick = {
                        onSelected(workout.id.toString())
                    },
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFF7B6F72),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.chevron_right),
                        contentDescription = "See the workout details",
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun WorkoutFilterChips(uiState: ChooseWorkoutUiState, viewModel: ChooseWorkoutViewModel) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(4.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            uiState.allWorkouts.keys.forEach { goal ->
//                FilterChip(
//                    selected = uiState.selectedGoal == goal,
//                    onClick = { viewModel.filterWorkoutsBasedOnUserGoal(goal) },
//                    label = { Text(text = goal.name, fontSize = 8.sp) },
//                    modifier = Modifier.padding(2.dp)
//                )
//            }
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChooseWorkoutScreenPreview() {
    ChooseWorkoutScreen.ChooseWorkoutScreenComposable(
        navigateToWorkoutPlanningScreen = {}
    )
}