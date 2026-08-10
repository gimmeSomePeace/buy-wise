package me.gimmesomepeace.buywise.application.basket.add

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.basket.InMemoryBasketRepository
import me.gimmesomepeace.buywise.domain.basket.getOrEmpty
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.qty
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AddProductToBasketUseCaseTest {
    @Test
    fun `should add product to basket`() =
        runTest {
            val repository = InMemoryBasketRepository()
            val useCase = AddProductToBasketUseCase(repository)

            val productId = productId()
            useCase.execute(productId, 3.qty())

            assertThat(
                repository.getOrEmpty().quantityOf(productId),
            ).isEqualTo(3.qty())
        }
}
