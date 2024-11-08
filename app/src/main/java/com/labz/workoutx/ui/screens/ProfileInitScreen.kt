package com.labz.workoutx.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.labz.workoutx.R
import com.labz.workoutx.exts.PastOrPresentSelectableDates
import com.labz.workoutx.models.Gender
import com.labz.workoutx.ui.auth.HeadingAndText
import com.labz.workoutx.ui.exts.ClickableTextInput
import com.labz.workoutx.ui.exts.GradientButton
import com.labz.workoutx.viewmodels.ProfileInitViewModel
import kotlinx.serialization.Serializable

@Serializable
object ProfileInitScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ProfileInitComposable(
        navigateToDashboard: () -> Unit = {},
        viewModel: ProfileInitViewModel = hiltViewModel()
    ) {
        val navigateToDashboard by viewModel.navigateToDashboard.collectAsStateWithLifecycle()
        // Trigger navigation when the state changes
        LaunchedEffect(navigateToDashboard) {
            if (navigateToDashboard) {
                navigateToDashboard()
                viewModel.onNavigated() // Reset state after navigation
            }
        }
        val uiState by viewModel.uiState
        viewModel.tryInitializeProfile()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()  // Add padding for keyboard
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_init),
                contentDescription = "Profile Initialization",
                modifier = Modifier
                    .heightIn(min = 150.dp, max = 200.dp)
                    .padding(bottom = 20.dp)
                    .align(Alignment.CenterHorizontally),
            )
            HeadingAndText(
                text = "Let's complete your profile",
                heading = "Profile initialization will help us to personalize your experience"
            )
            val focusRequesterForGender = remember { FocusRequester() }
            ExposedDropdownMenuBox(
                expanded = uiState.genderDropDownOpened,
                onExpandedChange = { viewModel::genderDropDownChanged },
            ) {
                ClickableTextInput(
                    value = uiState.gender?.toString() ?: "",
                    onValueChange = viewModel::onGenderChanged,
                    label = "Gender",
                    placeholder = "Choose Gender",
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.user2),
                            contentDescription = "Choose your gender",
                        )
                    },
                    modifier = Modifier
                        .menuAnchor(
                            type = MenuAnchorType.PrimaryNotEditable
                        )
                        .focusRequester(focusRequester = focusRequesterForGender)
                        .clickable(
                            onClick = {
                                focusRequesterForGender.requestFocus()
                            }
                        ),
                    onTextFieldClicked = {
                        viewModel.genderDropDownChanged(true)
                    },
                    readonly = true,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_down),
                            contentDescription = null
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = uiState.genderDropDownOpened,
                    onDismissRequest = { viewModel.genderDropDownChanged(false) },
                    modifier = Modifier.border(
                        width = 0.2.dp,
                        color = Color(0xFF7B6F72),
                        shape = RoundedCornerShape(4.dp)
                    )
                ) {
                    Gender.entries.forEach { gender ->
                        DropdownMenuItem(
                            text = { Text(gender.toString()) },
                            onClick = {
                                viewModel.onGenderChanged(gender.toString())
                                viewModel.genderDropDownChanged(false)
                            }
                        )
                    }
                }
            }

            ClickableTextInput(
                value = uiState.dateOfBirth?.toString() ?: "",
                onValueChange = viewModel::onDateOfBirthChanged,
                label = "Date of birth",
                placeholder = "Enter your date of birth",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "First Name",
                    )
                },
                onTextFieldClicked = viewModel::openDatePickerForResult,
                readonly = true
            )

            ClickableTextInput(
                value = uiState.weightInKgs?.toString() ?: "",
                onValueChange = viewModel::onWeightChanged,
                label = "Weight",
                placeholder = "Enter your weight in kgs",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.weight),
                        contentDescription = "Weight Text input",
                    )
                },
                errorText = uiState.weightError,
                numericKeyboard = true
            )

            ClickableTextInput(
                value = uiState.heightInCms?.toString() ?: "",
                onValueChange = viewModel::onHeightChanged,
                label = "Height",
                placeholder = "Enter your height in cms",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.swap),
                        contentDescription = "Height Text input",
                    )
                },
                errorText = uiState.heightError,
                numericKeyboard = true
            )

            ClickableTextInput(
                value = uiState.minutesActivePerDay?.toString() ?: "",
                onValueChange = viewModel::onMinutesActivePerDayChanged,
                label = "Minutes of exercise per day",
                placeholder = "Enter minutes of exercise per day",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.timer),
                        contentDescription = "Minutes of exercise per day Text input",
                    )
                },
                errorText = uiState.minutesError,
                numericKeyboard = true
            )
            GradientButton(
                text = "Next",
                onClick = {
                    viewModel.nextBtnClicked()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .windowInsetsPadding(WindowInsets.ime),
            )
        }

        if (uiState.datePickerOpened) {
            val dateState = rememberDatePickerState(
                selectableDates = PastOrPresentSelectableDates
            )
            DatePickerDialog(
                onDismissRequest = { viewModel.closeDatePicker() },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.onDateOfBirthChanged(dateState.selectedDateMillis)
                            viewModel.closeDatePicker()
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.closeDatePicker()
                        }
                    ) {
                        Text("Cancel")
                    }
                },
            ) {
                DatePicker(
                    state = dateState,
                    showModeToggle = true,
                )
            }
        }
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun ProfileInitScreenPreview() {
//    ProfileInitScreen.ProfileInitComposable()
//}