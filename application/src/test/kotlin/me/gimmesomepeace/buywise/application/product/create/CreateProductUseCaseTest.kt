package me.gimmesomepeace.buywise.application.product.create

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.product.productRepository
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.user.userId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreateProductUseCaseTest {
    @Test
    fun `should create new product`() =
        runTest {
            val ownerId = userId()
            val productId = productId()
            val repository = productRepository()

            CreateProductUseCase(
                idGenerator = { productId },
                productRepository = repository,
            ).execute(
                ownerId = ownerId,
                productName = "Test product",
            )

            val product = repository.get(productId)
            assertThat(product.name).isEqualTo("Test product")
        }
}
