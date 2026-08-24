package me.gimmesomepeace.buywise.application.product.delete

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.product.ProductException
import me.gimmesomepeace.buywise.domain.product.ProductRepository
import me.gimmesomepeace.buywise.domain.product.product
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.user.userId
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class DeleteProductUseCaseTest {
    private val repository = mockk<ProductRepository>()
    private val useCase = DeleteProductUseCase(repository)

    @Test
    fun `should delete existing product`() =
        runTest {
            val ownerId = userId()
            val productId = productId()
            val product = product(id = productId, ownerId = ownerId)

            coEvery { repository.get(productId) } returns product
            coEvery { repository.delete(productId) } just runs

            useCase.execute(ownerId, productId)

            coVerify(exactly = 1) { repository.delete(productId) }
        }

    @Test
    fun `should throw NotFound when product belongs to another user`() =
        runTest {
            val productId = productId()
            val ownerId = userId()
            coEvery { repository.get(productId) } returns
                product(id = productId, ownerId = userId())

            assertFailsWith<ProductException.NotFound> {
                useCase.execute(ownerId, productId)
            }
        }
}
