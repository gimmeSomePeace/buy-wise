package me.gimmesomepeace.buywise.web.user

import me.gimmesomepeace.buywise.domain.user.UserRole
import java.util.UUID

data class UserListItemResponse(
    val id: UUID,
    val login: String,
    val role: UserRole,
)
