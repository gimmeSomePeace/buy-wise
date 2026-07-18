package me.gimmesomepeace.buywise.application.basket.clear

import me.gimmesomepeace.buywise.domain.basket.Basket
import me.gimmesomepeace.buywise.domain.basket.BasketRepository

class ClearBasketUseCase(
    private val basketRepository: BasketRepository,
) {
    suspend fun execute() {
        val basket = basketRepository.find() ?: Basket()
        basket.clear()
        basketRepository.save(basket)
    }
}
