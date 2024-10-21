package com.labz.workoutx.ui.exts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.labz.workoutx.R


// TODO: Make use of a viewModel and uiStates
@Composable
fun ProgressesComposable() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Row for week dates
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.chevron_left),
                    contentDescription = "Load Previous Week"
                )
            }

            // TODO: Make the dates dynamic
            Text(
                text = "DATE_FROM - DATE_TO",
                modifier = Modifier.padding(8.dp),
            )

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.chevron_right),
                    contentDescription = "Load Next Week"
                )
            }
        }
        val weekDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        // TODO: Make the progresses dynamic
        val progresses = listOf(100f, 20f, 50f, 35f, 50f, 13f, 70f)
        // Middle Row for the progress bars
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            weekDays.forEachIndexed { index, day ->
                VerticalProgress(
                    day = day,
                    progressPercentage = progresses[index],
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        // Bottom Row for the tabs for progress types
        val tabTitles = listOf("Steps", "Calories", "Food", "Sleep")
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth(),
            // Customizing the indicator (underline) for the selected tab
            indicator = { tabPositions ->
                TabRowDefaults.PrimaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier.padding(8.dp),
                ) {
                    // Wrap the Text in a Box to apply the border and background for the selected tab
                    Box(
                        modifier = Modifier
                            .border(0.5.dp, Color(0xFF7B6F72), shape = RoundedCornerShape(8.dp))
                            .background(
                                if (selectedTabIndex == index) Color.LightGray.copy(alpha = 0.3f)
                                else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp) // Padding inside the boundary
                    ) {
                        Text(text = title)
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