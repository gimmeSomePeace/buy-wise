package me.gimmesomepeace.buywise.application.product.create

import me.gimmesomepeace.buywise.application.product.ProductDetails
import me.gimmesomepeace.buywise.application.product.toDetails
import me.gimmesomepeace.buywise.application.shared.IdGenerator
import me.gimmesomepeace.buywise.domain.product.Product
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.ProductRepository
import me.gimmesomepeace.buywise.domain.user.UserId

class CreateProductUseCase(
    private val idGenerator: IdGenerator<ProductId>,
    private val repository: ProductRepository,
) {
    suspend fun execute(
        ownerId: UserId,
        productName: String,
    ): ProductDetails {
        val id = idGenerator.generate()
        val product =
            Product(
                id,
                ownerId,
                productName,
            )

        repository.add(product)
        return product.toDetails()
    }
}
