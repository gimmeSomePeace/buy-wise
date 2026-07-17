package me.gimmesomepeace.offer.delete

import me.gimmesomepeace.buywise.offer.OfferId

data class DeleteOfferCommand(
    val offerId: OfferId,
)
