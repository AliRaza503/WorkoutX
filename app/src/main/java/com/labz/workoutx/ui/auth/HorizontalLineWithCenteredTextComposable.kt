package com.labz.workoutx.ui.auth

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalLineWithCenteredText(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp) // Adjust spacing as needed
        )
        Text(text = text)
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp) // Adjust spacing as needed
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HorizontalLineWithCenteredTextPreview() {
    HorizontalLineWithCenteredText(text = "Or")
}