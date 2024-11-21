package com.labz.workoutx.ui.screens

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.labz.workoutx.R
import com.labz.workoutx.viewmodels.PerformWorkoutViewModel
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class PerformWorkoutScreen(
    val workoutID: String
) {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PerformWorkoutComposable(
        viewModel: PerformWorkoutViewModel = hiltViewModel(),
        onWorkoutCancelled: () -> Unit,
        onWorkoutFinished: () -> Unit
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        Log.d("PerformWorkoutScreen", "PerformWorkoutComposable: workoutID: $workoutID")
        if (!uiState.isWorkoutLoaded) {
            viewModel.loadWorkout(workoutID)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    // Navigate back
                                    onWorkoutCancelled()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_back),
                                    contentDescription = "Close Workout",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        title = {
                            Text(uiState.workout?.name ?: "Workout")
                        },
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Display the initial countdown (3, 2, 1, Go)
                    uiState.countdownValue?.let { countdownValue ->
                        FullScreenCountdown(countdownValue)
                    }

                    // Display the workout countdown timer
                    if (uiState.countdownValue == null && uiState.remainingTime > 0) {
                        CountdownTimer(
                            uiState.workout?.targetMinutes?.toLong()?.times(60_000) ?: 0,
                            uiState.remainingTime,
                            onTimerFinish = {
                                viewModel.workoutCompleted()
                                onWorkoutFinished()
                            },
                            isRunning = uiState.isTimerRunning,
                            onStartStopClick = {
                                viewModel.onPlayPauseClicked()
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun FullScreenCountdown(countdownValue: Int) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (countdownValue == 0) "Go!" else countdownValue.toString(),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    @Composable
    fun CountdownTimer(
        totalTime: Long,
        remainingTime: Long,
        onTimerFinish: () -> Unit,
        isRunning: Boolean = true, // Add a boolean flag for timer state
        onStartStopClick: (Boolean) -> Unit, // Callback for start/stop action
    ) {
        LaunchedEffect(key1 = remainingTime, key2 = isRunning) {
            Log.d(
                "PerformWorkoutScreen",
                "CountdownTimer: remainingTime: $remainingTime, isRunning: $isRunning"
            )
            if (remainingTime <= 1000L) {
                onTimerFinish()
            }
        }
        val animatedProgress by animateFloatAsState(
            targetValue = remainingTime.toFloat() / totalTime.toFloat(),
            animationSpec = tween(durationMillis = 1000),
            label = ""
        )
        val animatedTextColor by animateColorAsState(
            targetValue = if (remainingTime < totalTime / 5) Color.Red else MaterialTheme.colorScheme.onBackground,
            animationSpec = tween(durationMillis = 500), label = ""
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Circular Progress Indicator with color change on low time
                CircularProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier.sizeIn(
                        minWidth = 230.dp,
                        minHeight = 230.dp,
                        maxWidth = 300.dp,
                        maxHeight = 300.dp
                    ),
                    color = animatedTextColor,
                    strokeWidth = 8.dp,
                    strokeCap = StrokeCap.Round
                )

                // Text with size animation and color change
                val minutes = (remainingTime / 1000) / 60
                val seconds = (remainingTime / 1000) % 60
                Text(
                    text = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        minutes,
                        seconds
                    ), // Format as MM:SS
                    color = animatedTextColor,
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Start/Stop Button
            IconButton(
                onClick = {
                    isRunning.not() // Toggle isRunning on button click
                    onStartStopClick(isRunning) // Call callback with updated state
                },
                modifier = Modifier
                    .padding(top = 48.dp)
                    .widthIn(min = 48.dp, max = 64.dp)
            ) {
                Icon(
                    painter = painterResource(id = if (isRunning) R.drawable.ic_pause else R.drawable.ic_play),
                    contentDescription = "Start/Stop Timer",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

