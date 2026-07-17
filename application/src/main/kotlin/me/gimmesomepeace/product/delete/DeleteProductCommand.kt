package me.gimmesomepeace.product.delete

import me.gimmesomepeace.buywise.product.ProductId

data class DeleteProductCommand(
    val productId: ProductId,
)
