package me.gimmesomepeace.buywise.application.offer

import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.user.UserId

data class OfferListItem(
    val id: OfferId,
    val ownerId: UserId,
    val productId: ProductId,
    val storeId: StoreId,
    val unitPrice: Money,
)
