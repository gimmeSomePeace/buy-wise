package me.gimmesomepeace.offer.price.change

import me.gimmesomepeace.buywise.offer.OfferRepository

class ChangePriceUseCase(
    private val offerRepository: OfferRepository,
) {
    suspend fun execute(command: ChangePriceCommand) {
        val offer = offerRepository.get(command.offerId)
        offer.changePrice(command.newPrice)
        offerRepository.update(offer)
    }
}
