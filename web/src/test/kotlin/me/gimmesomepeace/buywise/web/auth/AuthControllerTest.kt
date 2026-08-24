package me.gimmesomepeace.buywise.web.auth

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import me.gimmesomepeace.buywise.application.auth.AccessToken
import me.gimmesomepeace.buywise.application.auth.AuthenticateUserUseCase
import me.gimmesomepeace.buywise.application.auth.AuthenticationException
import me.gimmesomepeace.buywise.domain.user.Login
import me.gimmesomepeace.buywise.domain.user.login
import me.gimmesomepeace.buywise.web.TestSecurityConfig
import me.gimmesomepeace.buywise.web.auth.login.LoginRequest
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.jackson.databind.ObjectMapper
import java.util.stream.Stream

@WebMvcTest(AuthController::class)
@Import(TestSecurityConfig::class)
class AuthControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var authenticateUserUseCase: AuthenticateUserUseCase

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class Login {
        @Test
        fun `should successfully login`() {
            val login = login()
            val password = "password"
            coEvery {
                authenticateUserUseCase.execute(login, password)
            } returns AccessToken("access-token")

            val request = LoginRequest(login = login.value, password = password)

            val mvcResult =
                mockMvc
                    .post("/auth/login") {
                        contentType = MediaType.APPLICATION_JSON
                        content = objectMapper.writeValueAsString(request)
                    }.andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value("access-token"))
        }

        @Test
        fun `should return 401 when invalid credentials`() {
            coEvery {
                authenticateUserUseCase.execute(Login("login"), "password")
            } throws AuthenticationException.InvalidCredentials()

            val request = LoginRequest(login = "login", password = "password")
            val mvcResult =
                mockMvc
                    .post("/auth/login") {
                        contentType = MediaType.APPLICATION_JSON
                        content = objectMapper.writeValueAsString(request)
                    }.andReturn()

            mockMvc
                .perform(asyncDispatch(mvcResult))
                .andExpect(status().isUnauthorized)
                .andExpect(
                    header().string(
                        "WWW-Authenticate",
                        "Bearer realm=\"buywise\"",
                    ),
                )

            coVerify(exactly = 1) {
                authenticateUserUseCase.execute(Login("login"), "password")
            }
        }

        @ParameterizedTest
        @MethodSource("invalidLoginRequests")
        fun `should return 400 when bad request`(requestBody: Map<String, Any?>) {
            mockMvc
                .post("/auth/login") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(requestBody)
                }.andExpect {
                    status { isBadRequest() }
                }
        }

        fun invalidLoginRequests(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    mapOf("password" to "valid-password"),
                ),
                Arguments.of(
                    mapOf("login" to "valid-login"),
                ),
                Arguments.of(
                    mapOf("login" to "valid-login", "password" to "   "),
                ),
                Arguments.of(
                    mapOf("login" to "  ", "password" to "valid-password"),
                ),
            )
    }
}
