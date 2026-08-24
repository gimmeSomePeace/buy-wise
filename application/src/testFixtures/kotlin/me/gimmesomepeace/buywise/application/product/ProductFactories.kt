package me.gimmesomepeace.buywise.application.product

import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.userId

fun productDetails(
    id: ProductId = productId(),
    ownerId: UserId = userId(),
    name: String = "PRODUCT DETAILS NAME",
) = ProductDetails(
    id = id,
    ownerId = ownerId,
    name = name,
)

fun productListItem(
    id: ProductId = productId(),
    ownerId: UserId = userId(),
    name: String = "PRODUCT LIST ITEM NAME",
) = ProductListItem(
    id = id,
    ownerId = ownerId,
    name = name,
)
