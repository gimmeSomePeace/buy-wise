package me.gimmesomepeace.buywise.web.user.reg

import jakarta.validation.constraints.NotBlank

data class RegisterUserRequest(
    @field:NotBlank
    val login: String,

    @field:NotBlank
    val password: String,
)
