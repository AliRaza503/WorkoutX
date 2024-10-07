package com.labz.workoutx.services.auth

import android.content.Context
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountService {
    fun createAnonymousAccount(onResult: (Throwable?) -> Unit)
    suspend fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit)
    suspend fun createAccountWithEmailPassword(
        email: String,
        password: String,
        userName: String,
        onResult: (Throwable?) -> Unit
    )

    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun sendPasswordResetEmail(emailAddress: String)
    suspend fun signInWithGoogle(context: Context): Flow<Result<AuthResult>>
}