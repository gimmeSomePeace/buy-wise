package me.gimmesomepeace.buywise.repository

import me.gimmesomepeace.buywise.basket.Basket
import me.gimmesomepeace.buywise.basket.BasketRepository

class InMemoryBasketRepository : BasketRepository {
    private var _basket: Basket? = null

    override suspend fun find() = _basket

    override suspend fun save(basket: Basket) {
        _basket = basket
    }
}
