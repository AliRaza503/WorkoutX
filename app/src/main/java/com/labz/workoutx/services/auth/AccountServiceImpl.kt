package com.labz.workoutx.services.auth


import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.labz.workoutx.throwables.LoginException
import com.labz.workoutx.throwables.SignupExceptions
import kotlinx.coroutines.tasks.await


class AccountServiceImpl : AccountService {
    override fun createAnonymousAccount(onResult: (Throwable?) -> Unit) {
        Firebase.auth.signInAnonymously()
            .addOnCompleteListener { onResult(it.exception) }
    }

    override suspend fun authenticate(
        email: String,
        password: String,
        onResult: (Throwable?) -> Unit
    ) {
        try {
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
            onResult(null) // Success
        } catch (e: Exception) {
            val authError = when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    // Invalid password or email format
                    // You can handle this error specifically, e.g., show a message to the user
                    LoginException.InvalidCredentials
                }

                else -> {
                    // Other authentication errors
                    LoginException.UnknownException(e)
                }
            }
            onResult(authError) // Error
        }
    }

    override suspend fun createAccountWithEmailPassword(
        email: String,
        password: String,
        name: String,
        onResult: (Throwable?) -> Unit
    ) {
        try {
            val auth = Firebase.auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            user?.updateProfile(profileUpdates)?.await()

            onResult(null) // Success
        } catch (e: Exception) {
            val accCreationError = when (e) {
                is FirebaseAuthEmailException -> {
                    SignupExceptions.EmailAlreadyExistingException
                }

                else -> {
                    SignupExceptions.UnknownException(e)
                }
            }
            onResult(accCreationError) // Error
        }
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    override suspend fun sendPasswordResetEmail(emailAddress: String) {
        Firebase.auth.sendPasswordResetEmail(emailAddress).await()
    }

    override suspend fun signInWithGoogle(idToken: String): AuthResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val auth = Firebase.auth
        return auth.signInWithCredential(credential).await()
    }
}