package me.gimmesomepeace.buywise.web.user

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import me.gimmesomepeace.buywise.application.user.list.ListUsersUseCase
import me.gimmesomepeace.buywise.application.user.reg.RegisterUserUseCase
import me.gimmesomepeace.buywise.domain.shared.password.PasswordHash
import me.gimmesomepeace.buywise.domain.user.Login
import me.gimmesomepeace.buywise.domain.user.UserException
import me.gimmesomepeace.buywise.domain.user.user
import me.gimmesomepeace.buywise.web.TestSecurityConfig
import me.gimmesomepeace.buywise.web.user.reg.RegisterUserRequest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import tools.jackson.databind.ObjectMapper
import java.util.stream.Stream

@WebMvcTest(UserController::class)
@Import(TestSecurityConfig::class)
class UserControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var registerUserUseCase: RegisterUserUseCase

    @MockkBean
    lateinit var listUsersUseCase: ListUsersUseCase

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class Register {
        @Test
        fun `should register user`() {
            val login = "login"
            val password = "password"

            val request = RegisterUserRequest(
                login = login,
                password = password,
            )

            coEvery {
                registerUserUseCase.execute(Login(login), password)
            } returns user(
                login = Login(login),
                passwordHash = PasswordHash("password-hash")
            )

            val mockResult = mockMvc.post("/users") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andReturn()

            mockMvc.perform(asyncDispatch(mockResult))
                .andExpect(status().isCreated)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.login").value(login))
        }

        @Test
        fun `should return 409 when login is busy`() {
            val login = "login"
            val request = RegisterUserRequest(
                login = login,
                password = "password"
            )
            coEvery {
                registerUserUseCase.execute(login = Login(login), any())
            } throws UserException.LoginBusy(Login(login))

            val mockResult = mockMvc.post("/users") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andReturn()

            mockMvc.perform(asyncDispatch(mockResult))
                .andExpect(status().isConflict)
        }

        @ParameterizedTest
        @MethodSource("invalidRegisterRequests")
        fun `should return 400 when request is invalid`(requestBody: Map<String, Any?>) {
            mockMvc.post("/users") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(requestBody)
            }.andExpect {
                status { isBadRequest() }
            }
        }

        fun invalidRegisterRequests(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    mapOf("password" to "valid-password"),
                ),
                Arguments.of(
                    mapOf("login" to "valid-login")
                ),
                Arguments.of(
                    mapOf("login" to "valid-login", "password" to "   "),
                ),
                Arguments.of(
                    mapOf("login" to "  ", "password" to "valid-password"),
                )
            )
    }
}
