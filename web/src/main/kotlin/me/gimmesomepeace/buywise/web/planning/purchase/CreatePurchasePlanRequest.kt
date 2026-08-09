package me.gimmesomepeace.buywise.web.planning.purchase

import jakarta.validation.constraints.Positive

internal data class CreatePurchasePlanRequest(
    @field:Positive
    val storeCountLimit: Int?
)
