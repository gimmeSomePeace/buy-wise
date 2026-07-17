package me.gimmesomepeace.buywise.offer

import me.gimmesomepeace.buywise.product.ProductId
import me.gimmesomepeace.buywise.store.StoreId

/**
 * Уникальный идентификатор предложения.
 * Является составным идентификатором из идентификаторов продавца и продукта.
 *
 * @property productId Идентификатор продукта.
 * @property storeId Идентификатор магазина.
 */
data class OfferId(
    val productId: ProductId,
    val storeId: StoreId,
)
