package me.gimmesomepeace.buywise.domain.basket

import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.DomainException

sealed class BasketException(
    message: String,
) : DomainException(message) {
    class ProductNotInBasket(
        val productId: ProductId,
    ) : BasketException(
            message = "Product $productId not in basket.",
        )
}
