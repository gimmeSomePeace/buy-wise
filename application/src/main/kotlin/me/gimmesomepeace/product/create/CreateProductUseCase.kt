package me.gimmesomepeace.product.create

import me.gimmesomepeace.IdGenerator
import me.gimmesomepeace.buywise.product.Product
import me.gimmesomepeace.buywise.product.ProductId
import me.gimmesomepeace.buywise.product.ProductRepository

class CreateProductUseCase(
    private val idGenerator: IdGenerator<ProductId>,
    private val productRepository: ProductRepository,
) {
    suspend fun execute(command: CreateProductCommand) {
        val id = idGenerator.generate()
        val product = Product(
            id,
            command.productName
        )

        productRepository.add(product)
    }
}
