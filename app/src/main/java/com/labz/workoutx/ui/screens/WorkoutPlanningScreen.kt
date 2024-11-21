package com.labz.workoutx.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.labz.workoutx.R
import com.labz.workoutx.ui.exts.GradientButton
import com.labz.workoutx.uistates.WorkoutPlanningUiState
import com.labz.workoutx.viewmodels.WorkoutPlanningViewModel
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class WorkoutPlanningScreen(
    val workoutId: String
) {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun WorkoutPlanningScreenComposable(
        startWorkout: (workoutID: String) -> Unit,
        viewModel: WorkoutPlanningViewModel = hiltViewModel(),
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        if (uiState.workout == null) {
            viewModel.loadWorkoutBasedOnUserGoal(workoutId)
        }
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
            if (uiState.isCircularProgressIndicatorVisible || uiState.workout == null) {
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
                                    .height(150.dp)
                                    .padding(horizontal = 30.dp)
                                    .clip(
                                        RoundedCornerShape(22.dp)
                                    ),
                                contentScale = ContentScale.FillBounds
                            )

                            // Workout Description
                            Text(
                                text = uiState.workout!!.description,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Steps Title
                                Text(
                                    text = "How To Do It",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = "Steps ${uiState.workout!!.steps.size}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.LightGray,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                            // Steps List
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp) // Space between steps
                            ) {
                                uiState.workout!!.steps.forEachIndexed { index, step ->
                                    NumberedStepComposable(
                                        index = index,
                                        step = step,
                                        isLastStep = index == uiState.workout!!.steps.size - 1
                                    )
                                }
                            }

                            ActivityMinutesComposable(uiState, viewModel)

                            GradientButton(
                                text = "Start Workout",
                                onClick = {
                                    viewModel.setWorkoutMinutes()
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

    @Composable
    private fun NumberedStepComposable(
        index: Int,
        step: String,
        isLastStep: Boolean
    ) {
        Box(
            modifier = Modifier.height(IntrinsicSize.Max)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = String.format(Locale.getDefault(), "%02d", index + 1),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(id = R.color.gradient_start)
                )
                Image(
                    imageVector = if (isLastStep) {
                        ImageVector.vectorResource(id = R.drawable.circle)
                    } else {
                        ImageVector.vectorResource(id = R.drawable.circle_with_line)
                    },
                    contentDescription = "Step Icon"
                )
                val words = step.split(" ", limit = 3)
                val heading = words.take(
                    if (words[1].length > 4) 2 else 1
                ).joinToString(" ")

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .heightIn(min = 100.dp)
                ) {
                    Text(
                        text = heading,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxHeight(0.5f)
                    )
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    @Composable
    private fun ActivityMinutesComposable(
        uiState: WorkoutPlanningUiState,
        viewModel: WorkoutPlanningViewModel
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Activity Minutes: ",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${uiState.activityMinutes}",
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(
                onClick = {
                    viewModel.activityMinutesChanged(uiState.activityMinutes + 1)
                },
                enabled = uiState.activityMinutes < 60,
                content = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_add),
                        contentDescription = "Increase Activity Minutes"
                    )
                },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFF7B6F72),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .size(40.dp)
            )
            IconButton(
                onClick = {
                    viewModel.activityMinutesChanged(uiState.activityMinutes - 1)
                },
                enabled = uiState.activityMinutes > 1,
                content = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_minus),
                        contentDescription = "Decrease Activity Minutes"
                    )
                },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFF7B6F72),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .size(40.dp)
            )
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun WorkoutPlanningScreenPreview() {
//    WorkoutPlanningScreen(WorkoutTypes.workoutTypes.entries.first().value[0].id.toString()).WorkoutPlanningScreenComposable(
//        {})
//}