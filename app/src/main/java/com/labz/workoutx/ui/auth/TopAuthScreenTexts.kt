package com.labz.workoutx.ui.auth

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
fun TopAuthScreenTexts(text1: String, text2: String) {
    Text(
        text = text1,
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    Text(
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)) {
                append(text2)
            }
        }
    )
}