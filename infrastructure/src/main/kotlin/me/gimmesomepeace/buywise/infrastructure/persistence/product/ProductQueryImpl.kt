package me.gimmesomepeace.buywise.infrastructure.persistence.product

import me.gimmesomepeace.buywise.application.product.ProductDetails
import me.gimmesomepeace.buywise.application.product.ProductFilters
import me.gimmesomepeace.buywise.application.product.ProductListItem
import me.gimmesomepeace.buywise.application.product.ProductQuery
import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.infrastructure.persistence.product.specification.ProductSpecifications
import me.gimmesomepeace.buywise.infrastructure.persistence.product.specification.toSpecification
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull

class ProductQueryImpl(
    private val repository: ProductJpaRepository,
) : ProductQuery {
    override suspend fun find(
        id: ProductId,
    ): ProductDetails? = repository.findByIdOrNull(id.value)?.toDetails()

    override suspend fun list(
        request: PageRequest,
        filters: ProductFilters,
    ): Page<ProductListItem> {
        val spec = listOfNotNull(
            filters.toSpecification(),
            request.cursor?.let { ProductSpecifications.afterCursor(it) },
        ).reduceOrNull { acc, spec -> acc.and(spec) } ?: Specification.unrestricted()

        val requestWithExtra = Pageable.ofSize(request.pageSize + 1)
        val entities = repository.findAll(spec, requestWithExtra).content

        val hasExtra = entities.size > request.pageSize
        val pageItems = if (hasExtra) entities.dropLast(1) else entities

        return Page(
            items = pageItems.map { it.toListItem() },
            cursor =
                if (hasExtra) Cursor(pageItems.last().id.toString())
                else null
        )
    }
}
