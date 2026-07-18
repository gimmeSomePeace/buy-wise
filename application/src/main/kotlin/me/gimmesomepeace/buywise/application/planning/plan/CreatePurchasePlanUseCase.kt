package me.gimmesomepeace.buywise.application.planning.plan

import me.gimmesomepeace.buywise.domain.basket.Basket
import me.gimmesomepeace.buywise.domain.basket.BasketRepository
import me.gimmesomepeace.buywise.domain.offer.OfferRepository
import me.gimmesomepeace.buywise.domain.planning.BasketPurchasePlanner
import me.gimmesomepeace.buywise.domain.planning.PurchasePlanningResult
import me.gimmesomepeace.buywise.domain.planning.StoreCountLimit

class CreatePurchasePlanUseCase(
    private val basketRepository: BasketRepository,
    private val offerRepository: OfferRepository,
) {
    suspend fun execute(storeCountLimit: StoreCountLimit): PurchasePlanningResult {
        val basket = basketRepository.find() ?: Basket()
        val availableOffers = offerRepository.availableOffers()

        return BasketPurchasePlanner.plan(
            basket = basket,
            offers = availableOffers,
            maxStores = storeCountLimit,
        )
    }
}
