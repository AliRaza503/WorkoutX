package com.labz.workoutx.ui.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.labz.workoutx.R
import com.labz.workoutx.ui.auth.SignupScreen.SignupScreenComposable
import com.labz.workoutx.ui.exts.CenteredText
import com.labz.workoutx.ui.exts.GradientButton
import com.labz.workoutx.ui.exts.TextInput
import com.labz.workoutx.viewmodels.SignupViewModel
import kotlinx.serialization.Serializable

@Serializable
object SignupScreen {
    @Composable
    fun SignupScreenComposable(
        onSignupSuccess: () -> Unit,
        onLoginClicked: () -> Unit,
        viewModel: SignupViewModel = hiltViewModel()
    ) {
        val uiState by viewModel.uiState

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 50.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TopAuthScreenTexts(
                text1 = "Hey there,",
                text2 = "Create an Account"
            )

            TextInput(
                value = uiState.firstName,
                onValueChange = viewModel::onFirstNameChanged,
                label = "First Name",
                placeholder = "Enter your first name",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "First Name",
                    )
                },
                errorText = uiState.firstNameError,
            )
            TextInput(
                value = uiState.lastName,
                onValueChange = viewModel::onLastNameChanged,
                label = "Last Name",
                placeholder = "Enter your last name",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Last Name",
                    )
                },
                errorText = uiState.lastNameError,
            )
            TextInput(
                value = uiState.email,
                onValueChange = viewModel::onEmailChanged,
                label = "Email",
                placeholder = "Enter your email",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.message),
                        contentDescription = "Email",
                    )
                },
                errorText = uiState.emailError
            )
            TextInput(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChanged,
                label = "Password",
                placeholder = "Enter your password",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.lock),
                        contentDescription = "Password",
                    )
                },
                protectedField = true,
                isPasswordVisible = uiState.isPasswordVisible,
                invertVisibility = viewModel::togglePasswordVisibility,
                errorText = uiState.passwordError
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
                text = "Sign Up",
                onClick = {
                    if (!viewModel.hasErrors()) {
                        viewModel.createAccount()
                    }
                },
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 20.dp, start = 40.dp, end = 40.dp)
                    .fillMaxWidth()
            )

            OAuthBtns(
                onGoogleClick = { context -> viewModel.signInWithGoogle(context) }
            )

            CenteredText(
                text = "Already have an account? ",
                btnText = "Login",
                btnOnClick = {
                    onLoginClicked()
                }
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
fun SignupScreenPreview() {
    SignupScreenComposable(
        onSignupSuccess = {},
        onLoginClicked = {}
    )
}