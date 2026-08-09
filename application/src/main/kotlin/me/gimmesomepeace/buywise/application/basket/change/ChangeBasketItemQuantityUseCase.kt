package me.gimmesomepeace.buywise.application.basket.change

import me.gimmesomepeace.buywise.domain.basket.Basket
import me.gimmesomepeace.buywise.domain.basket.BasketRepository
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Quantity

class ChangeBasketItemQuantityUseCase(
    private val basketRepository: BasketRepository
) {
    suspend fun execute(
        productId: ProductId,
        quantity: Quantity
    ) {
        val basket = basketRepository.find() ?: Basket()
        val oldQuantity = basket.quantityOf(productId)

        if (oldQuantity > quantity) basket.decrease(productId, oldQuantity - quantity)
        else if (quantity > oldQuantity) basket.add(productId, quantity - oldQuantity)

        basketRepository.save(basket)
    }
}