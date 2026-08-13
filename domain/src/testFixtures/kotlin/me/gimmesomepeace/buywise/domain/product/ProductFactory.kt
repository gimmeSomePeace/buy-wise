package me.gimmesomepeace.buywise.domain.product

import com.github.f4b6a3.uuid.UuidCreator
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.userId

fun productId() = ProductId(UuidCreator.getTimeOrderedEpoch())

fun product(
    id: ProductId = productId(),
    ownerId: UserId = userId(),
    name: String = "Nameless product",
) = Product(
    id = id,
    ownerId = ownerId,
    name = name,
)
