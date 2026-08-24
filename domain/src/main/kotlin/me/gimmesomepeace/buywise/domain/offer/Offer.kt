package me.gimmesomepeace.buywise.domain.offer

import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.store.StoreId

/**
 * Предложение магазина о продаже определенного товара.
 *
 * @property id Идентификатор предложения.
 * @property productId Идентификатор предлагаемого товара.
 * @property storeId Идентификатор магазина, которому принадлежит предложение.
 */
class Offer(
    val id: OfferId,
    val productId: ProductId,
    val storeId: StoreId,
    unitPrice: Money,
) {
    /**
     * Цена одной единицы товара.
     */
    var unitPrice: Money = unitPrice
        private set

    fun changePrice(newPrice: Money) {
        unitPrice = newPrice
    }
}
