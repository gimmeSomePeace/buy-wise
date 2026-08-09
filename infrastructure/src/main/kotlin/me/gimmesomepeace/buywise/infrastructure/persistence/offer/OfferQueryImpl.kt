package me.gimmesomepeace.buywise.infrastructure.persistence.offer

import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.offer.Offer
import me.gimmesomepeace.buywise.domain.offer.OfferId
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import java.util.*

class OfferQueryImpl(
    private val repository: OfferJpaRepository
) : OfferQuery {
    override suspend fun find(id: OfferId): Offer? =
        repository.findByIdOrNull(id.value)?.toDomain()

    override suspend fun list(request: PageRequest): Page<Offer> {
        val requestWithExtra = Pageable.ofSize(request.pageSize + 1)

        val entities = request.cursor
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
            items = pageItems.map { it.toDomain() },
            cursor = if (hasExtra) Cursor(pageItems.last().id.toString()) else null,
        )
    }
}