package me.gimmesomepeace.buywise.web.auth.login

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank
    val login: String,
    @field:NotBlank
    val password: String,
)
