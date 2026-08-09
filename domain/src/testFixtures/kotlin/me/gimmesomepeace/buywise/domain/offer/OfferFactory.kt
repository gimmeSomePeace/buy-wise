package me.gimmesomepeace.buywise.domain.offer

import com.github.f4b6a3.uuid.UuidCreator
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.storeId

fun offerId() = OfferId(UuidCreator.getTimeOrderedEpoch())

fun offer(
    offerId: OfferId = offerId(),
    productId: ProductId = productId(),
    storeId: StoreId = storeId(),
    unitPrice: Money = 1.usd(),
) = Offer(
    id = offerId,
    productId = productId,
    storeId = storeId,
    unitPrice = unitPrice,
)
