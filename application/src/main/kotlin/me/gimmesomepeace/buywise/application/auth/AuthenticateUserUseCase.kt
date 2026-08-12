package me.gimmesomepeace.buywise.application.auth

import me.gimmesomepeace.buywise.application.user.UserQuery
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHasher
import me.gimmesomepeace.buywise.domain.user.Login

class AuthenticateUserUseCase(
    private val query: UserQuery,
    private val accessTokenGenerator: AccessTokenGenerator,
    private val passwordHasher: PasswordHasher,
) {
    suspend fun execute(
        login: Login,
        password: String
    ) : AccessToken {
        val user = query.findByLogin(login) ?: throw AuthenticationException.InvalidCredentials()
        if (!passwordHasher.matches(password, user.passwordHash))
            throw AuthenticationException.InvalidCredentials()

        return accessTokenGenerator.generate(
            userId = user.id,
            role = user.role
        )
    }
}
