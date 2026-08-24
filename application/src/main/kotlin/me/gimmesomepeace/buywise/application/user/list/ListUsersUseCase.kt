package me.gimmesomepeace.buywise.application.user.list

import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.user.UserListItem
import me.gimmesomepeace.buywise.application.user.UserQuery

class ListUsersUseCase(
    private val query: UserQuery,
) {
    suspend fun execute(request: PageRequest): Page<UserListItem> = query.list(request)
}
