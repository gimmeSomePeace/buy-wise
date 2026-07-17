package me.gimmesomepeace.offer.delete

import me.gimmesomepeace.buywise.offer.OfferRepository

class DeleteOfferUseCase(
    private val offerRepository: OfferRepository
) {
    suspend fun execute(command: DeleteOfferCommand) {
        offerRepository.delete(command.offerId)
    }
}
