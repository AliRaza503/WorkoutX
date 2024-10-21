package com.labz.workoutx.ui.exts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun VerticalProgress(
    day: String = "Sun",
    progressPercentage: Float,
    modifier: Modifier = Modifier
) {
    var prTemp = progressPercentage
    if (progressPercentage <= 0) {
        prTemp = 0.001f
    } else if (progressPercentage >= 100) {
        prTemp = 100f
    }
    val mProgress = animateFloatAsState(targetValue = prTemp / 100, label = "Progress")
    Column {
        Text(text = day)
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
                .width(16.dp)
                .heightIn(min = 200.dp, max = 250.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .weight(
                        if ((1 - mProgress.value) == 0f) {
                            0.0001f
                        } else {
                            1 - mProgress.value
                        }
                    )
                    .fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .weight(mProgress.value)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xffE000FF),
                                Color(0xffE000FF),
                                Color(0xFF7700FF),
                                Color(0xFF7700FF),
                            )
                        )
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressBarPreview() {
    VerticalProgress(progressPercentage = 50f)
}