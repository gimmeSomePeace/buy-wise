package me.gimmesomepeace.buywise.web.product.list

import me.gimmesomepeace.buywise.application.product.ProductListItem

data class ListProductsResponse(
    val products: List<ProductListItem>,
    val nextPageToken: String?,
)
