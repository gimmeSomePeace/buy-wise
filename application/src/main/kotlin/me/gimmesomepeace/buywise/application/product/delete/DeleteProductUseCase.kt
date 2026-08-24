package me.gimmesomepeace.buywise.application.product.delete

import me.gimmesomepeace.buywise.domain.product.ProductException
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.ProductRepository
import me.gimmesomepeace.buywise.domain.user.UserId

class DeleteProductUseCase(
    private val productRepository: ProductRepository,
) {
    suspend fun execute(
        userId: UserId,
        productId: ProductId,
    ) {
        val product =
            productRepository.get(
                productId,
            )
        if (product.ownerId != userId) {
            throw ProductException.NotFound(
                productId,
            )
        }
        productRepository.delete(productId)
    }
}
