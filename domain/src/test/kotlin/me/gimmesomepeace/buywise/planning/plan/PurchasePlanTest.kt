package me.gimmesomepeace.buywise.planning.plan

import me.gimmesomepeace.buywise.domain.planning.plan
import me.gimmesomepeace.buywise.domain.planning.purchase
import me.gimmesomepeace.buywise.domain.planning.storePlan
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.rub
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.domain.store.storeId
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

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
                        purchase(
                            productId = productId,
                            unitPrice = 1.usd(),
                        ),
                    ),
                    storePlan(
                        storeId2,
                        purchase(
                            productId = productId,
                            unitPrice = 1.rub(),
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
                        purchase(productId),
                    ),
                    storePlan(
                        storeId2,
                        purchase(productId),
                    ),
                )

            assertThat(plan.storePlans).hasSize(2)
        }
    }
}
