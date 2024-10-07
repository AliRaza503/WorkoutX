package com.labz.workoutx.ui.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.labz.workoutx.ui.auth.LoginScreen.LoginScreenComposable
import com.labz.workoutx.ui.exts.CenteredText
import com.labz.workoutx.ui.exts.GradientButton
import com.labz.workoutx.ui.exts.TextInput
import com.labz.workoutx.viewmodels.LoginViewModel
import kotlinx.serialization.Serializable

@Serializable
object LoginScreen {
    @Composable
    fun LoginScreenComposable(
        onLoginSuccess: () -> Unit,
        onSignupClicked: () -> Unit,
        viewModel: LoginViewModel = hiltViewModel()
    ) {
        val uiState by viewModel.uiState
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 80.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TopAuthScreenTexts(
                text1 = "Welcome back,",
                text2 = "Login to your account"
            )
            TextInput(
                value = uiState.email,
                onValueChange = viewModel::onEmailChanged,
                label = "Email",
                placeholder = "Enter your email",
                errorText = uiState.emailError
            )

            TextInput(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChanged,
                label = "Password",
                placeholder = "Enter your password",
                protectedField = true,
                isPasswordVisible = uiState.isPasswordVisible,
                invertVisibility = viewModel::togglePasswordVisibility,
                errorText = uiState.passwordError
            )

            CenteredText(
                text = "Forgot your password? ",
                btnText = "Reset",
                btnOnClick = {
                    if (viewModel.isEmailEntered()) {
                        viewModel.sendPasswordResetEmail()
                    }
                }
            )

            if (uiState.isLoadingAcc) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            GradientButton(
                text = "Login",
                onClick = {
                    if (!viewModel.hasErrors()) {
                        viewModel.authenticate()
                    }
                },
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 20.dp, start = 40.dp, end = 40.dp)
                    .fillMaxWidth()
            )

            OAuthBtns(
                modifier = Modifier.align(CenterHorizontally),
                onGoogleClick = { viewModel::signInWithGoogle }
            )

            CenteredText(
                text = "Don't have an account? ",
                btnText = "Sign Up",
                btnOnClick = { onSignupClicked() }
            )
            val context = LocalContext.current
            LaunchedEffect(key1 = uiState.showToast) {
                if (uiState.showToast && uiState.toastMessage != null) {
                    Toast.makeText(context, uiState.toastMessage, Toast.LENGTH_SHORT).show()
                    viewModel.disableToast()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenComposable(onLoginSuccess = {}, onSignupClicked = {})
}