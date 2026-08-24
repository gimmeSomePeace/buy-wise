package me.gimmesomepeace.buywise.application.user.reg

import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHash
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHasher
import me.gimmesomepeace.buywise.domain.user.User
import me.gimmesomepeace.buywise.domain.user.UserException
import me.gimmesomepeace.buywise.domain.user.UserRepository
import me.gimmesomepeace.buywise.domain.user.login
import me.gimmesomepeace.buywise.domain.user.user
import me.gimmesomepeace.buywise.domain.user.userId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class RegisterUserUseCaseTest {
    val repository = mockk<UserRepository>()
    val passwordHasher = mockk<PasswordHasher>()

    @Test
    fun `should register new user`() =
        runTest {
            val userId = userId()
            val user = user(id = userId)

            val idGenerator = { userId }
            val useCase =
                RegisterUserUseCase(
                    idGenerator = idGenerator,
                    repository = repository,
                    passwordHasher = passwordHasher,
                )

            every { passwordHasher.hash("password") } returns user.passwordHash
            coEvery { repository.add(any()) } just runs

            val result = useCase.execute(user.login, "password")

            assertThat(result)
                .extracting(
                    User::id,
                    User::login,
                    User::passwordHash,
                ).containsExactly(
                    user.id,
                    user.login,
                    user.passwordHash,
                )
        }

    @Test
    fun `should fail when login is busy`() =
        runTest {
            val login = login()
            val idGenerator = ::userId

            val useCase =
                RegisterUserUseCase(
                    idGenerator = idGenerator,
                    repository = repository,
                    passwordHasher = passwordHasher,
                )

            every { passwordHasher.hash(any()) } returns
                PasswordHash("password")
            coEvery { repository.add(any()) } throws
                UserException.LoginBusy(login)

            val ex =
                assertFailsWith<UserException.LoginBusy> {
                    useCase.execute(login, "password")
                }
            assertThat(ex.login).isEqualTo(login)
        }
}
