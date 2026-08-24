package me.gimmesomepeace.buywise.app.user

import me.gimmesomepeace.buywise.app.BeanNames
import me.gimmesomepeace.buywise.application.shared.IdGenerator
import me.gimmesomepeace.buywise.application.user.UserQuery
import me.gimmesomepeace.buywise.application.user.list.ListUsersUseCase
import me.gimmesomepeace.buywise.application.user.reg.RegisterUserUseCase
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHasher
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.UserRepository
import me.gimmesomepeace.buywise.infrastructure.persistence.user.UserJpaRepository
import me.gimmesomepeace.buywise.infrastructure.persistence.user.UserQueryImpl
import me.gimmesomepeace.buywise.infrastructure.persistence.user.UserRepositoryImpl
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserConfiguration {
    @Bean
    fun userRepository(
        userJpaRepository: UserJpaRepository,
    ): UserRepository = UserRepositoryImpl(userJpaRepository)

    @Bean
    fun userQuery(
        userRepository: UserJpaRepository,
    ): UserQuery = UserQueryImpl(userRepository)

    @Bean
    fun registerUserUseCase(
        @Qualifier(BeanNames.USER_ID_GENERATOR)
        idGenerator: IdGenerator<UserId>,
        repository: UserRepository,
        passwordHasher: PasswordHasher,
    ): RegisterUserUseCase = RegisterUserUseCase(idGenerator, repository, passwordHasher)

    @Bean
    fun listUsersUseCase(
        query: UserQuery,
    ) : ListUsersUseCase = ListUsersUseCase(query)
}
