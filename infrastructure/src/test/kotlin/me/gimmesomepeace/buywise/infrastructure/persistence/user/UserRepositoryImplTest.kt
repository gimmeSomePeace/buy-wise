package me.gimmesomepeace.buywise.infrastructure.persistence.user

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.user.Login
import me.gimmesomepeace.buywise.domain.user.UserException
import me.gimmesomepeace.buywise.domain.user.user
import me.gimmesomepeace.buywise.domain.user.userId
import me.gimmesomepeace.buywise.infrastructure.PostgresSqlContainer
import me.gimmesomepeace.buywise.infrastructure.persistence.TestPersistence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import kotlin.test.Test
import kotlin.test.assertFailsWith

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserRepositoryImpl::class, TestPersistence::class)
internal class UserRepositoryImplTest : PostgresSqlContainer() {
    @Autowired
    lateinit var jpaRepository: UserJpaRepository

    @Autowired
    lateinit var persistence: TestPersistence

    @Autowired
    lateinit var repository: UserRepositoryImpl

    @Nested
    inner class Get {
        @Test
        fun `should return user when exists`() =
            runTest {
                val expected = persistence.persist(user())

                val actual = repository.get(expected.id)

                assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected)
            }

        @Test
        fun `should throw NotFound when user not exists`() =
            runTest {
                val userId = userId()

                val ex =
                    assertFailsWith<UserException.NotFound> {
                        repository.get(userId)
                    }

                assertThat(ex.userId).isEqualTo(userId)
            }
    }

    @Nested
    inner class Add {
        @Test
        fun `should save new user`() =
            runTest {
                val user = user()

                repository.add(user)

                val actual = repository.get(user.id)
                assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(user)
            }

        @Test
        fun `should throw AlreadyExists when id is busy`() =
            runTest {
                val user = persistence.persist(user())

                val ex =
                    assertFailsWith<UserException.AlreadyExists> {
                        repository.add(user)
                    }

                assertThat(ex.userId).isEqualTo(user.id)
            }
    }

    @Nested
    inner class Update {
        @Test
        fun `should update user`() =
            runTest {
                val user =
                    persistence
                        .persist(
                            user(login = Login("old-login")),
                        ).apply { changeLogin(Login("new-login")) }

                repository.update(user)

                val actual = repository.get(user.id)
                assertThat(actual.login.value).isEqualTo("new-login")
                assertThat(actual.id).isEqualTo(user.id)
            }

        @Test
        fun `should throw NotFound when user not exists`() =
            runTest {
                val user = user()

                val ex =
                    assertFailsWith<UserException.NotFound> {
                        repository.update(user)
                    }

                assertThat(ex.userId).isEqualTo(user.id)
            }
    }

    @Nested
    inner class Delete {
        @Test
        fun `should delete user`() =
            runTest {
                val user = persistence.persist(user())

                repository.delete(user.id)

                val ex =
                    assertFailsWith<UserException.NotFound> {
                        repository.get(user.id)
                    }
                assertThat(ex.userId).isEqualTo(user.id)
            }

        @Test
        fun `should throw NotFound when user not exists`() =
            runTest {
                val userId = userId()

                val ex =
                    assertFailsWith<UserException.NotFound> {
                        repository.delete(userId)
                    }

                assertThat(ex.userId).isEqualTo(userId)
            }
    }
}
