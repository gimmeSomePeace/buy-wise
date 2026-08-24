package me.gimmesomepeace.buywise.application.product

import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.product.ProductId

interface ProductQuery {
    suspend fun find(id: ProductId): ProductDetails?

    suspend fun list(
        request: PageRequest,
        filters: ProductFilters =
            ProductFilters(),
    ): Page<ProductListItem>
}
