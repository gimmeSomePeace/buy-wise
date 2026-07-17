package me.gimmesomepeace.basket.add

import me.gimmesomepeace.buywise.basket.Basket
import me.gimmesomepeace.buywise.basket.BasketRepository

class AddToBasketUseCase(
    private val basketRepository: BasketRepository
) {
    suspend fun execute(command: AddToBasketCommand) {
        val basket = basketRepository.find() ?: Basket()

        basket.add(
            command.productId,
            command.quantity,
        )

        basketRepository.save(basket)
    }
}
