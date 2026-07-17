package me.gimmesomepeace.product.delete

import me.gimmesomepeace.buywise.product.ProductRepository

class DeleteProductUseCase(
    private val productRepository: ProductRepository,
) {
    suspend fun execute(command: DeleteProductCommand) {
        productRepository.delete(command.productId)
    }
}
