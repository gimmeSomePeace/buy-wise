package me.gimmesomepeace.buywise.web.product.create

import jakarta.validation.constraints.NotBlank

internal data class CreateProductRequest(
    @field:NotBlank
    val name: String,
)
