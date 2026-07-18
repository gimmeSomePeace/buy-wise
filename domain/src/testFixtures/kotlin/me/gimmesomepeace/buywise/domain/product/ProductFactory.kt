package me.gimmesomepeace.buywise.domain.product

import java.util.UUID

fun productId() = ProductId(UUID.randomUUID())

fun product(
    id: ProductId = productId(),
    name: String = "Nameless product",
) = Product(
    id = id,
    name = name,
)
