package me.gimmesomepeace.buywise.application.product.rename

import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.ProductRepository

class RenameProductUseCase(
    private val productRepository: ProductRepository,
) {
    suspend fun execute(
        productId: ProductId,
        newName: String,
    ) {
        val product = productRepository.get(productId)
        product.rename(newName)
        productRepository.update(product)
    }
}
