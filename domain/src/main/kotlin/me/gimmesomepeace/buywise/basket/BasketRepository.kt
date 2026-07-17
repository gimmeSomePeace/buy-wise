package me.gimmesomepeace.buywise.basket

interface BasketRepository {
    suspend fun find(): Basket?

    suspend fun save(basket: Basket)
}
