package me.gimmesomepeace.buywise.infrastructure.persistence.offer

import me.gimmesomepeace.buywise.application.offer.OfferDetails
import me.gimmesomepeace.buywise.application.offer.OfferListItem
import me.gimmesomepeace.buywise.domain.offer.Offer
import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.planning.offer.AvailableOffer
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.user.UserId
import java.util.UUID

internal fun OfferEntity.toDomain() =
    Offer(
        id = OfferId(this.id),
        productId = ProductId(this.productId),
        storeId = StoreId(this.storeId),
        unitPrice = Money(this.price, this.currency),
    )

internal fun Offer.toEntity() =
    OfferEntity(
        id = this.id.value,
        productId = this.productId.value,
        storeId = this.storeId.value,
        price = this.unitPrice.value,
        currency = this.unitPrice.currency,
    )

internal fun OfferEntity.toDetails() = OfferDetails(
    id = OfferId(this.id),
    ownerId = UserId(UUID.randomUUID()),
    storeId = StoreId(this.storeId),
    productId = ProductId(this.productId),
    unitPrice = Money(this.price, this.currency),
)

internal fun OfferEntity.toListItem() = OfferListItem(
    id = OfferId(this.id),
    ownerId = UserId(UUID.randomUUID()),
    storeId = StoreId(this.storeId),
    productId = ProductId(this.productId),
    unitPrice = Money(this.price, this.currency),
)

internal fun OfferEntity.toAvailableOffer() =
    AvailableOffer(
        storeId = StoreId(this.storeId),
        productId = ProductId(this.productId),
        unitPrice = Money(this.price, this.currency),
    )
