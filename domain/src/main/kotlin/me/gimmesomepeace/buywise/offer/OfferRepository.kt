package me.gimmesomepeace.buywise.offer

import me.gimmesomepeace.buywise.planning.offer.AvailableOfferCatalog

interface OfferRepository {
    suspend fun get(offerId: OfferId): Offer

    suspend fun add(offer: Offer)

    suspend fun update(offer: Offer)

    suspend fun delete(offerId: OfferId)

    suspend fun availableOffers(): AvailableOfferCatalog
}
