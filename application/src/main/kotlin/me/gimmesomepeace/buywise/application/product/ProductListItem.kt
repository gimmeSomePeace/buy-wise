package me.gimmesomepeace.buywise.application.product

import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.user.UserId

data class ProductListItem(
    val id: ProductId,
    val ownerId: UserId,
    val name: String,
)
