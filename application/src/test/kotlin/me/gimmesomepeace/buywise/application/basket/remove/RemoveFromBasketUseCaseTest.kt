package me.gimmesomepeace.buywise.application.basket.remove

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.basket.basketRepository
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.basket.getOrEmpty
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Quantity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RemoveFromBasketUseCaseTest {
    @Test
    fun `should remove existing product`() =
        runTest {
            val productId = productId()

            val repository =
                basketRepository(
                    basket {
                        add(productId, Quantity.ONE)
                    },
                )
            RemoveFromBasketUseCase(repository)
                .execute(productId = productId)

            assertThat(repository.getOrEmpty().quantityOf(productId))
                .isEqualTo(Quantity.ZERO)
        }
}
