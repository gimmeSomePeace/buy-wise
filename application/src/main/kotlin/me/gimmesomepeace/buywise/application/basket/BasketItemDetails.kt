package me.gimmesomepeace.buywise.application.basket

import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Quantity

data class BasketItemDetails(
    val productId: ProductId,
    val quantity: Quantity,
)
