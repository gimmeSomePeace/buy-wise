package me.gimmesomepeace.buywise.infrastructure.persistence.store.specification

import me.gimmesomepeace.buywise.application.store.StoreFilters
import me.gimmesomepeace.buywise.infrastructure.persistence.store.StoreEntity
import org.springframework.data.jpa.domain.Specification


internal fun StoreFilters.toSpecification(): Specification<StoreEntity> =
    listOfNotNull(
        ownerId?.let { StoreSpecifications.byOwner(it) },
        nameContains?.let { StoreSpecifications.nameContains(it) },
    ).fold(Specification.unrestricted()) {acc, spec ->
        acc.and(spec)
    }
