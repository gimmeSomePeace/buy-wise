package me.gimmesomepeace.buywise.application.offer

import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.user.UserId

data class OfferDetails(
    val id: OfferId,
    val ownerId: UserId,
    val storeId: StoreId,
    val productId: ProductId,
    val unitPrice: Money,
)
