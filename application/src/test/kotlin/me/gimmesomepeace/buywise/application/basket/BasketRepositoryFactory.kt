package me.gimmesomepeace.buywise.application.basket

import me.gimmesomepeace.buywise.domain.basket.Basket

suspend fun basketRepository(basket: Basket = Basket()) =
    InMemoryBasketRepository().apply {
        save(basket)
    }
