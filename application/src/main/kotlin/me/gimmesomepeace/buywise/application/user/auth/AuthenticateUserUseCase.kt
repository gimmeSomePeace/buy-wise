package me.gimmesomepeace.buywise.application.user.auth

import me.gimmesomepeace.buywise.application.user.UserQuery
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHasher
import me.gimmesomepeace.buywise.domain.user.Login

class AuthenticateUserUseCase(
    private val query: UserQuery,
    private val passwordHasher: PasswordHasher,
) {
    suspend fun execute(
        login: Login,
        password: String
    ) : AuthenticationResult {
        val user = query.findByLogin(login) ?: throw AuthenticationException.InvalidCredentials()
        if (!passwordHasher.matches(password, user.passwordHash))
            throw AuthenticationException.InvalidCredentials()

        return AuthenticationResult(
            userId = user.id,
            role = user.role
        )
    }
}
