package me.gimmesomepeace.buywise.application.auth

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.user.UserQuery
import me.gimmesomepeace.buywise.application.user.userDetails
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHasher
import me.gimmesomepeace.buywise.domain.user.login
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlin.test.assertFailsWith

class AuthenticateUserUseCaseTest {
    private val query = mockk<UserQuery>()
    private val passwordHasher = mockk<PasswordHasher>()


    @Test
    fun `should authenticate user`() = runTest {
        val accessToken = accessToken()
        val useCase = AuthenticateUserUseCase(
            query = query,
            passwordHasher = passwordHasher,
            accessTokenGenerator = { _, _ -> accessToken }
        )

        val password = "password"
        val userDetails = userDetails()

        coEvery { query.findByLogin(userDetails.login) } returns userDetails
        every { passwordHasher.matches(password, userDetails.passwordHash) } returns true

        val result = useCase.execute(userDetails.login, password)

        assertThat(result).isEqualTo(accessToken)
    }

    @Test
    fun `should fail when user not found`() = runTest {
        val useCase = AuthenticateUserUseCase(
            query = query,
            passwordHasher = passwordHasher,
            accessTokenGenerator = { _, _ -> accessToken() }
        )

        coEvery { query.findByLogin(any()) } returns null

        assertFailsWith<AuthenticationException.InvalidCredentials> {
            useCase.execute(login(), "password")
        }
    }

    @Test
    fun `should fail when password is incorrect`() = runTest {
        val useCase = AuthenticateUserUseCase(
            query = query,
            passwordHasher = passwordHasher,
            accessTokenGenerator = { _, _ -> accessToken() }
        )

        val userDetails = userDetails()
        coEvery { query.findByLogin(any()) } returns userDetails
        coEvery { passwordHasher.matches(any(), any()) } returns false

        assertFailsWith<AuthenticationException.InvalidCredentials> {
            useCase.execute(userDetails.login, "password")
        }
    }
}
