package me.gimmesomepeace.buywise.domain.offer

import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.storeId

fun offerId(
    productId: ProductId = productId(),
    storeId: StoreId = storeId(),
) = OfferId(
    productId = productId,
    storeId = storeId,
)

fun offer(
    offerId: OfferId = offerId(),
    unitPrice: Money = 1.usd(),
) = Offer(
    id = offerId,
    productId = offerId.productId,
    storeId = offerId.storeId,
    unitPrice = unitPrice,
)
