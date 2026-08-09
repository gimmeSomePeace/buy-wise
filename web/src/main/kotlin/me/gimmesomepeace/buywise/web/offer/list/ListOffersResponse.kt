package me.gimmesomepeace.buywise.web.offer.list

import me.gimmesomepeace.buywise.domain.offer.Offer

data class ListOffersResponse(
    val offers: List<Offer>,
    val nextPageToken: String?
)
