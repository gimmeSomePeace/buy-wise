package me.gimmesomepeace.buywise.planning.plan

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

class StorePurchasePlanTest {
    @Nested
    inner class Creation {
        @Test
        fun `should fail when purchases are empty`() {
            assertThatThrownBy {
                storePlan()
            }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `should fail when currencies are different`() {
            val productId1 = productId()
            val productId2 = productId()

            assertThatThrownBy {
                storePlan(
                    storeId = storeId(),
                    purchase(
                        productId = productId1,
                        unitPrice = 1.usd(),
                    ),
                    purchase(
                        productId = productId2,
                        unitPrice = 1.rub(),
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
                    purchase(productId),
                    purchase(productId),
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
                    purchase(productId1),
                    purchase(productId2),
                )

            assertThat(plan.purchases).hasSize(2)
        }
    }
}
