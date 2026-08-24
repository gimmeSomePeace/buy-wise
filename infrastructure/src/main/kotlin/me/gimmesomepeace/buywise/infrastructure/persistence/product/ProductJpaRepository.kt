package me.gimmesomepeace.buywise.infrastructure.persistence.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.UUID

interface ProductJpaRepository :
    JpaRepository<ProductEntity, UUID>,
    JpaSpecificationExecutor<ProductEntity>
