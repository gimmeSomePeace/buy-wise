package me.gimmesomepeace.buywise.application.basket.change

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.basket.basketRepository
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.basket.getOrEmpty
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.qty
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ChangeBasketItemQuantityUseCaseTest {
    @Test
    fun `should change basket item quantity`() =
        runTest {
            val productId = productId()
            val repository =
                basketRepository(
                    basket {
                        add(productId, 3.qty())
                    },
                )

            val useCase = ChangeBasketItemQuantityUseCase(repository)

            useCase.execute(productId, 5.qty())
            assertThat(
                repository.getOrEmpty().quantityOf(productId),
            ).isEqualTo(5.qty())
        }

    @Test
    fun `should not fail when changing not existing items's quantity`() =
        runTest {
            val productId = productId()
            val repository = basketRepository()
            val useCase = ChangeBasketItemQuantityUseCase(repository)

            useCase.execute(productId, 5.qty())
            assertThat(
                repository.getOrEmpty().quantityOf(productId),
            ).isEqualTo(5.qty())
        }
}
