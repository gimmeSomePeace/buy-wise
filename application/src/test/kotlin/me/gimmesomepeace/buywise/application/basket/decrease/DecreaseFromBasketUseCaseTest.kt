package me.gimmesomepeace.buywise.application.basket.decrease

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.basket.basketRepository
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.basket.getOrEmpty
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.qty
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DecreaseFromBasketUseCaseTest {
    @Test
    fun `should decrease product quantity`() =
        runTest {
            val productId = productId()
            val repository =
                basketRepository(
                    basket {
                        add(productId, 5.qty())
                    },
                )

            DecreaseFromBasketUseCase(
                basketRepository = repository,
            ).execute(
                productId = productId,
                quantity = 2.qty(),
            )

            assertThat(repository.getOrEmpty().quantityOf(productId))
                .isEqualTo(3.qty())
        }
}
