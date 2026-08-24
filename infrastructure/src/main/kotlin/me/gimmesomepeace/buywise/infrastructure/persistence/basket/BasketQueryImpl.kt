package me.gimmesomepeace.buywise.infrastructure.persistence.basket

import me.gimmesomepeace.buywise.application.basket.BasketDetails
import me.gimmesomepeace.buywise.application.basket.BasketQuery

class BasketQueryImpl(
    private val repository: BasketJpaRepository,
) : BasketQuery {
    override suspend fun find(): BasketDetails? {
        val items = repository.findAll()
        if (items.isEmpty()) return null

        return BasketDetails(
            items =
                items.map {
                    it
                        .toBasketItemDetails()
                },
        )
    }
}
