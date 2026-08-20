package me.gimmesomepeace.buywise.application.product.rename

import me.gimmesomepeace.buywise.domain.product.ProductException
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.ProductRepository
import me.gimmesomepeace.buywise.domain.user.UserId

class RenameProductUseCase(
    private val productRepository: ProductRepository,
) {
    suspend fun execute(
        userId: UserId,
        productId: ProductId,
        newName: String,
    ) {
        val product = productRepository.get(productId)
        if (product.ownerId != userId)
            throw ProductException.NotFound(productId)
        product.rename(newName)
        productRepository.update(product)
    }
}
