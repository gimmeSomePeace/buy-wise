package me.gimmesomepeace.buywise.application.offer

import me.gimmesomepeace.buywise.domain.offer.Offer

suspend fun offerRepository(vararg offers: Offer) =
    InMemoryOfferRepository().apply {
        offers.forEach {
            add(it)
        }
    }
