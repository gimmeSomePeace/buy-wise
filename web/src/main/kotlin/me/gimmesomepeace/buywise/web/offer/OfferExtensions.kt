package me.gimmesomepeace.buywise.web.offer

import me.gimmesomepeace.buywise.application.offer.OfferDetails
import me.gimmesomepeace.buywise.application.offer.OfferListItem
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.web.offer.list.ListOffersResponse

internal fun OfferDetails.toDetailsResponse() =
    OfferDetailsResponse(
        id = this.id.value,
        productId = this.productId.value,
        storeId = this.storeId.value,
        unitPrice = this.unitPrice.value,
        currency = this.unitPrice.currency,
    )

internal fun Page<OfferListItem>.toListOffersResponse() =
    ListOffersResponse(
        offers = this.items,
        nextPageToken = cursor?.value,
    )
