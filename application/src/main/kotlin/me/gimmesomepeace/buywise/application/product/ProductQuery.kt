package me.gimmesomepeace.buywise.application.product

import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.user.UserId

interface ProductQuery {
    suspend fun find(
        id: ProductId,
    ): ProductDetails?

    suspend fun list(
        ownerId: UserId,
        request: PageRequest,
    ): Page<ProductListItem>
}
