package me.gimmesomepeace.buywise.infrastructure.persistence.store

import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.store.StoreListItem
import me.gimmesomepeace.buywise.application.store.StoreQuery
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.user.UserId
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import java.util.UUID

class StoreQueryImpl(
    private val repository: StoreJpaRepository,
) : StoreQuery {
    override suspend fun find(
        id: StoreId,
    ) = repository.findByIdOrNull(id.value)?.toDetails()

    override suspend fun list(
        ownerId: UserId,
        request: PageRequest,
    ): Page<StoreListItem> {
        val requestWithExtra = Pageable.ofSize(request.pageSize + 1)

        val entities =
            request.cursor
                ?.let { cursor ->
                    repository.findByOwnerIdAndIdGreaterThanOrderByIdAsc(
                        ownerId = ownerId.value,
                        id = UUID.fromString(cursor.value),
                        pageable = requestWithExtra,
                    )
                }
                ?: repository.findByOwnerId(ownerId = ownerId.value, requestWithExtra)

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
