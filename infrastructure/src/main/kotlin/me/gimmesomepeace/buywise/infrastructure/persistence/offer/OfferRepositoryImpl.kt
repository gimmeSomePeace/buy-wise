package me.gimmesomepeace.buywise.infrastructure.persistence.offer

import me.gimmesomepeace.buywise.domain.offer.Offer
import me.gimmesomepeace.buywise.domain.offer.OfferException
import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.offer.OfferRepository
import me.gimmesomepeace.buywise.domain.planning.offer.AvailableOfferCatalog

class OfferRepositoryImpl(
    private val repository: OfferJpaRepository,
) : OfferRepository {
    override suspend fun get(offerId: OfferId): Offer =
        repository
            .findById(offerId.value)
            .orElseThrow { OfferException.NotFound(offerId) }
            .toDomain()

    override suspend fun add(offer: Offer) {
        if (repository.existsById(offer.id.value)) throw OfferException.AlreadyExists(offer.id)
        repository.save(offer.toEntity())
    }

    override suspend fun update(offer: Offer) {
        if (!repository.existsById(offer.id.value)) throw OfferException.NotFound(offer.id)
        repository.save(offer.toEntity())
    }

    override suspend fun delete(offerId: OfferId) {
        if (!repository.existsById(offerId.value)) throw OfferException.NotFound(offerId)
        repository.deleteById(offerId.value)
    }

    override suspend fun availableOffers(): AvailableOfferCatalog =
        AvailableOfferCatalog(
            offers = repository.findAll().map { it.toAvailableOffer() },
        )
}
