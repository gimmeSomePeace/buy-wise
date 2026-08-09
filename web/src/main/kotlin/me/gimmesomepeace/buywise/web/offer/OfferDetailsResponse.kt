package me.gimmesomepeace.buywise.web.offer

import me.gimmesomepeace.buywise.domain.shared.Currency
import java.math.BigDecimal
import java.util.UUID

internal data class OfferDetailsResponse(
    val id: UUID,
    val productId: UUID,
    val storeId: UUID,
    val unitPrice: BigDecimal,
    val currency: Currency,
)
