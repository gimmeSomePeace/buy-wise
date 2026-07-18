package me.gimmesomepeace.buywise.application.basket.clear

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.basket.InMemoryBasketRepository
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.basket.getOrEmpty
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Quantity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ClearBasketUseCaseTest {
    @Test
    fun `should clear basket`() =
        runTest {
            val repository = InMemoryBasketRepository()
            val clearBasketUseCase =
                ClearBasketUseCase(
                    basketRepository = repository,
                )

            repository.save(
                basket {
                    add(productId(), Quantity.ONE)
                },
            )
            clearBasketUseCase.execute()

            assertThat(repository.getOrEmpty().isEmpty()).isTrue()
        }
}
