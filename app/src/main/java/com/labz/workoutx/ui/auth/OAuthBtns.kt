package com.labz.workoutx.ui.auth

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.labz.workoutx.R

@Composable
fun OAuthBtns(onGoogleClick: (context: Context) -> Unit) {
    HorizontalLineWithCenteredText(text = "Or")
    val isDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current as Activity
    val onClick = { onGoogleClick(context) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize().padding(20.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = when (isDarkTheme) {
                true -> Color(0xFF131314)
                false -> Color(0xFFFFFFFF)
            },
            modifier = Modifier
                .height(50.dp)
                .width(260.dp)
                .clip(CircleShape)
                .border(
                    BorderStroke(
                        width = 1.dp,
                        color = Color(0xFF8E918F)
                    ),
                    shape = CircleShape
                )
                .clickable { onClick() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 5.dp)
            ) {
                Spacer(modifier = Modifier.width(14.dp))
                Image(
                    painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Continue with Google")
                Spacer(modifier = Modifier.weight(1f))
            }
        }

    }

}

@Preview
@Composable
fun OAuthBtnsPreview() {
    OAuthBtns(onGoogleClick = {})
}