package me.gimmesomepeace.buywise.planning.plan

import me.gimmesomepeace.buywise.plan
import me.gimmesomepeace.buywise.productId
import me.gimmesomepeace.buywise.purchase
import me.gimmesomepeace.buywise.shared.Currency
import me.gimmesomepeace.buywise.shared.Money
import me.gimmesomepeace.buywise.shared.Quantity
import me.gimmesomepeace.buywise.storeId
import me.gimmesomepeace.buywise.storePlan
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PurchasePlanTest {
    @Nested
    inner class Creation {
        @Test
        fun `should fail when store plans are empty`() {
            assertThatThrownBy {
                plan()
            }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `should fail when currencies are different`() {
            val storeId1 = storeId()
            val storeId2 = storeId()

            val productId = productId()

            assertThatThrownBy {
                plan(
                    storePlan(
                        storeId1,
                        PurchaseItem(
                            productId,
                            Quantity(1),
                            Money(BigDecimal.ONE, Currency.EUR),
                        ),
                    ),
                    storePlan(
                        storeId2,
                        PurchaseItem(
                            productId,
                            Quantity(1),
                            Money(BigDecimal.ONE, Currency.RUB),
                        ),
                    ),
                )
            }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `should fail when store are duplicated`() {
            val storeId = storeId()

            assertThatThrownBy {
                plan(
                    storePlan(storeId),
                    storePlan(storeId),
                )
            }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `should create with all store plans`() {
            val storeId1 = storeId()
            val storeId2 = storeId()
            val productId = productId()

            val plan =
                plan(
                    storePlan(
                        storeId1,
                        purchase(productId, 1, 1),
                    ),
                    storePlan(
                        storeId2,
                        purchase(productId, 1, 1),
                    ),
                )

            assertThat(plan.storePlans).hasSize(2)
        }
    }
}
