package com.labz.workoutx.throwables

sealed class SignupExceptions : Throwable() {
    object EmailAlreadyExistingException : SignupExceptions() {
        private fun readResolve(): Any = EmailAlreadyExistingException
        override fun getLocalizedMessage(): String {
            return "Email address already exists"
        }
    }
    data class UnknownException(val throwable: Throwable) : SignupExceptions()
}