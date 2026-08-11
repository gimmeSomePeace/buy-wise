package me.gimmesomepeace.buywise.application.user.reg

import me.gimmesomepeace.buywise.application.shared.IdGenerator
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHasher
import me.gimmesomepeace.buywise.domain.user.*

class RegisterUserUseCase(
    private val idGenerator: IdGenerator<UserId>,
    private val repository: UserRepository,
    private val passwordHasher: PasswordHasher
) {
    suspend fun execute(
        login: Login,
        password: String
    ) : User {
        val user = User(
            id = idGenerator.generate(),
            login = login,
            passwordHash = passwordHasher.hash(password),
            role = UserRole.USER
        )

        repository.add(user)
        return user
    }
}
