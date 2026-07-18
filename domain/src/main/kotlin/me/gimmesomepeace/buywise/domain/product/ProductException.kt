package me.gimmesomepeace.buywise.domain.product

import me.gimmesomepeace.buywise.domain.shared.DomainException

sealed class ProductException(
    message: String,
) : DomainException(message) {
    class NotFound(
        productId: ProductId,
    ) : ProductException("Product with id $productId not found")

    class AlreadyExists(
        productId: ProductId,
    ) : ProductException("Product with id $productId already exists")
}
