package com.labz.workoutx.ui.exts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.labz.workoutx.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    protectedField: Boolean = false,
    isPasswordVisible: Boolean = false,
    invertVisibility: () -> Unit = {},
    leadingIcon: @Composable (() -> Unit)? = null,
    errorText: String? = null,
) {
    val isError = errorText != null && errorText.isNotEmpty()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 0.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon,
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (protectedField && !isPasswordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = {
                if (protectedField) {
                    IconButton(
                        onClick = {
                            invertVisibility()
                        }
                    ) {
                        if (isPasswordVisible) {
                            Icon(
                                painter = painterResource(id = R.drawable.eye),
                                contentDescription = "Hide Password",
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.not_eye),
                                contentDescription = "Show Password",
                            )
                        }
                    }
                }
            },
            isError = isError,
        )
    }
    if (isError) {
        Text(
            text = errorText,
            color = Color(0xFFCC7979),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}


@Preview
@Composable
fun TextInputPreview() {
    MaterialTheme {
        TextInput(
            value = "",
            onValueChange = {},
            label = "Email",
            placeholder = "Enter your email",
            protectedField = false,
            isPasswordVisible = false,
            invertVisibility = {},
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.message),
                    contentDescription = "Email",
                )
            },
            errorText = "Password must contain at least one uppercase letter"
        )
    }
}