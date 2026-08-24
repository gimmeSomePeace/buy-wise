package me.gimmesomepeace.buywise.application.offer.delete

import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.domain.offer.OfferException
import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.offer.OfferRepository
import me.gimmesomepeace.buywise.domain.user.UserId

class DeleteOfferUseCase(
    private val repository: OfferRepository,
    private val query: OfferQuery,
) {
    suspend fun execute(
        userId: UserId,
        offerId: OfferId,
    ) {
        if (!query.existsByIdAndOwner(
                offerId,
                userId,
            )
        ) {
            throw OfferException.NotFound(offerId)
        }

        repository.delete(offerId)
    }
}
