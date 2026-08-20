package me.gimmesomepeace.buywise.infrastructure.persistence.offer

import me.gimmesomepeace.buywise.application.offer.OfferDetails
import me.gimmesomepeace.buywise.application.offer.OfferListItem
import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.user.UserId
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import java.util.*

class OfferQueryImpl(
    private val repository: OfferJpaRepository,
) : OfferQuery {
    override suspend fun find(
        id: OfferId,
    ): OfferDetails? = repository.findByIdOrNull(id.value)?.toDetails()

    override suspend fun list(
        ownerId: UserId,
        request: PageRequest,
    ): Page<OfferListItem> {
        val requestWithExtra = Pageable.ofSize(request.pageSize + 1)

        val entities =
            request.cursor
                ?.let { cursor ->
                    // TODO: нужно потом заменить на фильтрацию только по своим offer
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
                } else null
        )
    }

    override suspend fun existsByIdAndOwner(
        id: OfferId,
        userId: UserId
    ): Boolean = repository.existsByIdAndOwnerId(id.value, userId.value)
}
