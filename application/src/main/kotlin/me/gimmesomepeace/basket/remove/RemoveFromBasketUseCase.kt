package me.gimmesomepeace.basket.remove

import me.gimmesomepeace.buywise.basket.Basket
import me.gimmesomepeace.buywise.basket.BasketRepository

class RemoveFromBasketUseCase(
    private val basketRepository: BasketRepository,
) {
    suspend fun execute(command: RemoveFromBasketCommand) {
        val basket = basketRepository.find() ?: Basket()

        basket.remove(
            productId = command.productId
        )
    }
}