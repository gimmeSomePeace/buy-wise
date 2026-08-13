package me.gimmesomepeace.buywise.application.user

import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.user.Login

interface UserQuery {
    suspend fun findByLogin(login: Login): UserView?
    suspend fun list(request: PageRequest): Page<UserListItem>
}
