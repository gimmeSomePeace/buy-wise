package me.gimmesomepeace.buywise.application.product.rename

import io.mockk.*
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.product.ProductException
import me.gimmesomepeace.buywise.domain.product.ProductRepository
import me.gimmesomepeace.buywise.domain.product.product
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.user.userId
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class RenameProductUseCaseTest {
    private val repository = mockk<ProductRepository>()
    private val useCase = RenameProductUseCase(repository)

    @Test
    fun `should rename product when it exists and belongs to user`() = runTest {
        val ownerId = userId()
        val productId = productId()
        val product = product(id = productId, ownerId = ownerId, name = "Old Name")
        val newName = "New Name"

        coEvery { repository.get(productId) } returns product
        coEvery { repository.update(any()) } just runs

        useCase.execute(ownerId, productId, newName)

        coVerify(exactly = 1) {
            repository.update(match { it.name == newName })
        }
    }

    @Test
    fun `should throw NotFound when product belongs to another user`() = runTest {
        val ownerId = userId()
        val productId = productId()
        val anotherUserId = userId()

        coEvery { repository.get(productId) } returns product(id = productId, ownerId = anotherUserId)

        assertFailsWith<ProductException.NotFound> {
            useCase.execute(ownerId, productId, "New Name")
        }

        coVerify(exactly = 0) { repository.update(any()) }
    }

    @Test
    fun `should throw NotFound when product not found`() = runTest {
        val productId = productId()
        coEvery { repository.get(productId) } throws ProductException.NotFound(productId)

        assertFailsWith<ProductException.NotFound> {
            useCase.execute(userId(), productId, "New Name")
        }

        coVerify(exactly = 0) { repository.update(any()) }
    }
}
