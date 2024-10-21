package com.labz.workoutx.services.auth

import android.content.Context
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.labz.workoutx.services.db.DBService
import kotlinx.coroutines.flow.Flow

interface AccountService {
    suspend fun authenticate(email: String, password: String, dbServiceRef: DBService, onResult: (Throwable?) -> Unit)
    suspend fun createAccountWithEmailPassword(
        email: String,
        password: String,
        userName: String,
        onResult: (Throwable?) -> Unit
    )

    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun sendPasswordResetEmail(emailAddress: String)
    suspend fun signInWithGoogle(context: Context, dbServiceRef: DBService): Flow<Result<AuthResult>>
    suspend fun signOut(onResult: (Throwable?) -> Unit)
    fun isUserLoggedIn(): Boolean
}