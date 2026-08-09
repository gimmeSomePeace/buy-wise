package me.gimmesomepeace.buywise.infrastructure.persistence.product

import me.gimmesomepeace.buywise.domain.product.Product
import me.gimmesomepeace.buywise.domain.product.ProductId

internal fun ProductEntity.toDomain() = Product(
    id = ProductId(this.id),
    name = this.name,
)

internal fun Product.toEntity() = ProductEntity(
    id = this.id.value,
    name = this.name,
)
