package me.gimmesomepeace.buywise.web.offer

import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.domain.offer.Offer
import me.gimmesomepeace.buywise.web.offer.list.ListOffersResponse

internal fun Offer.toDetailsResponse() =
    OfferDetailsResponse(
        id = this.id.value,
        productId = this.productId.value,
        storeId = this.storeId.value,
        unitPrice = this.unitPrice.value,
        currency = this.unitPrice.currency,
    )

internal fun Page<Offer>.toListOffersResponse() =
    ListOffersResponse(
        offers = this.items,
        nextPageToken = cursor?.value,
    )
