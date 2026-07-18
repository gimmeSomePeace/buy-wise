package me.gimmesomepeace.buywise.domain.planning

import me.gimmesomepeace.buywise.domain.planning.offer.AvailableOffer
import me.gimmesomepeace.buywise.domain.planning.offer.AvailableOfferCatalog
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.storeId

fun available(
    productId: ProductId = productId(),
    storeId: StoreId = storeId(),
    price: Money = 1.usd(),
) = AvailableOffer(
    storeId = storeId,
    productId = productId,
    unitPrice = price,
)

fun offerCatalog(vararg availableOffers: AvailableOffer) =
    AvailableOfferCatalog(
        availableOffers.toList(),
    )
