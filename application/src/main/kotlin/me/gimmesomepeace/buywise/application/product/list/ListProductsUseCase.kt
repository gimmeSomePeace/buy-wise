package me.gimmesomepeace.buywise.application.product.list

import me.gimmesomepeace.buywise.application.product.ProductFilters
import me.gimmesomepeace.buywise.application.product.ProductQuery
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.user.UserId

class ListProductsUseCase(
    private val productQuery: ProductQuery,
) {
    suspend fun execute(
        ownerId: UserId,
        request: PageRequest,
    ) = productQuery.list(
        request,
        ProductFilters(ownerId = ownerId),
    )
}
