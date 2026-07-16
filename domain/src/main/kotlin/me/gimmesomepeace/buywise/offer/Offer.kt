package me.gimmesomepeace.buywise.offer

import me.gimmesomepeace.buywise.product.ProductId
import me.gimmesomepeace.buywise.shared.Money
import me.gimmesomepeace.buywise.store.StoreId

/**
 * Предложение о приобретении продукта по определенной цене.
 *
 * @property storeId Идентификатор стороны, сформировавшей предложение.
 * @property productId Идентификатор предлагаемого продукта.
 * @property unitPrice Цена за единицу товара.
 */
data class Offer(
    val storeId: StoreId,
    val productId: ProductId,
    val unitPrice: Money,
)
