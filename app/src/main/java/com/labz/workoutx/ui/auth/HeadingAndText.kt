package com.labz.workoutx.ui.auth

import android.R.id.text1
import android.R.id.text2
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeadingAndText(heading: String, text: String) {
    Text(
        text = heading,
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
    )
    Text(
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)) {
                append(text)
            }
        }
    )
}