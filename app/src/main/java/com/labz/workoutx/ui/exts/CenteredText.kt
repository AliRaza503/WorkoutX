package com.labz.workoutx.ui.exts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.labz.workoutx.R

@Composable
fun CenteredText(text: String, btnText: String, btnOnClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Gray)) {
            append(text)
        }
        pushStringAnnotation(tag = "btnText", annotation = "btnText")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.gradient_start)
            )
        ) {
            append(btnText)
        }
        pop()
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Disable visual indication
                onClick = btnOnClick
            ),
        text = annotatedText,
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCenteredText() {
    CenteredText(
        text = "UserName: John Doe, User Email: ",
        btnText = "Logout",
        btnOnClick = {}
    )
}