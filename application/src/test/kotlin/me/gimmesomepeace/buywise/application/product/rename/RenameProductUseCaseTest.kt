package me.gimmesomepeace.buywise.application.product.rename

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.product.productRepository
import me.gimmesomepeace.buywise.domain.product.product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RenameProductUseCaseTest {
    @Test
    fun `should rename existing product`() =
        runTest {
            val product = product(name = "Milk")
            val repository = productRepository(product)

            val useCase =
                RenameProductUseCase(
                    productRepository = repository,
                )

            useCase.execute(
                productId = product.id,
                newName = "Apple",
            )

            val actual = repository.get(product.id)

            assertThat(actual.name)
                .isEqualTo("Apple")
        }
}
