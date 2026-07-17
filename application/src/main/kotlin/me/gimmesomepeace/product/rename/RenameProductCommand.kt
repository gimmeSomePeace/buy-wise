package me.gimmesomepeace.product.rename

import me.gimmesomepeace.buywise.product.ProductId

data class RenameProductCommand(
    val productId: ProductId,
    val newName: String,
)
