package me.gimmesomepeace.buywise.application.user.auth

import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.UserRole

data class AuthenticationResult(
    val userId: UserId,
    val role: UserRole,
)
