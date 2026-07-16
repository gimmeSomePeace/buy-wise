package me.gimmesomepeace.buywise.basket

import me.gimmesomepeace.buywise.product.ProductId
import me.gimmesomepeace.buywise.shared.Quantity

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
