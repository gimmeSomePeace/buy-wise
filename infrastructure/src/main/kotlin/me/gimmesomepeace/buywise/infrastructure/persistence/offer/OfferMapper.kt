package me.gimmesomepeace.buywise.infrastructure.persistence.offer

import me.gimmesomepeace.buywise.domain.offer.Offer
import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.planning.offer.AvailableOffer
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.store.StoreId

internal fun OfferEntity.toDomain() = Offer(
    id = OfferId(this.id),
    productId = ProductId(this.productId),
    storeId = StoreId(this.storeId),
    unitPrice = Money(this.price, this.currency),
)

internal fun Offer.toEntity() = OfferEntity(
    id = this.id.value,
    productId = this.productId.value,
    storeId = this.storeId.value,
    price = this.unitPrice.value,
    currency = this.unitPrice.currency,
)

internal fun OfferEntity.toAvailableOffer() = AvailableOffer(
    storeId = StoreId(this.storeId),
    productId = ProductId(this.productId),
    unitPrice = Money(this.price, this.currency),
)
