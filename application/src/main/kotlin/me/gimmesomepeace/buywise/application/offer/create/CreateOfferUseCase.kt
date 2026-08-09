package me.gimmesomepeace.buywise.application.offer.create

import me.gimmesomepeace.buywise.application.shared.IdGenerator
import me.gimmesomepeace.buywise.domain.offer.Offer
import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.offer.OfferRepository
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.store.StoreId

class CreateOfferUseCase(
    private val idGenerator: IdGenerator<OfferId>,
    private val offerRepository: OfferRepository,
) {
    suspend fun execute(
        productId: ProductId,
        storeId: StoreId,
        unitPrice: Money,
    ) : Offer {
        val id = idGenerator.generate()
        val offer =
            Offer(
                id = id,
                productId = productId,
                storeId = storeId,
                unitPrice = unitPrice,
            )

        offerRepository.add(offer)
        return offer
    }
}
