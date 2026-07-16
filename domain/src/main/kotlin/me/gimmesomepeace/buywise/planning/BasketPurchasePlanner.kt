package me.gimmesomepeace.buywise.planning

import me.gimmesomepeace.buywise.basket.Basket
import me.gimmesomepeace.buywise.offer.AvailableOfferCatalog
import me.gimmesomepeace.buywise.planning.plan.PurchaseItem
import me.gimmesomepeace.buywise.planning.plan.PurchasePlan
import me.gimmesomepeace.buywise.planning.plan.StorePurchasePlan
import me.gimmesomepeace.buywise.shared.Money
import me.gimmesomepeace.buywise.store.StoreId
import kotlin.math.min

internal fun <T> combinations(
    items: List<T>,
    maxSize: Int,
): Sequence<List<T>> =
    sequence {
        val current = ArrayList<T>(maxSize)

        suspend fun SequenceScope<List<T>>.dfs(start: Int) {
            if (current.size == maxSize) {
                yield(current.toList())
                return
            }

            for (i in start until items.size) {
                current += items[i]
                dfs(i + 1)
                current.removeLast()
            }
        }

        dfs(0)
    }

/**
 * Сервис построения планов покупки.
 */
object BasketPurchasePlanner {
    /**
     * Строит планы покупки на основе корзины и доступных предложений.
     *
     * @param basket Корзина, содержащая все товары, покупку которых необходимо распланировать.
     * @param offers Предложения на покупку.
     * @param maxStores Ограничение на максимальное количество используемых магазинов в пределах одного плана.
     *
     * @return Результат, содержащий найденные планы покупки
     * или информацию о невозможности их построения.
     * Планы покупки отсортированы по возрастанию цены.
     *
     * @throws IllegalStateException если корзина пуста (нечего планировать).
     */
    fun plan(
        basket: Basket,
        offers: AvailableOfferCatalog,
        maxStores: StoreCountLimit,
    ): PurchasePlanningResult {
        require(!basket.isEmpty()) {
            "Basket must not be empty for planning"
        }

        val storeIds = offers.stores()
        val maxSize =
            when (maxStores) {
                StoreCountLimit.Unlimited -> storeIds.size
                is StoreCountLimit.Limited -> min(storeIds.size, maxStores.value)
            }

        val plans =
            (1..maxSize)
                .asSequence()
                .flatMap { combinations(storeIds.toList(), it) }
                .mapNotNull { evaluate(basket, it, offers) }
                .sortedBy { it.totalPrice }
                .toList()

        return if (plans.isEmpty()) {
            PurchasePlanningResult.Impossible
        } else {
            PurchasePlanningResult.Success(
                plans = plans.distinct(),
            )
        }
    }

    private fun evaluate(
        basket: Basket,
        stores: List<StoreId>,
        availableOfferCatalog: AvailableOfferCatalog,
    ): PurchasePlan? {
        val purchases = mutableMapOf<StoreId, MutableList<PurchaseItem>>()
        for (item in basket.items()) {
            var cheapestStore: StoreId? = null
            var cheapestPrice: Money? = null

            for (store in stores) {
                val price =
                    availableOfferCatalog
                        .of(item.id, store)
                        ?.unitPrice
                        ?: continue

                if (cheapestPrice == null || price < cheapestPrice) {
                    cheapestPrice = price
                    cheapestStore = store
                }
            }

            if (cheapestStore == null || cheapestPrice == null) return null

            purchases
                .getOrPut(cheapestStore) { mutableListOf() }
                .add(
                    PurchaseItem(
                        item.id,
                        item.quantity,
                        cheapestPrice,
                    ),
                )
        }

        return PurchasePlan(
            storePlans =
                purchases.entries.map {
                    StorePurchasePlan(it.key, it.value)
                },
        )
    }
}
