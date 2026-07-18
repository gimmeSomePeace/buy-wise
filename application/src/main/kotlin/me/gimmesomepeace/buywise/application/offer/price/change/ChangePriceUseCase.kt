package me.gimmesomepeace.buywise.application.offer.price.change

import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.offer.OfferRepository
import me.gimmesomepeace.buywise.domain.shared.Money

class ChangePriceUseCase(
    private val offerRepository: OfferRepository,
) {
    suspend fun execute(
        offerId: OfferId,
        newPrice: Money,
    ) {
        val offer = offerRepository.get(offerId)
        offer.changePrice(newPrice)
        offerRepository.update(offer)
    }
}
