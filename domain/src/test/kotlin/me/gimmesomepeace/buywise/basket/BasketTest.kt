package me.gimmesomepeace.buywise.basket

import me.gimmesomepeace.buywise.product.ProductId
import me.gimmesomepeace.buywise.shared.Quantity
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID

class BasketTest {
    @Nested
    inner class Add {
        @Test
        fun `should add product`() {
            val productId = ProductId(UUID.randomUUID())
            val basket = Basket()

            basket.add(
                productId,
                Quantity.ONE,
            )

            assertThat(basket.quantityOf(productId)).isEqualTo(Quantity.ONE)
        }

        @Test
        fun `should sum product quantities when adding existing product`() {
            val productId = ProductId(UUID.randomUUID())
            val basket = Basket()

            basket.add(productId, Quantity(1))
            basket.add(productId, Quantity(1))

            assertThat(basket.quantityOf(productId))
                .isEqualTo(Quantity(2))
        }

        @Test
        fun `should not create duplicate items when adding existing product`() {
            val productId = ProductId(UUID.randomUUID())
            val basket = Basket()

            basket.add(productId, Quantity(1))
            basket.add(productId, Quantity(2))

            assertThat(basket.items())
                .containsExactly(
                    BasketItem(productId, Quantity(3)),
                )
        }
    }

    @Nested
    inner class QuantityOf {
        @Test
        fun `should return quantity of existing product`() {
            val productId = ProductId(UUID.randomUUID())
            val basket = Basket()

            basket.add(productId, Quantity(3))

            assertThat(basket.quantityOf(productId)).isEqualTo(Quantity(3))
        }

        @Test
        fun `should return zero quantity when product does not exist`() {
            val basket = Basket()

            assertThat(basket.quantityOf(ProductId(UUID.randomUUID())))
                .isEqualTo(Quantity.ZERO)
        }
    }

    @Nested
    inner class Decrease {
        @Test
        fun `should decrease quantity of existing product`() {
            val productId = ProductId(UUID.randomUUID())
            val basket = Basket()

            basket.add(productId, Quantity(5))
            basket.decrease(productId, Quantity(2))

            assertThat(basket.quantityOf(productId))
                .isEqualTo(Quantity(3))
        }

        @Test
        fun `should remove product when decrease below zero`() {
            val productId = ProductId(UUID.randomUUID())
            val basket = Basket()

            basket.add(productId, Quantity(5))
            basket.decrease(productId, Quantity(99999))

            assertThat(basket.quantityOf(productId)).isEqualTo(Quantity.ZERO)
        }

        @Test
        fun `should throw exception when product does not exist`() {
            val productId = ProductId(UUID.randomUUID())
            val basket = Basket()

            assertThatThrownBy { basket.decrease(productId, Quantity(99999)) }
                .isInstanceOf(BasketException.ProductNotInBasket::class.java)
        }

        @Test
        fun `should throw exception when decrease by zero`() {
            val productId = ProductId(UUID.randomUUID())
            val basket = Basket()

            assertThatThrownBy { basket.decrease(productId, Quantity.ZERO) }
                .isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Nested
    inner class Remove {
        @Test
        fun `should remove product`() {
            val productId = ProductId(UUID.randomUUID())
            val basket = Basket()

            basket.add(productId, Quantity.ONE)
            basket.remove(productId)

            assertThat(basket.quantityOf(productId))
                .isEqualTo(Quantity.ZERO)
        }

        @Test
        fun `should not fall when product does not exist`() {
            val productId = ProductId(UUID.randomUUID())
            val basket = Basket()

            assertThatCode {
                basket.remove(productId)
            }.doesNotThrowAnyException()
        }
    }

    @Nested
    inner class Clear {
        @Test
        fun `should clear basket`() {
            val productId1 = ProductId(UUID.randomUUID())
            val productId2 = ProductId(UUID.randomUUID())
            val basket = Basket()

            basket.add(productId1, Quantity.ONE)
            basket.add(productId2, Quantity.ONE)
            basket.clear()

            assertThat(basket.items()).isEmpty()
        }
    }

    @Nested
    inner class IsEmpty {
        @Test
        fun `should return false when basket contains products`() {
            val productId1 = ProductId(UUID.randomUUID())
            val basket = Basket()

            basket.add(productId1, Quantity.ONE)

            assertThat(basket.isEmpty()).isFalse()
        }

        @Test
        fun `should return true when basket is empty`() {
            val basket = Basket()

            assertThat(basket.isEmpty()).isTrue()
        }
    }
}
