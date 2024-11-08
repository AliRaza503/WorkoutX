package com.labz.workoutx.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.labz.workoutx.ui.exts.GradientButton
import com.labz.workoutx.viewmodels.WorkoutPlanningViewModel
import kotlinx.serialization.Serializable

@Serializable
object WorkoutPlanningScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun WorkoutPlanningScreenComposable(
        startWorkout: (workoutID: String) -> Unit,
        viewModel: WorkoutPlanningViewModel = hiltViewModel(),
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(uiState.workout?.name ?: "Workout Planning")
                    }
                )
            }
        ) { innerPadding ->
            // Check if workout is non-null to proceed
            if (uiState.isCircularProgressIndicatorVisible) {
                // Display a loading or empty state if no workout is available
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Set up a full-screen Box with a background color or optional image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background // Optional background color
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp) // Padding around the content
                                .verticalScroll(rememberScrollState()), // Enable vertical scrolling
                            verticalArrangement = Arrangement.spacedBy(12.dp), // Spacing between items
                            horizontalAlignment = Alignment.Start
                        ) {
                            // Workout Image
                            AsyncImage(
                                model = uiState.workout!!.imagePath,
                                contentDescription = "Workout Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.FillBounds // Fill the bounds of the image
                            )

                            // Workout Description
                            Text(
                                text = uiState.workout!!.description,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            // Target Minutes
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Target Minutes: ",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "${uiState.workout!!.targetMinutes}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Text(
                                text = "Benefits: ",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = uiState.workout!!.benefit,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )

                            // Steps Title
                            Text(
                                text = "Steps to Perform:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // Steps List
                            Column(
                                verticalArrangement = Arrangement.spacedBy(5.dp) // Space between steps
                            ) {
                                uiState.workout!!.steps.forEachIndexed { index, step ->
                                    Text(
                                        text = "${index + 1}. $step",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(start = 8.dp) // Indent steps
                                    )
                                }
                            }

                            GradientButton(
                                text = "Start Workout",
                                onClick = {
                                    startWorkout(uiState.workout!!.id.toString())
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp) // Button width
                            )
                        }
                    }
                }
            }
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun WorkoutPlanningScreenPreview() {
//    WorkoutPlanningScreen.WorkoutPlanningScreenComposable({})
//}