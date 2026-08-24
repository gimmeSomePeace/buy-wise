package me.gimmesomepeace.buywise.infrastructure.persistence.product

import me.gimmesomepeace.buywise.domain.product.Product
import me.gimmesomepeace.buywise.domain.product.ProductException
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val repository: ProductJpaRepository,
) : ProductRepository {
    override suspend fun get(productId: ProductId): Product {
        val entity =
            repository.findByIdOrNull(
                productId.value,
            )
                ?: throw ProductException
                    .NotFound(
                        productId,
                    )
        return entity.toDomain()
    }

    override suspend fun add(product: Product) {
        if (repository.existsById(
                product.id.value,
            )
        ) {
            throw ProductException.AlreadyExists(
                product.id,
            )
        }

        repository.save(product.toEntity())
    }

    override suspend fun update(product: Product) {
        if (!repository.existsById(
                product.id.value,
            )
        ) {
            throw ProductException.NotFound(
                product.id,
            )
        }

        repository.save(product.toEntity())
    }

    override suspend fun delete(productId: ProductId) {
        if (!repository.existsById(
                productId.value,
            )
        ) {
            throw ProductException.NotFound(
                productId,
            )
        }

        repository.deleteById(productId.value)
    }
}
