package me.gimmesomepeace.buywise.application.product.create

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.product.productRepository
import me.gimmesomepeace.buywise.domain.product.productId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreateProductUseCaseTest {
    @Test
    fun `should create new product`() =
        runTest {
            val productId = productId()
            val repository = productRepository()

            CreateProductUseCase(
                idGenerator = { productId },
                productRepository = repository,
            ).execute(
                productName = "Test product",
            )

            val product = repository.get(productId)
            assertThat(product.name).isEqualTo("Test product")
        }
}
