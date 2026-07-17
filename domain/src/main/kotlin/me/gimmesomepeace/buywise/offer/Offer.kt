package me.gimmesomepeace.buywise.offer

import me.gimmesomepeace.buywise.product.ProductId
import me.gimmesomepeace.buywise.shared.Money
import me.gimmesomepeace.buywise.store.StoreId

class Offer(
    val id: OfferId,
    val productId: ProductId,
    val storeId: StoreId,
    unitPrice: Money,
) {
    var unitPrice: Money = unitPrice
        private set

    fun changePrice(newPrice: Money) {
        unitPrice = newPrice
    }
}
