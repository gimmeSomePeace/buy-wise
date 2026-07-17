package me.gimmesomepeace.basket.decrease

import me.gimmesomepeace.buywise.product.ProductId
import me.gimmesomepeace.buywise.shared.Quantity

data class DecreaseFromBasketCommand(
    val productId: ProductId,
    val quantity: Quantity
)
