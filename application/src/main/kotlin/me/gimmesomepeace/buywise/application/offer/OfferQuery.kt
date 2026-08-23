package me.gimmesomepeace.buywise.application.offer

import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.user.UserId

interface OfferQuery {
    suspend fun find(
        id: OfferId,
    ): OfferDetails?

    suspend fun list(
        request: PageRequest,
        filters: OfferFilters = OfferFilters(),
    ): Page<OfferListItem>

    suspend fun existsByIdAndOwner(id: OfferId, userId: UserId): Boolean
}
