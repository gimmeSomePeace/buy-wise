package me.gimmesomepeace.buywise.web.product.list

import me.gimmesomepeace.buywise.domain.product.Product

data class ListProductsResponse(
    val products: List<Product>,
    val nextPageToken: String?,
)
