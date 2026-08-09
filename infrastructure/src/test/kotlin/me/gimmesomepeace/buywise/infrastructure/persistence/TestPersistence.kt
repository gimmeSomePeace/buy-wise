package me.gimmesomepeace.buywise.infrastructure.persistence

import me.gimmesomepeace.buywise.domain.offer.Offer
import me.gimmesomepeace.buywise.domain.product.Product
import me.gimmesomepeace.buywise.domain.store.Store
import me.gimmesomepeace.buywise.infrastructure.persistence.offer.OfferJpaRepository
import me.gimmesomepeace.buywise.infrastructure.persistence.offer.toEntity
import me.gimmesomepeace.buywise.infrastructure.persistence.product.ProductJpaRepository
import me.gimmesomepeace.buywise.infrastructure.persistence.product.toEntity
import me.gimmesomepeace.buywise.infrastructure.persistence.store.StoreJpaRepository
import me.gimmesomepeace.buywise.infrastructure.persistence.store.toEntity
import org.springframework.stereotype.Component

@Component
internal class TestPersistence(
    private val offerJpaRepository: OfferJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
    private val storeJpaRepository: StoreJpaRepository,
) {
    fun persist(store: Store) : Store =
        store.also {
            storeJpaRepository.save(it.toEntity())
        }

    fun persist(product: Product) : Product =
        product.also {
            productJpaRepository.save(it.toEntity())
        }

    fun persist(offer: Offer) : Offer =
        offer.also {
            offerJpaRepository.save(it.toEntity())
        }
}