package me.gimmesomepeace.buywise.application.product.get

import me.gimmesomepeace.buywise.application.product.ProductDetails
import me.gimmesomepeace.buywise.application.product.ProductQuery
import me.gimmesomepeace.buywise.domain.product.ProductException
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.user.UserId

class GetProductUseCase(
    private val query: ProductQuery,
) {
    suspend fun execute(
        userId: UserId,
        productId: ProductId,
    ) : ProductDetails {
        val product  = query.find(productId) ?: throw ProductException.NotFound(productId)
        if (product.ownerId != userId)
            throw ProductException.NotFound(productId)
        return product
    }
}
