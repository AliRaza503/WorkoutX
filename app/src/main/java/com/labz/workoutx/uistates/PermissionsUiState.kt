package com.labz.workoutx.uistates

import java.util.UUID

sealed class PermissionsUiState {
    object Uninitialized : PermissionsUiState()
    object Granted : PermissionsUiState()

    // A random UUID is used in each Error object to allow errors to be uniquely identified,
    // and recomposition won't result in multiple snackbars.
    data class Error(val exception: Throwable, val uuid: UUID = UUID.randomUUID()) : PermissionsUiState()
}
