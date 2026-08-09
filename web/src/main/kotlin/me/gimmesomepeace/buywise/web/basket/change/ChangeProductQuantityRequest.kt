package me.gimmesomepeace.buywise.web.basket.change

import jakarta.validation.constraints.PositiveOrZero

internal data class ChangeProductQuantityRequest(
    @field:PositiveOrZero
    val newQuantity: Int
)
