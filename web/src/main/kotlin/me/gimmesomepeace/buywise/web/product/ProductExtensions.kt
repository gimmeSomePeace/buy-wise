package me.gimmesomepeace.buywise.web.product

import me.gimmesomepeace.buywise.application.product.ProductDetails
import me.gimmesomepeace.buywise.application.product.ProductListItem
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.web.product.list.ListProductsResponse

internal fun ProductDetails.toDetailsResponse() =
    ProductDetailsResponse(
        id = id.value,
        name = name,
    )

internal fun Page<ProductListItem>.toListProductsResponse() =
    ListProductsResponse(
        products = this.items,
        nextPageToken = this.cursor?.value,
    )
