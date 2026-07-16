package me.gimmesomepeace.buywise.planning.plan

import me.gimmesomepeace.buywise.shared.Money

/**
 * План покупки набора продуктов.
 *
 * Инварианты:
 *  - Должен быть хотя бы один план покупки в магазине.
 *  - Все планы покупки в магазинах должны быть выражены в одной валюте.
 *  - Каждый магазин должен встречаться не более одного раза.
 *
 * @property storePlans Планы покупки для отдельных магазинов.
 */
data class PurchasePlan(
    val storePlans: List<StorePurchasePlan>,
) {
    /**
     * Общая стоимость покупки.
     */
    val totalPrice: Money

    init {
        require(storePlans.isNotEmpty()) { "store plans must have at least one store." }

        val currencies =
            storePlans
                .map { it.totalPrice.currency }
                .distinct()

        require(currencies.size == 1) {
            "Purchase plan must contain only one currency."
        }
        require(storePlans.size == storePlans.map { it.storeId }.distinct().size) {
            "Purchase plan must not have duplicate stores."
        }

        val currency = currencies.first()
        totalPrice =
            storePlans
                .map { it.totalPrice }
                .fold(Money.zero(currency), Money::plus)
    }
}
