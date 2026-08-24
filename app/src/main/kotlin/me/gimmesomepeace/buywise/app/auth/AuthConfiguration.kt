package me.gimmesomepeace.buywise.app.auth

import me.gimmesomepeace.buywise.application.auth.AccessTokenGenerator
import me.gimmesomepeace.buywise.application.auth.AuthenticateUserUseCase
import me.gimmesomepeace.buywise.application.user.UserQuery
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHasher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthConfiguration {
    @Bean
    fun authenticateUserUseCase(
        userQuery: UserQuery,
        tokenGenerator: AccessTokenGenerator,
        passwordHasher: PasswordHasher,
    ) = AuthenticateUserUseCase(userQuery, tokenGenerator, passwordHasher)
}
