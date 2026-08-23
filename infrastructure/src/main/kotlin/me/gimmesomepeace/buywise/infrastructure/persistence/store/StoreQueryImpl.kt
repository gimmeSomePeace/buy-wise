package me.gimmesomepeace.buywise.infrastructure.persistence.store

import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.store.StoreFilters
import me.gimmesomepeace.buywise.application.store.StoreListItem
import me.gimmesomepeace.buywise.application.store.StoreQuery
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.infrastructure.persistence.store.specification.StoreSpecifications
import me.gimmesomepeace.buywise.infrastructure.persistence.store.specification.toSpecification
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull

class StoreQueryImpl(
    private val repository: StoreJpaRepository,
) : StoreQuery {
    override suspend fun find(
        id: StoreId,
    ) = repository.findByIdOrNull(id.value)?.toDetails()

    override suspend fun list(
        request: PageRequest,
        filters: StoreFilters,
    ): Page<StoreListItem> {

        val spec = listOfNotNull(
            filters.toSpecification(),
            request.cursor?.let { cursor -> StoreSpecifications.afterCursor(cursor) }
        ).reduce { acc, s -> acc.and(s) }

        val requestWithExtra = Pageable.ofSize(request.pageSize + 1)
        val entities = repository.findAll(spec, requestWithExtra).content

        val hasExtra = entities.size > request.pageSize
        val pageItems = if (hasExtra) entities.dropLast(1) else entities

        return Page(
            items = pageItems.map { it.toListItem() },
            cursor =
                if (hasExtra) Cursor(pageItems.last().id.toString())
                else null
        )
    }
}
