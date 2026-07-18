package me.gimmesomepeace.buywise.application.offer.delete

import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.offer.OfferRepository

class DeleteOfferUseCase(
    private val offerRepository: OfferRepository,
) {
    suspend fun execute(offerId: OfferId) {
        offerRepository.delete(offerId)
    }
}
