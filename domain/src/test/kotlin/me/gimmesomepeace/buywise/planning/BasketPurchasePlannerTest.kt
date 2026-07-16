package me.gimmesomepeace.buywise.planning

import me.gimmesomepeace.buywise.basket.Basket
import me.gimmesomepeace.buywise.money
import me.gimmesomepeace.buywise.offer
import me.gimmesomepeace.buywise.offersCatalog
import me.gimmesomepeace.buywise.planning.plan.PurchaseItem
import me.gimmesomepeace.buywise.productId
import me.gimmesomepeace.buywise.shared.Quantity
import me.gimmesomepeace.buywise.storeId
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class BasketPurchasePlannerTest {
    @Test
    fun `should create purchase plan for single product and single store`() {
        val productId = productId()
        val storeId = storeId()

        val basket =
            Basket().apply {
                add(productId, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offersCatalog(
                        offer(productId, storeId, 100),
                    ),
                maxStores = StoreCountLimit.Unlimited,
            )

        assertThat(result)
            .isInstanceOfSatisfying(PurchasePlanningResult.Success::class.java) { success ->
                val plan = success.plans.single()
                val purchase = plan.storePlans.single()

                assertThat(plan.totalPrice).isEqualTo(money(100))
                assertThat(purchase.storeId).isEqualTo(storeId)
                assertThat(purchase.purchases)
                    .containsExactly(
                        PurchaseItem(productId, Quantity.ONE, money(100)),
                    )
            }
    }

    @Test
    fun `should choose cheapest store for product`() {
        val productId = productId()
        val storeId1 = storeId()
        val storeId2 = storeId()

        val basket =
            Basket().apply {
                add(productId, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offersCatalog(
                        offer(productId, storeId1, 100),
                        offer(productId, storeId2, 1),
                    ),
                maxStores = StoreCountLimit.Unlimited,
            )

        assertThat(result)
            .isInstanceOfSatisfying(PurchasePlanningResult.Success::class.java) { success ->
                assertThat(success.plans.map { it.totalPrice })
                    .containsExactly(
                        money(1),
                        money(100),
                    )
                assertThat(
                    success.plans
                        .first()
                        .storePlans
                        .first()
                        .storeId,
                ).isEqualTo(storeId2)
            }
    }

    @Test
    fun `should return offers from different stores`() {
        val productId1 = productId()
        val productId2 = productId()

        val storeId1 = storeId()
        val storeId2 = storeId()

        val basket =
            Basket().apply {
                add(productId1, Quantity.ONE)
                add(productId2, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offersCatalog(
                        offer(productId1, storeId1, 1),
                        offer(productId1, storeId2, 999),
                        offer(productId2, storeId1, 999),
                        offer(productId2, storeId2, 1),
                    ),
                maxStores = StoreCountLimit.Unlimited,
            )

        assertThat(result)
            .isInstanceOfSatisfying(
                PurchasePlanningResult.Success::class.java,
            ) { success ->
                assertThat(success.plans.first().totalPrice)
                    .isEqualTo(money(2))

                assertThat(
                    success.plans
                        .first()
                        .storePlans
                        .map { it.storeId },
                ).containsExactlyInAnyOrder(storeId1, storeId2)
            }
    }

    @Test
    fun `should return only one store`() {
        val productId1 = productId()
        val productId2 = productId()

        val storeId1 = storeId()
        val storeId2 = storeId()

        val basket =
            Basket().apply {
                add(productId1, Quantity.ONE)
                add(productId2, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offersCatalog(
                        offer(productId1, storeId1, 100),
                        offer(productId2, storeId1, 100),
                        offer(productId1, storeId2, 50),
                        offer(productId2, storeId2, 900),
                    ),
                maxStores = StoreCountLimit.Limited(1),
            )

        assertThat(result)
            .isInstanceOfSatisfying(PurchasePlanningResult.Success::class.java) { success ->
                val cheapestPlan = success.plans.first()
                val purchase = cheapestPlan.storePlans.single()

                assertThat(cheapestPlan.totalPrice).isEqualTo(money(200))
                assertThat(purchase.storeId).isEqualTo(storeId1)
            }
    }

    @Test
    fun `should fail when product not found in offers`() {
        val productId = productId()
        val basket =
            Basket().apply {
                add(productId, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers = offersCatalog(),
                maxStores = StoreCountLimit.Unlimited,
            )

        assertThat(result).isInstanceOf(PurchasePlanningResult.Impossible::class.java)
    }

    @Test
    fun `should fail when basket is empty`() {
        val basket = Basket()

        assertThatThrownBy {
            BasketPurchasePlanner.plan(
                basket = basket,
                offers = offersCatalog(),
                maxStores = StoreCountLimit.Unlimited,
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `should return all plans`() {
        val productId1 = productId()
        val productId2 = productId()

        val storeId1 = storeId()
        val storeId2 = storeId()
        val storeId3 = storeId()

        val basket =
            Basket().apply {
                add(productId1, Quantity.ONE)
                add(productId2, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offersCatalog(
                        offer(productId1, storeId1, 1),
                        offer(productId2, storeId1, 9),
                        offer(productId1, storeId2, 4),
                        offer(productId2, storeId2, 2),
                        offer(productId1, storeId3, 3),
                        offer(productId2, storeId3, 1),
                    ),
                maxStores = StoreCountLimit.Unlimited,
            )

        assertThat(result)
            .isInstanceOfSatisfying(
                PurchasePlanningResult.Success::class.java,
            ) { success ->
                assertThat(success.plans).hasSize(5)
            }
    }

    @Test
    fun `should consider quantities`() {
        val productId1 = productId()
        val productId2 = productId()

        val storeId1 = storeId()
        val storeId2 = storeId()

        val basket =
            Basket().apply {
                add(productId1, Quantity.ONE)
                add(productId2, Quantity(10))
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offersCatalog(
                        offer(productId1, storeId1, 2),
                        offer(productId2, storeId1, 5),
                        offer(productId1, storeId2, 5),
                        offer(productId2, storeId2, 3),
                    ),
                maxStores = StoreCountLimit.Limited(1),
            )

        assertThat(result)
            .isInstanceOfSatisfying(PurchasePlanningResult.Success::class.java) { success ->
                val cheapestPlan = success.plans.first()
                assertThat(cheapestPlan.totalPrice).isEqualTo(money(35))
            }
    }

    @Test
    fun `should return all plans with the same total price`() {
        val productId1 = productId()

        val storeId1 = storeId()
        val storeId2 = storeId()

        val basket =
            Basket().apply {
                add(productId1, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offersCatalog(
                        offer(productId1, storeId1, 1),
                        offer(productId1, storeId2, 1),
                    ),
                maxStores = StoreCountLimit.Unlimited,
            )

        assertThat(result)
            .isInstanceOfSatisfying(PurchasePlanningResult.Success::class.java) { success ->
                assertThat(success.plans.map { it.totalPrice }).containsExactly(
                    money(1),
                    money(1),
                )
            }
    }
}
