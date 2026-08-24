package me.gimmesomepeace.buywise.web.offer.create

import jakarta.validation.constraints.Positive
import me.gimmesomepeace.buywise.domain.shared.Currency
import java.math.BigDecimal
import java.util.UUID

internal data class CreateOfferRequest(
    val productId: UUID,
    val storeId: UUID,
    @field:Positive
    val unitPrice: BigDecimal,
    val currency: Currency,
)
