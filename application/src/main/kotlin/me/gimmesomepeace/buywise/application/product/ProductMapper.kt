package me.gimmesomepeace.buywise.application.product

import me.gimmesomepeace.buywise.domain.product.Product

fun Product.toDetails() =
    ProductDetails(
        id = this.id,
        ownerId = this.ownerId,
        name = this.name,
    )
