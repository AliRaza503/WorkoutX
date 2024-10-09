package com.labz.workoutx.ui.exts

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.labz.workoutx.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickableTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    protectedField: Boolean = false,
    isPasswordVisible: Boolean = false,
    leadingIcon: @Composable (() -> Unit),
    errorText: String? = null,
    onTextFieldClicked: () -> Unit = {},
    modifier: Modifier = Modifier,
    readonly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    numericKeyboard: Boolean = false
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
            modifier = modifier.fillMaxWidth(),
            singleLine = true,
            readOnly = readonly,
            visualTransformation = if (protectedField && !isPasswordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = if (numericKeyboard) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
            trailingIcon = trailingIcon,
            isError = isError,
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                onTextFieldClicked()
                            }
                        }
                    }
                }
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
        ClickableTextInput(
            value = "",
            onValueChange = {},
            label = "Email",
            placeholder = "Enter your email",
            protectedField = false,
            isPasswordVisible = false,
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