package me.gimmesomepeace.buywise.web.product

import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.domain.product.Product
import me.gimmesomepeace.buywise.web.product.list.ListProductsResponse

internal fun Product.toDetailsResponse() =
    ProductDetailsResponse(
        id = id.value,
        name = name,
    )

internal fun Page<Product>.toListProductsResponse() =
    ListProductsResponse(
        products = this.items,
        nextPageToken = this.cursor?.value,
    )
