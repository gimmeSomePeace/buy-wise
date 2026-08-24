package me.gimmesomepeace.buywise.application.user

import me.gimmesomepeace.buywise.domain.user.Login
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.UserRole

data class UserListItem(
    val id: UserId,
    val login: Login,
    val role: UserRole,
)
