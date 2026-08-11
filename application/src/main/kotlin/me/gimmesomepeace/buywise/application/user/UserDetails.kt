package me.gimmesomepeace.buywise.application.user

import me.gimmesomepeace.buywise.domain.shared.password.PasswordHash
import me.gimmesomepeace.buywise.domain.user.Login
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.UserRole

data class UserDetails(
    val id: UserId,
    val login: Login,
    val passwordHash: PasswordHash,
    val role: UserRole,
)
