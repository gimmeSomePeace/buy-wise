package me.gimmesomepeace.basket.clear

import me.gimmesomepeace.buywise.basket.Basket
import me.gimmesomepeace.buywise.basket.BasketRepository

class ClearBasketUseCase(
    private val basketRepository: BasketRepository,
) {
    suspend fun execute() {
        val basket = basketRepository.find() ?: Basket()
        basket.clear()
        basketRepository.save(basket)
    }
}
