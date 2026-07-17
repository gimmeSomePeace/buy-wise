package me.gimmesomepeace.planning.plan

import me.gimmesomepeace.buywise.basket.Basket
import me.gimmesomepeace.buywise.basket.BasketRepository
import me.gimmesomepeace.buywise.offer.OfferRepository
import me.gimmesomepeace.buywise.planning.BasketPurchasePlanner
import me.gimmesomepeace.buywise.planning.PurchasePlanningResult

class CreatePurchasePlanUseCase(
    private val basketRepository: BasketRepository,
    private val offerRepository: OfferRepository,
) {
    suspend fun execute(command: CreatePurchasePlanCommand) : PurchasePlanningResult {
        val basket = basketRepository.find() ?: Basket()
        val availableOffers = offerRepository.availableOffers()

        return BasketPurchasePlanner.plan(
            basket = basket,
            offers = availableOffers,
            maxStores = command.storeCountLimit
        )
    }
}
