package me.gimmesomepeace.buywise.web.basket

import me.gimmesomepeace.buywise.application.basket.BasketDetails
import me.gimmesomepeace.buywise.application.basket.BasketItemDetails
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Quantity

fun basketDetails(
    vararg items: BasketItemDetails
) = BasketDetails(items.toList())

fun basketItemsDetails(
    productId: ProductId = productId(),
    quantity: Quantity = Quantity.ONE
) = BasketItemDetails(
    productId = productId,
    quantity = quantity,
)
