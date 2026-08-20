package me.gimmesomepeace.buywise.application.offer

import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.offer.offerId
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.storeId
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.userId

fun offerDetails(
    id: OfferId = offerId(),
    ownerId: UserId = userId(),
    productId: ProductId = productId(),
    storeId: StoreId = storeId(),
    unitPrice: Money = 1.usd()
) = OfferDetails(
    id = id,
    ownerId = ownerId,
    storeId = storeId,
    productId = productId,
    unitPrice = unitPrice
)

fun offerListItem(
    id: OfferId = offerId(),
    ownerId: UserId = userId(),
    productId: ProductId = productId(),
    storeId: StoreId = storeId(),
    unitPrice: Money = 1.usd()
) = OfferListItem(
    id = id,
    ownerId = ownerId,
    storeId = storeId,
    productId = productId,
    unitPrice = unitPrice
)
