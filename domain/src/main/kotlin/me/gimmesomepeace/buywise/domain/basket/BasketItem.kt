package me.gimmesomepeace.buywise.domain.basket

import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Quantity

/**
 * Позиция продукта в корзине.
 *
 * @property id Идентификатор продукта.
 * @property quantity Количество продукта.
 */
data class BasketItem(
    val id: ProductId,
    val quantity: Quantity,
)
