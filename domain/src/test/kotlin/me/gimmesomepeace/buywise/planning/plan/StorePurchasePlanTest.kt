package me.gimmesomepeace.buywise.planning.plan

import me.gimmesomepeace.buywise.money
import me.gimmesomepeace.buywise.productId
import me.gimmesomepeace.buywise.purchase
import me.gimmesomepeace.buywise.shared.Currency
import me.gimmesomepeace.buywise.shared.Quantity
import me.gimmesomepeace.buywise.storeId
import me.gimmesomepeace.buywise.storePlan
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class StorePurchasePlanTest {
    @Nested
    inner class Creation {
        @Test
        fun `should fail when purchases are empty`() {
            assertThatThrownBy {
                storePlan(storeId())
            }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `should fail when currencies are different`() {
            val storeId = storeId()
            val productId1 = productId()
            val productId2 = productId()

            assertThatThrownBy {
                storePlan(
                    storeId,
                    PurchaseItem(
                        productId1,
                        Quantity(1),
                        money(1, Currency.USD),
                    ),
                    PurchaseItem(
                        productId2,
                        Quantity(1),
                        money(1, Currency.RUB),
                    ),
                )
            }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `should fail when products are duplicated`() {
            val productId = productId()

            assertThatThrownBy {
                storePlan(
                    storeId(),
                    purchase(productId, 1, 1),
                    purchase(productId, 2, 2),
                )
            }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `should create with all purchases`() {
            val productId1 = productId()
            val productId2 = productId()

            val plan =
                storePlan(
                    storeId(),
                    purchase(productId1, 1, 1),
                    purchase(productId2, 1, 1),
                )

            assertThat(plan.purchases).hasSize(2)
        }
    }
}
