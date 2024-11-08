package com.labz.workoutx.ui.exts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.labz.workoutx.R

@Composable
fun GradientButton(
    text: String,
    enabled: Boolean = true,
    gradient: Brush = Brush.horizontalGradient(
        colors = if (enabled) {
            listOf(
                colorResource(id = R.color.gradient_start),
                colorResource(id = R.color.gradient_end)
            )
        } else {
            listOf(
                colorResource(id = R.color.disabled_end).copy(alpha = 0.8f),
                colorResource(id = R.color.disabled_end).copy(alpha = 0.8f),
            )
        }
    ),
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Black
        ),
        contentPadding = PaddingValues(),
        onClick = { onClick() },
        enabled = enabled,
        modifier = modifier
            .heightIn(min = 48.dp, max = 64.dp)
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GradientButtonPreview() {
    GradientButton(text = "Sign Up", onClick = {  }, modifier = Modifier.fillMaxWidth(), enabled = false)
}