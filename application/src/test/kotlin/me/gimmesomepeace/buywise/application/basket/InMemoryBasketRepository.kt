package me.gimmesomepeace.buywise.application.basket

import me.gimmesomepeace.buywise.domain.basket.Basket
import me.gimmesomepeace.buywise.domain.basket.BasketRepository

class InMemoryBasketRepository : BasketRepository {
    private var basket: Basket? = null

    override suspend fun find() = basket

    override suspend fun save(basket: Basket) {
        this.basket = basket
    }
}
