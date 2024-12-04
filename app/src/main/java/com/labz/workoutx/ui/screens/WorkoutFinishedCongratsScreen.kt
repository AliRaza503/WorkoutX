package com.labz.workoutx.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.labz.workoutx.R
import com.labz.workoutx.ui.exts.GradientButton
import kotlinx.serialization.Serializable

@Serializable
object WorkoutFinishedCongratsScreen {
    @Composable
    fun WorkoutFinishedCongratsScreenComposable(
        modifier: Modifier = Modifier,
        backToHome: () -> Unit
    ) {
        val headingText = "Congratulations!\nYou have completed your workout"
        val bodyText =
            "Exercises is king and nutrition is queen. Combine the two and you will have a kingdom\n-Jack Lalanne"
        Column(modifier.fillMaxSize().padding(20.dp)) {
            Image(
                painter = painterResource(id = R.drawable.workout_completed),
                contentDescription = "Workout Finished",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                contentScale = ContentScale.FillWidth
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        )
                    ) {
                        append(headingText)
                    }
                    append("\n\n")
                    append(bodyText)
                },
                textAlign = TextAlign.Center
            )
            GradientButton(
                text = "Back to Home",
                onClick = { backToHome() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewWorkoutFinishedCongratsScreen() {
//    WorkoutFinishedCongratsScreenComposable(
//        backToHome = {}
//    )
//}