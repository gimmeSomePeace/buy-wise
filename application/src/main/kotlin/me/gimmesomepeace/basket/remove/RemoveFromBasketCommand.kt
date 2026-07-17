package me.gimmesomepeace.basket.remove

import me.gimmesomepeace.buywise.product.ProductId

data class RemoveFromBasketCommand(
    val productId: ProductId,
)
