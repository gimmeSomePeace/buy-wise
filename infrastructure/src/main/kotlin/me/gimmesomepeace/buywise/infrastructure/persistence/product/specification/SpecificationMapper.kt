package me.gimmesomepeace.buywise.infrastructure.persistence.product.specification

import me.gimmesomepeace.buywise.application.product.ProductFilters
import org.springframework.data.jpa.domain.Specification

fun ProductFilters.toSpecification() =
    listOfNotNull(
        ownerId?.let { ProductSpecifications.byOwner(it) },
        nameContains?.let { ProductSpecifications.nameContains(it) }
    ).reduceOrNull { acc, spec -> acc.and(spec) } ?: Specification.unrestricted()
