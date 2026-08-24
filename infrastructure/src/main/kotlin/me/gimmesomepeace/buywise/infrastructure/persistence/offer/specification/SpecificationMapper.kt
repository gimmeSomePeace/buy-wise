package me.gimmesomepeace.buywise.infrastructure.persistence.offer.specification

import me.gimmesomepeace.buywise.application.offer.OfferFilters
import me.gimmesomepeace.buywise.infrastructure.persistence.offer.OfferEntity
import org.springframework.data.jpa.domain.Specification

fun OfferFilters.toSpecification(): Specification<OfferEntity> =
    listOfNotNull(
        ownerId?.let { OfferSpecifications.byOwner(it) },
        productIds?.let { OfferSpecifications.byProductIds(it) },
        storeIds?.let { OfferSpecifications.byStoreIds(it) },
        minPrice?.let { OfferSpecifications.byMinPrice(it) },
        maxPrice?.let { OfferSpecifications.byMaxPrice(it) },
        currencies?.let { OfferSpecifications.byCurrencies(it) },
    ).reduceOrNull { acc, spec -> acc.and(spec) }
        ?: Specification.unrestricted()
