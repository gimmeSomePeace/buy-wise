package me.gimmesomepeace.buywise.application.offer

import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.offer.Offer
import me.gimmesomepeace.buywise.domain.offer.OfferId

interface OfferQuery {
    suspend fun find(
        id: OfferId,
    ): Offer?

    suspend fun list(
        request: PageRequest,
    ): Page<Offer>
}
