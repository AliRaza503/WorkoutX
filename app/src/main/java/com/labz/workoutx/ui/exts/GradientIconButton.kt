package com.labz.workoutx.ui.exts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import com.labz.workoutx.R


@Composable
fun GradientIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradient: Brush = Brush.horizontalGradient(
        colors = listOf(
            colorResource(id = R.color.gradient_start),
            colorResource(id = R.color.gradient_end)
        )
    ),
    icon: Painter,
    contentDescription: String,
    iconTint: Color = Color.White
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .clip(CircleShape)
            .background(brush = gradient)
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
            tint = iconTint
        )
    }
}