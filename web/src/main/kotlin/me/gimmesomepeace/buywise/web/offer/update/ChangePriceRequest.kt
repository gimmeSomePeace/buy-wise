package me.gimmesomepeace.buywise.web.offer.update

import jakarta.validation.constraints.Positive
import me.gimmesomepeace.buywise.domain.shared.Currency
import java.math.BigDecimal

internal data class ChangePriceRequest(
    @field:Positive
    val newPrice: BigDecimal,
    val newCurrency: Currency,
)
