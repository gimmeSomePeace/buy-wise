package me.gimmesomepeace.buywise.application.product

import me.gimmesomepeace.buywise.domain.product.Product
import me.gimmesomepeace.buywise.domain.product.ProductException
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.ProductRepository

class InMemoryProductRepository : ProductRepository {
    private val data =
        mutableMapOf<ProductId, Product>()

    override suspend fun get(productId: ProductId): Product =
        data[productId] ?: throw ProductException.NotFound(
            productId,
        )

    override suspend fun add(product: Product) {
        if (product.id in data) {
            throw ProductException.AlreadyExists(
                product.id,
            )
        }
        data[product.id] = product
    }

    override suspend fun update(product: Product) {
        if (product.id !in data) {
            throw ProductException.NotFound(
                product.id,
            )
        }
        data[product.id] = product
    }

    override suspend fun delete(productId: ProductId) {
        if (productId !in data) {
            throw ProductException.NotFound(
                productId,
            )
        }
        data.remove(productId)
    }
}
