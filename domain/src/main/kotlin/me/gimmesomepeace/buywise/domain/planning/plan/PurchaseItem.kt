package me.gimmesomepeace.buywise.domain.planning.plan

import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.shared.Quantity

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
