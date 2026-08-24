package me.gimmesomepeace.buywise.application.offer.get

import me.gimmesomepeace.buywise.application.offer.OfferDetails
import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.domain.offer.OfferException
import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.user.UserId

class GetOfferUseCase(
    private val query: OfferQuery,
) {
    suspend fun execute(
        userId: UserId,
        offerId: OfferId,
    ): OfferDetails {
        val offer =
            query.find(offerId)
                ?: throw OfferException.NotFound(
                    offerId,
                )
        if (offer.ownerId != userId) {
            throw OfferException.NotFound(offerId)
        }
        return offer
    }
}
