package com.labz.workoutx.throwables


sealed class LoginException : Throwable() {
    object InvalidCredentials : LoginException() {
        private fun readResolve(): Any = InvalidCredentials
        override fun getLocalizedMessage(): String {
            return "Email or password is incorrect"
        }
    }

    data class UnknownException(val throwable: Throwable) : LoginException()

}