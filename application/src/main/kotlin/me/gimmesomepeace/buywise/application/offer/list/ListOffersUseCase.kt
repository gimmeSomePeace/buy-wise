package me.gimmesomepeace.buywise.application.offer.list

import me.gimmesomepeace.buywise.application.offer.OfferFilters
import me.gimmesomepeace.buywise.application.offer.OfferListItem
import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.user.UserId

class ListOffersUseCase(
    private val offerQuery: OfferQuery,
) {
    suspend fun execute(
        userId: UserId,
        request: PageRequest,
    ): Page<OfferListItem> =
        offerQuery.list(
            request,
            OfferFilters(ownerId = userId),
        )
}
