package me.gimmesomepeace.buywise.basket

import me.gimmesomepeace.buywise.domain.basket.Basket
import me.gimmesomepeace.buywise.domain.basket.BasketException
import me.gimmesomepeace.buywise.domain.basket.BasketItem
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Quantity
import me.gimmesomepeace.buywise.domain.shared.qty
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class BasketTest {
    @Nested
    inner class Add {
        @Test
        fun `should add product`() {
            val productId = productId()
            val basket =
                basket {
                    add(productId, Quantity.ONE)
                }

            assertThat(basket.quantityOf(productId)).isEqualTo(Quantity.ONE)
        }

        @Test
        fun `should sum product quantities when adding existing product`() {
            val productId = productId()
            val basket =
                basket {
                    add(productId, Quantity.ONE)
                    add(productId, Quantity.ONE)
                }

            assertThat(basket.quantityOf(productId))
                .isEqualTo(2.qty())
        }

        @Test
        fun `should not create duplicate items when adding existing product`() {
            val productId = productId()
            val basket =
                basket {
                    add(productId, Quantity.ONE)
                    add(productId, 2.qty())
                }

            assertThat(basket.items())
                .containsExactly(
                    BasketItem(productId, 3.qty()),
                )
        }
    }

    @Nested
    inner class QuantityOf {
        @Test
        fun `should return quantity of existing product`() {
            val productId = productId()
            val basket =
                basket {
                    add(productId, 3.qty())
                }

            assertThat(basket.quantityOf(productId)).isEqualTo(3.qty())
        }

        @Test
        fun `should return zero quantity when product does not exist`() {
            val basket = Basket()

            assertThat(basket.quantityOf(productId()))
                .isEqualTo(Quantity.ZERO)
        }
    }

    @Nested
    inner class Decrease {
        @Test
        fun `should decrease quantity of existing product`() {
            val productId = productId()
            val basket =
                basket {
                    add(productId, 5.qty())
                    decrease(productId, 2.qty())
                }

            assertThat(basket.quantityOf(productId))
                .isEqualTo(3.qty())
        }

        @Test
        fun `should remove product when decrease below zero`() {
            val productId = productId()
            val basket =
                basket {
                    add(productId, 5.qty())
                    decrease(productId, 999.qty())
                }

            assertThat(basket.quantityOf(productId)).isEqualTo(Quantity.ZERO)
        }

        @Test
        fun `should throw exception when product does not exist`() {
            val productId = productId()
            val basket = basket()

            assertThatThrownBy {
                basket.decrease(
                    productId,
                    9999.qty(),
                )
            }.isInstanceOf(BasketException.ProductNotInBasket::class.java)
        }

        @Test
        fun `should throw exception when decrease by zero`() {
            val productId = productId()
            val basket = basket()

            assertThatThrownBy {
                basket.decrease(
                    productId,
                    Quantity.ZERO,
                )
            }.isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Nested
    inner class Remove {
        @Test
        fun `should remove product`() {
            val productId = productId()
            val basket =
                basket {
                    add(productId, Quantity.ONE)
                    remove(productId)
                }

            assertThat(basket.quantityOf(productId))
                .isEqualTo(Quantity.ZERO)
        }

        @Test
        fun `should fall when product does not exist`() {
            val productId = productId()
            val basket = basket()

            assertThatThrownBy {
                basket.remove(productId)
            }.isInstanceOf(BasketException.ProductNotInBasket::class.java)
        }
    }

    @Nested
    inner class Clear {
        @Test
        fun `should clear basket`() {
            val basket =
                basket {
                    add(productId(), Quantity.ONE)
                    add(productId(), Quantity.ONE)
                    clear()
                }

            assertThat(basket.items()).isEmpty()
        }
    }

    @Nested
    inner class IsEmpty {
        @Test
        fun `should return false when basket contains products`() {
            val productId1 = productId()
            val basket =
                basket {
                    add(productId1, Quantity.ONE)
                }

            assertThat(basket.isEmpty()).isFalse()
        }

        @Test
        fun `should return true when basket is empty`() {
            val basket = basket()
            assertThat(basket.isEmpty()).isTrue()
        }
    }
}
