package me.gimmesomepeace.buywise.web.auth

import jakarta.validation.Valid
import me.gimmesomepeace.buywise.application.auth.AuthenticateUserUseCase
import me.gimmesomepeace.buywise.domain.user.Login
import me.gimmesomepeace.buywise.web.auth.login.LoginRequest
import me.gimmesomepeace.buywise.web.auth.login.LoginResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/auth")
@RestController
class AuthController(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
) {
    @PostMapping("/login")
    suspend fun login(
        @Valid @RequestBody request: LoginRequest,
    ): ResponseEntity<LoginResponse> {
        val accessToken =
            authenticateUserUseCase.execute(
                login = Login(request.login),
                password = request.password,
            )
        val response =
            LoginResponse(accessToken.value)

        return ResponseEntity.ok(response)
    }
}
