package me.gimmesomepeace.buywise.infrastructure.persistence.product

import me.gimmesomepeace.buywise.domain.product.Product
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.user.UserId

internal fun ProductEntity.toDomain() =
    Product(
        id = ProductId(this.id),
        ownerId = UserId(ownerId),
        name = this.name,
    )

internal fun Product.toEntity() =
    ProductEntity(
        id = this.id.value,
        ownerId = ownerId.value,
        name = this.name,
    )
