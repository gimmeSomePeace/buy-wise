package me.gimmesomepeace.buywise.application.product.create

import io.mockk.*
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.shared.IdGenerator
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.ProductRepository
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.user.userId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreateProductUseCaseTest {
    private val idGenerator = mockk<IdGenerator<ProductId>>()
    private val repository = mockk<ProductRepository>()
    private val useCase = CreateProductUseCase(idGenerator, repository)

    @Test
    fun `should create product with correct owner`() = runTest {
        val ownerId = userId()
        val name = "New Product"

        coEvery { idGenerator.generate() } returns productId()
        coEvery { repository.add(any()) } just runs

        val result = useCase.execute(ownerId, name)

        assertThat(result.ownerId).isEqualTo(ownerId)
        assertThat(result.name).isEqualTo(name)

        coVerify(exactly = 1) {
            repository.add(match { it.ownerId == ownerId })
        }
    }
}
