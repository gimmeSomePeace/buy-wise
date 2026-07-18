package me.gimmesomepeace.buywise.application.basket.remove

import me.gimmesomepeace.buywise.domain.basket.Basket
import me.gimmesomepeace.buywise.domain.basket.BasketRepository
import me.gimmesomepeace.buywise.domain.product.ProductId

class RemoveFromBasketUseCase(
    private val basketRepository: BasketRepository,
) {
    suspend fun execute(productId: ProductId) {
        val basket = basketRepository.find() ?: Basket()

        basket.remove(
            productId = productId,
        )
    }
}
