package me.gimmesomepeace.buywise.planning.plan

import me.gimmesomepeace.buywise.shared.Money
import me.gimmesomepeace.buywise.store.StoreId

/**
 * План покупки в отдельном магазине.
 *
 * Инварианты:
 *  - Набор покупок в магазине не должен быть пустым.
 *  - Все покупки должны быть выражены в одной валюте.
 *  - Покупка одного и того же товара не должна повторяться.
 *
 * @property storeId Идентификатор магазина, план покупки в котором и был составлен.
 * @property purchases Набор покупок в магазине.
 */
data class StorePurchasePlan(
    val storeId: StoreId,
    val purchases: List<PurchaseItem>,
) {
    /**
     * Общая стоимость покупки.
     */
    val totalPrice: Money

    init {
        require(purchases.isNotEmpty()) { "Store purchase items must not be empty." }

        val currencies = purchases.map { it.unitPrice.currency }.distinct()

        require(currencies.size == 1) {
            "Store purchase must contain only one currency."
        }
        require(purchases.size == purchases.distinctBy { it.productId }.size) {
            "Store purchase must not have duplicate products."
        }

        val currency = currencies.first()
        totalPrice =
            purchases
                .map { it.unitPrice * it.quantity }
                .fold(Money.zero(currency), Money::plus)
    }
}
