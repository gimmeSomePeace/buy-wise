package me.gimmesomepeace.basket.decrease

import me.gimmesomepeace.buywise.basket.Basket
import me.gimmesomepeace.buywise.basket.BasketRepository

class DecreaseFromBasketUseCase(
    private val basketRepository: BasketRepository,
) {
    suspend fun execute(command: DecreaseFromBasketCommand) {
        val basket = basketRepository.find() ?: Basket()

        basket.decrease(command.productId, command.quantity)
        basketRepository.save(basket)
    }
}