package me.gimmesomepeace.buywise.application.product.delete

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.product.productRepository
import me.gimmesomepeace.buywise.domain.product.ProductException
import me.gimmesomepeace.buywise.domain.product.product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DeleteProductUseCaseTest {
    @Test
    fun `should delete existing product`() =
        runTest {
            val product = product()
            val repository = productRepository(product)

            val useCase =
                DeleteProductUseCase(
                    productRepository = repository,
                )

            useCase.execute(
                productId = product.id,
            )

            val result =
                runCatching {
                    repository.delete(product.id)
                }

            assertThat(result.exceptionOrNull())
                .isInstanceOf(ProductException.NotFound::class.java)
        }
}
