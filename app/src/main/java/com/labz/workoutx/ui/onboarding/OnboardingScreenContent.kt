package com.labz.workoutx.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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


@Composable
fun OnboardingScreenContent(
    headingText: String = "This is heading",
    bodyText: String = "this is body\non multiple lines",
    drawableId: Int = R.drawable.onboarding_1
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "Onboarding Screen",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillWidth
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(top = 280.dp),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)) {
                    append(headingText)
                }
                append("\n\n")
                append(bodyText)
            },
            textAlign = TextAlign.Start
        )
    }
}