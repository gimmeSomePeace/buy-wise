package me.gimmesomepeace.buywise.application.basket.decrease

import me.gimmesomepeace.buywise.domain.basket.Basket
import me.gimmesomepeace.buywise.domain.basket.BasketRepository
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Quantity

class DecreaseFromBasketUseCase(
    private val basketRepository: BasketRepository,
) {
    suspend fun execute(
        productId: ProductId,
        quantity: Quantity,
    ) {
        val basket = basketRepository.find() ?: Basket()

        basket.decrease(productId, quantity)
        basketRepository.save(basket)
    }
}
