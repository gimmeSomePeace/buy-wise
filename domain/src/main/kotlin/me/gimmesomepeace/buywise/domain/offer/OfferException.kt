package me.gimmesomepeace.buywise.domain.offer

import me.gimmesomepeace.buywise.domain.shared.DomainException

sealed class OfferException(
    message: String,
) : DomainException(message) {
    class NotFound(
        offerId: OfferId,
    ) : OfferException("Offer with id $offerId not found")

    class AlreadyExists(
        offerId: OfferId,
    ) : OfferException("Offer with id $offerId already exists")
}
