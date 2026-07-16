package me.gimmesomepeace.buywise.planning.plan

import me.gimmesomepeace.buywise.product.ProductId
import me.gimmesomepeace.buywise.shared.Money
import me.gimmesomepeace.buywise.shared.Quantity

/**
 * Позиция плана покупки.
 *
 * @property productId Идентификатор продукта.
 * @property quantity Количество приобретаемого продукта.
 * @property unitPrice Цена за единицу продукта.
 */
data class PurchaseItem(
    val productId: ProductId,
    val quantity: Quantity,
    val unitPrice: Money,
)
