package me.gimmesomepeace.buywise.infrastructure.persistence.product

import me.gimmesomepeace.buywise.domain.product.Product
import me.gimmesomepeace.buywise.domain.product.ProductException
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.ProductRepository

class ProductRepositoryImpl(
    private val repository: ProductJpaRepository,
) : ProductRepository {
    override suspend fun get(productId: ProductId) =
        repository
            .findById(productId.value)
            .orElseThrow { ProductException.NotFound(productId) }
            .toDomain()

    override suspend fun add(product: Product) {
        if (repository.existsById(product.id.value)) {
            throw ProductException.AlreadyExists(product.id)
        }
        repository.save(product.toEntity())
    }

    override suspend fun update(product: Product) {
        if (!repository.existsById(product.id.value)) {
            throw ProductException.NotFound(product.id)
        }
        repository.save(product.toEntity())
    }

    override suspend fun delete(productId: ProductId) {
        if (!repository.existsById(productId.value)) {
            throw ProductException.NotFound(productId)
        }
        repository.deleteById(productId.value)
    }
}
