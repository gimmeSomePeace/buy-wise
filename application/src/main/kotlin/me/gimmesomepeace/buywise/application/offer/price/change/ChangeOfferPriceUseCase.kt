package me.gimmesomepeace.buywise.application.offer.price.change

import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.domain.offer.OfferException
import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.offer.OfferRepository
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.user.UserId

class ChangeOfferPriceUseCase(
    private val repository: OfferRepository,
    private val query: OfferQuery,
) {
    suspend fun execute(
        userId: UserId,
        offerId: OfferId,
        newPrice: Money,
    ) {
        if (!query.existsByIdAndOwner(
                offerId,
                userId,
            )
        ) {
            throw OfferException.NotFound(offerId)
        }

        val offer = repository.get(offerId)
        offer.changePrice(newPrice)
        repository.update(offer)
    }
}
