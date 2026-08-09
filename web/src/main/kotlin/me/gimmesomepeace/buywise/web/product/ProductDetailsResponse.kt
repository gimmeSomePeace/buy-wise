package me.gimmesomepeace.buywise.web.product

import java.util.UUID

internal data class ProductDetailsResponse(
    val id: UUID,
    val name: String,
)
