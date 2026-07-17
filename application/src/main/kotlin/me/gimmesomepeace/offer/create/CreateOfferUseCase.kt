package me.gimmesomepeace.offer.create

import me.gimmesomepeace.IdGenerator
import me.gimmesomepeace.buywise.offer.Offer
import me.gimmesomepeace.buywise.offer.OfferId
import me.gimmesomepeace.buywise.offer.OfferRepository

class CreateOfferUseCase(
    private val idGenerator: IdGenerator<OfferId>,
    private val offerRepository: OfferRepository,
) {
    suspend fun execute(command: CreateOfferCommand) {
        val id = idGenerator.generate()
        val offer = Offer(
            id = id,
            productId = command.productId,
            storeId = command.storeId,
            unitPrice = command.unitPrice,
        )

        offerRepository.add(offer)
    }
}