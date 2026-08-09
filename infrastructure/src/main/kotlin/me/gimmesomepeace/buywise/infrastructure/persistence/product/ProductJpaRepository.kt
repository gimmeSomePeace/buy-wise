package me.gimmesomepeace.buywise.infrastructure.persistence.product

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProductJpaRepository : JpaRepository<ProductEntity, UUID> {
    fun findByIdGreaterThanOrderByIdAsc(
        id: UUID,
        pageable: Pageable
    ): List<ProductEntity>
}
