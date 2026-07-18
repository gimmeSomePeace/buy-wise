package me.gimmesomepeace.buywise.domain.planning.offer

import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.store.StoreId

/**
 * Предложение о приобретении продукта по определенной цене.
 *
 * @property storeId Идентификатор стороны, сформировавшей предложение.
 * @property productId Идентификатор предлагаемого продукта.
 * @property unitPrice Цена за единицу товара.
 */
data class AvailableOffer(
    val storeId: StoreId,
    val productId: ProductId,
    val unitPrice: Money,
)
