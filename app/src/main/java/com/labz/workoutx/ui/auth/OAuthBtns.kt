package com.labz.workoutx.ui.auth

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.labz.workoutx.R

@Composable
fun OAuthBtns(modifier: Modifier = Modifier, onGoogleClick: () -> Unit) {
    HorizontalLineWithCenteredText(text = "Or")
    Row(
        modifier = modifier
            .padding(10.dp)
    ) {
        OutlinedCard(modifier = Modifier.padding(10.dp)) {
            IconButton(
                onClick = {

                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                    tint = Color.Unspecified
                )
            }
        }

        OutlinedCard(modifier = Modifier.padding(10.dp)) {
            IconButton(
                onClick = {
                    //TODO: Handle Facebook login
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_facebook),
                    contentDescription = "Google",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Preview
@Composable
fun OAuthBtnsPreview() {
    OAuthBtns(onGoogleClick = {})
}