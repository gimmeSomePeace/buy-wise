package me.gimmesomepeace.buywise.web.user

import java.util.UUID

data class UserDetailsResponse(
    val id: UUID,
    val login: String,
)
