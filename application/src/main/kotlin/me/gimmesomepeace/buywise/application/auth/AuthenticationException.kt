package me.gimmesomepeace.buywise.application.auth

sealed class AuthenticationException(
    message: String,
) : RuntimeException(message) {
    class InvalidCredentials :
        AuthenticationException(
            "Invalid login credentials",
        )
}
