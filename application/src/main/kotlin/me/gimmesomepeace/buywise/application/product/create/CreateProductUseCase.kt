package me.gimmesomepeace.buywise.application.product.create

import me.gimmesomepeace.buywise.application.shared.IdGenerator
import me.gimmesomepeace.buywise.domain.product.Product
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.ProductRepository

class CreateProductUseCase(
    private val idGenerator: IdGenerator<ProductId>,
    private val productRepository: ProductRepository,
) {
    suspend fun execute(productName: String) : Product {
        val id = idGenerator.generate()
        val product =
            Product(
                id,
                productName,
            )

        productRepository.add(product)
        return product
    }
}
