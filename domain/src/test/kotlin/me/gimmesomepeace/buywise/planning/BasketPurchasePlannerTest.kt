package me.gimmesomepeace.buywise.planning

import me.gimmesomepeace.buywise.domain.basket.Basket
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.planning.BasketPurchasePlanner
import me.gimmesomepeace.buywise.domain.planning.PurchasePlanningResult
import me.gimmesomepeace.buywise.domain.planning.StoreCountLimit
import me.gimmesomepeace.buywise.domain.planning.available
import me.gimmesomepeace.buywise.domain.planning.offerCatalog
import me.gimmesomepeace.buywise.domain.planning.purchase
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Quantity
import me.gimmesomepeace.buywise.domain.shared.qty
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.domain.store.storeId
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class BasketPurchasePlannerTest {
    @Test
    fun `should create purchase plan for single product and single store`() {
        val productId = productId()
        val storeId = storeId()

        val basket =
            basket {
                add(productId, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offerCatalog(
                        available(productId, storeId, 100.usd()),
                    ),
                maxStores = StoreCountLimit.Unlimited,
            )

        assertThat(result)
            .isInstanceOfSatisfying(
                PurchasePlanningResult.Success::class.java,
            ) { success ->
                val plan = success.plans.single()
                val purchase = plan.storePlans.single()

                assertThat(plan.totalPrice).isEqualTo(100.usd())
                assertThat(purchase.storeId).isEqualTo(storeId)
                assertThat(purchase.purchases)
                    .containsExactly(
                        purchase(productId, Quantity.ONE, 100.usd()),
                    )
            }
    }

    @Test
    fun `should choose cheapest store for product`() {
        val productId = productId()
        val storeId1 = storeId()
        val storeId2 = storeId()

        val basket =
            basket {
                add(productId, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offerCatalog(
                        available(productId, storeId1, 100.usd()),
                        available(productId, storeId2, 1.usd()),
                    ),
                maxStores = StoreCountLimit.Unlimited,
            )

        assertThat(result)
            .isInstanceOfSatisfying(
                PurchasePlanningResult.Success::class.java,
            ) { success ->
                assertThat(success.plans.map { it.totalPrice })
                    .containsExactly(
                        1.usd(),
                        100.usd(),
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
            basket {
                add(productId1, Quantity.ONE)
                add(productId2, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offerCatalog(
                        available(productId1, storeId1, 1.usd()),
                        available(productId1, storeId2, 999.usd()),
                        available(productId2, storeId1, 999.usd()),
                        available(productId2, storeId2, 1.usd()),
                    ),
                maxStores = StoreCountLimit.Unlimited,
            )

        assertThat(result)
            .isInstanceOfSatisfying(
                PurchasePlanningResult.Success::class.java,
            ) { success ->
                assertThat(success.plans.first().totalPrice)
                    .isEqualTo(2.usd())

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
            basket {
                add(productId1, Quantity.ONE)
                add(productId2, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offerCatalog(
                        available(productId1, storeId1, 100.usd()),
                        available(productId2, storeId1, 100.usd()),
                        available(productId1, storeId2, 50.usd()),
                        available(productId2, storeId2, 900.usd()),
                    ),
                maxStores = StoreCountLimit.Limited(1),
            )

        assertThat(result)
            .isInstanceOfSatisfying(
                PurchasePlanningResult.Success::class.java,
            ) { success ->
                val cheapestPlan = success.plans.first()
                val purchase = cheapestPlan.storePlans.single()

                assertThat(cheapestPlan.totalPrice).isEqualTo(200.usd())
                assertThat(purchase.storeId).isEqualTo(storeId1)
            }
    }

    @Test
    fun `should fail when product not found in offers`() {
        val productId = productId()
        val basket =
            basket {
                add(productId, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers = offerCatalog(),
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
                offers = offerCatalog(),
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
            basket {
                add(productId1, Quantity.ONE)
                add(productId2, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offerCatalog(
                        available(productId1, storeId1, 1.usd()),
                        available(productId2, storeId1, 9.usd()),
                        available(productId1, storeId2, 4.usd()),
                        available(productId2, storeId2, 2.usd()),
                        available(productId1, storeId3, 3.usd()),
                        available(productId2, storeId3, 1.usd()),
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
            basket {
                add(productId1, Quantity.ONE)
                add(productId2, 10.qty())
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offerCatalog(
                        available(productId1, storeId1, 2.usd()),
                        available(productId2, storeId1, 5.usd()),
                        available(productId1, storeId2, 5.usd()),
                        available(productId2, storeId2, 3.usd()),
                    ),
                maxStores = StoreCountLimit.Limited(1),
            )

        assertThat(result)
            .isInstanceOfSatisfying(
                PurchasePlanningResult.Success::class.java,
            ) { success ->
                val cheapestPlan = success.plans.first()
                assertThat(cheapestPlan.totalPrice).isEqualTo(35.usd())
            }
    }

    @Test
    fun `should return all plans with the same total price`() {
        val productId1 = productId()

        val storeId1 = storeId()
        val storeId2 = storeId()

        val basket =
            basket {
                add(productId1, Quantity.ONE)
            }

        val result =
            BasketPurchasePlanner.plan(
                basket = basket,
                offers =
                    offerCatalog(
                        available(productId1, storeId1, 1.usd()),
                        available(productId1, storeId2, 1.usd()),
                    ),
                maxStores = StoreCountLimit.Unlimited,
            )

        assertThat(result)
            .isInstanceOfSatisfying(PurchasePlanningResult.Success::class.java) { success ->
                assertThat(success.plans.map { it.totalPrice }).containsExactly(
                    1.usd(),
                    1.usd(),
                )
            }
    }
}
