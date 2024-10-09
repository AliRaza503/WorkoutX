package com.labz.workoutx.ui.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.labz.workoutx.R
import com.labz.workoutx.exts.PastOrPresentSelectableDates
import com.labz.workoutx.models.Gender
import com.labz.workoutx.models.Goal
import com.labz.workoutx.ui.auth.HeadingAndText
import com.labz.workoutx.ui.exts.ClickableTextInput
import com.labz.workoutx.ui.exts.GradientButton
import com.labz.workoutx.viewmodels.ProfileInitViewModel
import kotlinx.serialization.Serializable

@Serializable
object ProfileInit {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ProfileInitComposable(
        viewModel: ProfileInitViewModel = hiltViewModel()
    ) {
        val uiState by viewModel.uiState
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 80.dp)
                .verticalScroll(rememberScrollState())
        ) {
            HeadingAndText(
                text = "Let's complete your profile",
                heading = "Profile initialization will help us to personalize your experience"
            )
            val focusRequester = remember { FocusRequester() }
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
                        .focusRequester(focusRequester = focusRequester)
                        .clickable(
                            onClick = {
                                focusRequester.requestFocus()
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
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
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
                placeholder = "Enter your weight",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.weight),
                        contentDescription = "First Name",
                    )
                },
                errorText = uiState.weightError,
                numericKeyboard = true
            )

            ClickableTextInput(
                value = uiState.heightInCms?.toString() ?: "",
                onValueChange = viewModel::onHeightChanged,
                label = "Height",
                placeholder = "Enter your height",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.swap),
                        contentDescription = "First Name",
                    )
                },
                errorText = uiState.heightError,
                numericKeyboard = true
            )

            ExposedDropdownMenuBox(
                expanded = uiState.genderDropDownOpened,
                onExpandedChange = { viewModel::genderDropDownChanged },
            ) {
                ClickableTextInput(
                    value = uiState.goal?.toString() ?: "",
                    onValueChange = viewModel::onGoalChanged,
                    label = "Goal",
                    placeholder = "Choose Goal",
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.goal),
                            contentDescription = "First Name",
                        )
                    },
                    modifier = Modifier
                        .menuAnchor(
                            type = MenuAnchorType.PrimaryNotEditable
                        )
                        .focusRequester(focusRequester = focusRequester)
                        .clickable(
                            onClick = {
                                focusRequester.requestFocus()
                            }
                        ),
                    onTextFieldClicked = {
                        viewModel.goalDropDownChanged(!uiState.genderDropDownOpened)
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
                    expanded = uiState.goalDropDownOpened,
                    onDismissRequest = { viewModel.goalDropDownChanged(false) },
                    modifier = Modifier.border(
                        width = 0.2.dp,
                        color = Color(0xFF7B6F72),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                    )
                ) {
                    Goal.entries.forEach { goal ->
                        DropdownMenuItem(
                            text = { Text(goal.toString()) },
                            onClick = {
                                viewModel.onGoalChanged(goal.toString())
                                viewModel.goalDropDownChanged(false)
                            }
                        )
                    }
                }
            }

            GradientButton(
                text = "Next",
                onClick = {
                    // TODO: Navigate to the home screen
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
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


@Preview(showBackground = true)
@Composable
fun ProfileInitScreenPreview() {
    ProfileInit.ProfileInitComposable()
}