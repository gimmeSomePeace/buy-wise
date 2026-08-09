package me.gimmesomepeace.buywise.infrastructure.persistence.basket

import me.gimmesomepeace.buywise.domain.basket.Basket
import me.gimmesomepeace.buywise.domain.basket.BasketRepository
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Quantity

class BasketRepositoryImpl(
    private val repository: BasketJpaRepository,
) : BasketRepository {
    override suspend fun find(): Basket? {
        val items = repository.findAll()
        if (items.isEmpty()) return null

        return Basket().apply {
            items.forEach {
                add(
                    ProductId(it.productId),
                    Quantity(it.quantity),
                )
            }
        }
    }

    override suspend fun save(basket: Basket) {
        repository.deleteAll()
        repository.saveAll(
            basket.items().map {
                it.toEntity()
            },
        )
    }
}
