package me.gimmesomepeace.buywise.infrastructure.persistence.offer.specification

import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Currency
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.infrastructure.persistence.offer.OfferEntity
import me.gimmesomepeace.buywise.infrastructure.persistence.store.StoreEntity
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import java.util.*

object OfferSpecifications {
    fun byProductIds(productIds: List<ProductId>) : Specification<OfferEntity> =
        Specification { root, _, _ ->
            root.get<UUID>("productId").`in`(productIds.map { it.value })
        }

    fun byStoreIds(storeIds: List<StoreId>) : Specification<OfferEntity> =
        Specification { root, _, _ ->
            root.get<UUID>("storeId").`in`(storeIds.map { it.value })
        }

    fun byMinPrice(minPrice: BigDecimal) : Specification<OfferEntity> =
        Specification { root, _, builder ->
            builder.ge(root.get<BigDecimal>("price"), minPrice)
        }

    fun byMaxPrice(maxPrice: BigDecimal) : Specification<OfferEntity> =
        Specification { root, _, builder ->
            builder.le(root.get<BigDecimal>("price"), maxPrice)
        }

    fun byCurrencies(currencies: List<Currency>) : Specification<OfferEntity> =
        Specification { root, _, _ ->
            root.get<Currency>("currency").`in`(currencies)
        }

    fun afterCursor(cursor: Cursor): Specification<OfferEntity> =
        Specification { root, _, builder ->
            builder.greaterThan(
                root.get("id"),
                UUID.fromString(cursor.value)
            )
        }

    fun byOwner(ownerId: UserId) : Specification<OfferEntity> =
        Specification { root, _, builder ->
            val storeJoin = root.join<OfferEntity, StoreEntity>("store")
            builder.equal(storeJoin.get<UUID>("ownerId"), ownerId.value)
        }
}
