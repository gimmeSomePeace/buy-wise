package me.gimmesomepeace.buywise.offer

import me.gimmesomepeace.buywise.offer
import me.gimmesomepeace.buywise.offersCatalog
import me.gimmesomepeace.buywise.productId
import me.gimmesomepeace.buywise.storeId
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class OffersCatalogTest {
    @Nested
    inner class Initialization {
        @Test
        fun `should fail when duplicate offers exist`() {
            val productId1 = productId()
            val storeId1 = storeId()

            assertThatThrownBy {
                OffersCatalog(
                    listOf(
                        offer(productId1, storeId1, 1),
                        offer(productId1, storeId1, 2),
                    ),
                )
            }.isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Nested
    inner class Stores {
        @Test
        fun `should return all store ids`() {
            val productId1 = productId()

            val storeId1 = storeId()
            val storeId2 = storeId()
            val catalog =
                offersCatalog(
                    offer(productId1, storeId1, 1),
                    offer(productId1, storeId2, 1),
                )

            assertThat(catalog.stores().size).isEqualTo(2)
        }
    }

    @Nested
    inner class OfferOf {
        @Test
        fun `should return existing offer`() {
            val productId = productId()
            val storeId = storeId()

            val catalog =
                offersCatalog(
                    offer(productId, storeId, 1),
                )

            assertThat(catalog.of(productId, storeId))
                .isEqualTo(offer(productId, storeId, 1))
        }

        @Test
        fun `should return null when offer does not exist`() {
            val productId = productId()
            val storeId = storeId()

            val catalog = offersCatalog()
            assertThat(catalog.of(productId, storeId)).isNull()
        }
    }

    @Nested
    inner class OffersFor {
        @Test
        fun `should return all existing offers for product`() {
            val productId1 = productId()
            val productId2 = productId()

            val storeId1 = storeId()
            val storeId2 = storeId()

            val catalog =
                offersCatalog(
                    offer(productId1, storeId1, 1),
                    offer(productId1, storeId2, 2),
                    offer(productId2, storeId2, 3),
                )

            assertThat(catalog.forProduct(productId1)).hasSize(2)
        }
    }
}
