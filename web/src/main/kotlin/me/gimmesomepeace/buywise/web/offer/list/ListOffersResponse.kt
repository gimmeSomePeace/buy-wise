package me.gimmesomepeace.buywise.web.offer.list

import me.gimmesomepeace.buywise.application.offer.OfferListItem

data class ListOffersResponse(
    val offers: List<OfferListItem>,
    val nextPageToken: String?,
)
