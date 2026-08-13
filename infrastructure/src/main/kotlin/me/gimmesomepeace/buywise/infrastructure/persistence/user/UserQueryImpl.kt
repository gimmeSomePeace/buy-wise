package me.gimmesomepeace.buywise.infrastructure.persistence.user

import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.user.UserListItem
import me.gimmesomepeace.buywise.application.user.UserQuery
import me.gimmesomepeace.buywise.application.user.UserView
import me.gimmesomepeace.buywise.domain.user.Login
import org.springframework.data.domain.Pageable
import java.util.*

class UserQueryImpl(
    private val repository: UserJpaRepository,
) : UserQuery {

    override suspend fun findByLogin(login: Login): UserView? =
        repository.findByLogin(login.value)?.toView()

    override suspend fun list(request: PageRequest): Page<UserListItem> {
        val requestWithExtra = Pageable.ofSize(request.pageSize + 1)

        val entities =
            request.cursor
                ?.let { cursor ->
                    repository.findByIdGreaterThanOrderByIdAsc(
                        id = UUID.fromString(cursor.value),
                        pageable = requestWithExtra,
                    )
                }
                ?: repository.findAll(requestWithExtra).content

        val hasExtra = entities.size > request.pageSize
        val pageItems = if (hasExtra) entities.dropLast(1) else entities

        return Page(
            items = pageItems.map { it.toListItem() },
            cursor =
                if (hasExtra) {
                    Cursor(
                        pageItems.last().id.toString(),
                    )
                } else {
                    null
                },
        )
    }
}
