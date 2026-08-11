package me.gimmesomepeace.buywise.application.user.auth

sealed class AuthenticationException(
    message: String,
) : RuntimeException(message) {
    class InvalidCredentials: AuthenticationException("Invalid login credentials")
}
