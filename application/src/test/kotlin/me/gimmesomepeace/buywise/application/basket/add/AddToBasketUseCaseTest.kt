package me.gimmesomepeace.buywise.application.basket.add

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.basket.InMemoryBasketRepository
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.basket.getOrEmpty
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Quantity
import me.gimmesomepeace.buywise.domain.shared.qty
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AddToBasketUseCaseTest {
    @Test
    fun `should add to basket`() =
        runTest {
            val repository = InMemoryBasketRepository()
            val useCase =
                AddToBasketUseCase(
                    basketRepository = repository,
                )

            val productId = productId()
            useCase.execute(
                productId = productId,
                quantity = Quantity.ONE,
            )

            val basket = repository.getOrEmpty()
            assertThat(basket.quantityOf(productId))
                .isEqualTo(Quantity.ONE)
        }

    @Test
    fun `should add to existing basket`() =
        runTest {
            val productId = productId()
            val repository = InMemoryBasketRepository()
            val basket =
                basket {
                    add(productId, 5.qty())
                }
            repository.save(basket)

            val useCase =
                AddToBasketUseCase(
                    basketRepository = repository,
                )

            useCase.execute(
                productId,
                5.qty(),
            )

            assertThat(repository.getOrEmpty().quantityOf(productId))
                .isEqualTo(10.qty())
        }
}
