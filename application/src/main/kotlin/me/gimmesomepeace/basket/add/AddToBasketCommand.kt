package me.gimmesomepeace.basket.add

import me.gimmesomepeace.buywise.product.ProductId
import me.gimmesomepeace.buywise.shared.Quantity

data class AddToBasketCommand(
    val productId: ProductId,
    val quantity: Quantity
)
