package me.gimmesomepeace.buywise.infrastructure.persistence.basket

import me.gimmesomepeace.buywise.application.basket.BasketItemDetails
import me.gimmesomepeace.buywise.domain.basket.BasketItem
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Quantity

internal fun BasketItem.toEntity() = BasketEntity(
    productId = this.id.value,
    quantity = this.quantity.value
)

internal fun BasketEntity.toBasketItemDetails() = BasketItemDetails(
    productId = ProductId(this.productId),
    quantity = Quantity(this.quantity),
)
