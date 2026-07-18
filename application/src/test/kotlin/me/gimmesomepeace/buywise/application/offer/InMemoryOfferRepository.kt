package me.gimmesomepeace.buywise.application.offer

import me.gimmesomepeace.buywise.domain.offer.Offer
import me.gimmesomepeace.buywise.domain.offer.OfferException
import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.offer.OfferRepository
import me.gimmesomepeace.buywise.domain.planning.offer.AvailableOffer
import me.gimmesomepeace.buywise.domain.planning.offer.AvailableOfferCatalog

class InMemoryOfferRepository : OfferRepository {
    private val data =
        mutableMapOf<OfferId, Offer>()

    override suspend fun get(offerId: OfferId): Offer =
        data[offerId]
            ?: throw OfferException.NotFound(offerId)

    override suspend fun add(offer: Offer) {
        if (offer.id in data) {
            throw OfferException.AlreadyExists(
                offer.id,
            )
        }
        data[offer.id] = offer
    }

    override suspend fun update(offer: Offer) {
        if (offer.id !in data) {
            throw OfferException.NotFound(
                offer.id,
            )
        }
        data[offer.id] = offer
    }

    override suspend fun delete(offerId: OfferId) {
        if (offerId !in data) {
            throw OfferException.NotFound(
                offerId,
            )
        }
        data.remove(offerId)
    }

    override suspend fun availableOffers() =
        AvailableOfferCatalog(
            data.values.map {
                AvailableOffer(
                    storeId = it.storeId,
                    productId = it.productId,
                    unitPrice = it.unitPrice,
                )
            },
        )
}
