package me.gimmesomepeace.buywise.application.product.get

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.product.ProductQuery
import me.gimmesomepeace.buywise.application.product.productDetails
import me.gimmesomepeace.buywise.domain.product.ProductException
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.user.userId
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlin.test.assertFailsWith

class GetProductUseCaseTest {
    private val query = mockk<ProductQuery>()
    private val useCase = GetProductUseCase(query)

    @Test
    fun `should return product by id`() =
        runTest {
            val productId = productId()
            val userId = userId()
            val expected = productDetails(id = productId, ownerId = userId)
            coEvery { query.find(productId) } returns expected

            val actual = useCase.execute(userId, productId)
            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected)
        }

    @Test
    fun `should throw NotFound when product belongs to another user`() =
        runTest {
            val productId = productId()
            val ownerId = userId()
            coEvery { query.find(productId) } returns
                productDetails(id = productId, ownerId = userId())

            assertFailsWith<ProductException.NotFound> {
                useCase.execute(ownerId, productId)
            }
        }

    @Test
    fun `should throw NotFound when product not found`() =
        runTest {
            val productId = productId()
            coEvery { query.find(productId) } returns null

            assertFailsWith<ProductException.NotFound> {
                useCase.execute(userId(), productId)
            }
        }
}
