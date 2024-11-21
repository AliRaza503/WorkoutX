package com.labz.workoutx.ui.exts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.labz.workoutx.R
import com.labz.workoutx.exts.ProgressTab
import com.labz.workoutx.viewmodels.ProgressesViewModel


@Composable
fun ProgressesComposable(
    onProgressesLoaded: () -> Unit = {},
    viewModel: ProgressesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.isCircularProgressIndicatorVisible) {
        if (!uiState.isCircularProgressIndicatorVisible) {
            onProgressesLoaded()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        // Show CircularProgressIndicator if data is loading
        if (uiState.isCircularProgressIndicatorVisible) {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp)
            )
            Text("Loading data...", modifier = Modifier.padding(8.dp))
        } else {
            val goal =
                viewModel.getGoal().toString().lowercase().replaceFirstChar { it.uppercase() }

            Text(
                text = "Your Goal is to $goal",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = viewModel.getTarget(selectedTab),
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )

            // Top Row for week dates
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        viewModel.loadPreviousWeekProgress()
                    },
                    modifier = Modifier.padding(8.dp),
                    enabled = uiState.dataIsOfVeryLastWeek.not()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.chevron_left),
                        contentDescription = "Load Previous Week"
                    )
                }

                // Display dynamic date range
                Text(
                    text = uiState.dateRange,
                    modifier = Modifier.padding(8.dp),
                )

                IconButton(
                    onClick = { viewModel.loadNextWeekProgress() },
                    modifier = Modifier.padding(8.dp),
                    enabled = uiState.dataIsOfCurrentWeek.not()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.chevron_right),
                        contentDescription = "Load Next Week"
                    )
                }
            }
            // Progress for the selected tab (Steps, Calories, etc.)
            val weekDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

            // Middle Row for the progress bars
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val progressDataList = when (selectedTab) {
                    ProgressTab.STEPS -> uiState.oneWeekData.map { it?.stepsData ?: 0f }
                    ProgressTab.CALORIES -> uiState.oneWeekData.map { it?.caloriesBurned ?: 0f }
                    ProgressTab.MINUTES -> uiState.oneWeekData.map { it?.minutesActive ?: 0f }
                }
                weekDays.forEachIndexed { index, day ->
                    val progress: Float = (progressDataList.getOrNull(index))?.toFloat() ?: 0f
                    // Find the percentage progress
                    // The user must have walked 10,000 steps or more to make the progress 100%
                    val progressPercentage = viewModel.getProgressPercentage(progress)
                    Log.d("ProgressesComposable", "Progress: $progressDataList")
                    VerticalProgress(
                        day = day,
                        progressPercentage = progressPercentage,
                        modifier = Modifier.padding(8.dp),
                        progressValue = progress
                    )
                }
            }

            // Bottom Row for the tabs for progress types
            val tabTitles =
                ProgressTab.entries.map { it.name.lowercase().replaceFirstChar { it.uppercase() } }
            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                modifier = Modifier.fillMaxWidth(),
                indicator = { tabPositions ->
                    TabRowDefaults.PrimaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal])
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab.ordinal == index,
                        onClick = { viewModel.onTabSelected(index) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .border(0.5.dp, Color(0xFF7B6F72), shape = RoundedCornerShape(8.dp))
                                .background(
                                    if (selectedTab.ordinal == index) Color.LightGray.copy(alpha = 0.3f)
                                    else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Text(text = title)
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProgressesComposablePreview() {
    ProgressesComposable()
}