package me.gimmesomepeace.buywise.web.offer

import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Currency
import me.gimmesomepeace.buywise.domain.store.storeId
import me.gimmesomepeace.buywise.web.offer.create.CreateOfferRequest
import me.gimmesomepeace.buywise.web.offer.update.ChangePriceRequest
import java.math.BigDecimal
import java.util.UUID

internal fun createOfferRequest(
    productId: UUID = productId().value,
    storeId: UUID = storeId().value,
    unitPrice: BigDecimal = BigDecimal.ONE,
    currency: Currency = Currency.USD,
) = CreateOfferRequest(
    productId = productId,
    storeId = storeId,
    unitPrice = unitPrice,
    currency = currency,
)

internal fun changePriceRequest(
    unitPrice: BigDecimal = BigDecimal.ONE,
    currency: Currency = Currency.USD,
) = ChangePriceRequest(
    newPrice = unitPrice,
    newCurrency = currency,
)
