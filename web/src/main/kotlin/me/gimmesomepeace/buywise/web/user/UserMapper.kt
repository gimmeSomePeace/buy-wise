package me.gimmesomepeace.buywise.web.user

import me.gimmesomepeace.buywise.application.user.UserListItem
import me.gimmesomepeace.buywise.domain.user.User

fun User.toDetailsResponse() = UserDetailsResponse(
    id = this.id.value,
    login = this.login.value,
)

fun UserListItem.toResponse() = UserListItemResponse(
    id = this.id.value,
    login = this.login.value,
    role = this.role,
)
