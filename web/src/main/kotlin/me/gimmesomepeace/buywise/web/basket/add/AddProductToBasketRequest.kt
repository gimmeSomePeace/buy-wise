package me.gimmesomepeace.buywise.web.basket.add

import jakarta.validation.constraints.Positive
import java.util.UUID

internal data class AddProductToBasketRequest(
    val productId: UUID,
    @field:Positive
    val quantity: Int,
)
