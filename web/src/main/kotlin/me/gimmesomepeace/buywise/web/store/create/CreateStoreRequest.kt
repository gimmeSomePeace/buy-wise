package me.gimmesomepeace.buywise.web.store.create

import jakarta.validation.constraints.NotBlank

internal data class CreateStoreRequest(
    @field:NotBlank
    val name: String,
)
