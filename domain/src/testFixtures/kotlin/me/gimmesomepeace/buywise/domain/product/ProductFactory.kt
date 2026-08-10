package me.gimmesomepeace.buywise.domain.product

import com.github.f4b6a3.uuid.UuidCreator

fun productId() = ProductId(UuidCreator.getTimeOrderedEpoch())

fun product(
    id: ProductId = productId(),
    name: String = "Nameless product",
) = Product(
    id = id,
    name = name,
)
