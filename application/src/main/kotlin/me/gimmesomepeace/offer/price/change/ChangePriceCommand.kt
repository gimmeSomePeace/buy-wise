package me.gimmesomepeace.offer.price.change

import me.gimmesomepeace.buywise.offer.OfferId
import me.gimmesomepeace.buywise.shared.Money

data class ChangePriceCommand(
    val offerId: OfferId,
    val newPrice: Money,
)
