package me.gimmesomepeace.buywise.application.offer.list

import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.offer.Offer
import me.gimmesomepeace.buywise.domain.user.UserId

class ListOffersUseCase(
    private val offerQuery: OfferQuery,
) {
    suspend fun execute(
        ownerId: UserId,
        request: PageRequest,
    ): Page<Offer> = offerQuery.list(ownerId, request)
}
