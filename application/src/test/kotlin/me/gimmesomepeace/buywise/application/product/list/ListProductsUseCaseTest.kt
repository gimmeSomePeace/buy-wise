package me.gimmesomepeace.buywise.application.product.list

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.product.ProductListItem
import me.gimmesomepeace.buywise.application.product.ProductQuery
import me.gimmesomepeace.buywise.application.product.productListItem
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.user.userId
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ListProductsUseCaseTest {
    private val query = mockk<ProductQuery>()
    private val useCase = ListProductsUseCase(query)

    @Test
    fun `should return products for owner`() = runTest {
        val ownerId = userId()
        val page = Page(
            items = listOf(productListItem(ownerId = ownerId)),
            cursor = null,
        )

        coEvery { query.list(any(), any()) } returns page

        val result = useCase.execute(ownerId, PageRequest(pageSize = 20))

        assertThat(result.items).hasSize(1)
        assertThat(result.items.first().ownerId).isEqualTo(ownerId)
    }

    @Test
    fun `should return empty page when no products`() = runTest {
        val ownerId = userId()
        val emptyPage = Page<ProductListItem>(items = emptyList(), cursor = null)

        coEvery { query.list(any(), any()) } returns emptyPage

        val result = useCase.execute(ownerId, PageRequest(pageSize = 20))

        assertThat(result.items).isEmpty()
    }
}
