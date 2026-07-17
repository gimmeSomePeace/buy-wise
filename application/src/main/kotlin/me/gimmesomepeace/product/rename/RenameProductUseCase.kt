package me.gimmesomepeace.product.rename

import me.gimmesomepeace.buywise.product.ProductRepository

class RenameProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend fun execute(command: RenameProductCommand) {
        val product = productRepository.get(command.productId)

        product.rename(command.newName)

        productRepository.update(product)
    }
}