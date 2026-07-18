package me.gimmesomepeace.buywise.application.product.delete

import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.ProductRepository

class DeleteProductUseCase(
    private val productRepository: ProductRepository,
) {
    suspend fun execute(productId: ProductId) {
        productRepository.delete(productId)
    }
}
