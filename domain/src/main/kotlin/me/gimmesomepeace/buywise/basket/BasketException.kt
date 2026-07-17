package me.gimmesomepeace.buywise.basket

import me.gimmesomepeace.buywise.product.ProductId
import me.gimmesomepeace.buywise.shared.DomainException

/**
 * Базовый тип исключений, связанных с операциями над корзиной.
 */
sealed class BasketException(
    message: String,
) : DomainException(message) {
    /**
     * Запрошенный продукт отсутствует в корзине.
     *
     * @property productId Идентификатор отсутствующего продукта.
     */
    class ProductNotInBasket(
        val productId: ProductId,
    ) : BasketException(
            message = "Product $productId not in basket.",
        )
}
