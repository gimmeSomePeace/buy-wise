package me.gimmesomepeace.buywise.application.product.list

import me.gimmesomepeace.buywise.application.product.ProductQuery
import me.gimmesomepeace.buywise.application.shared.PageRequest

class ListProductsUseCase(
    private val productQuery: ProductQuery,
) {
    suspend fun execute(request: PageRequest) = productQuery.list(request)
}
