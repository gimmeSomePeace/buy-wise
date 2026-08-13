package me.gimmesomepeace.buywise.infrastructure.persistence.product

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProductJpaRepository : JpaRepository<ProductEntity, UUID> {
    fun findByOwnerId(
        ownerId: UUID,
        pageable: Pageable
    ): List<ProductEntity>

    fun findByOwnerIdAndIdGreaterThanOrderByIdAsc(
        ownerId: UUID,
        id: UUID,
        pageable: Pageable,
    ): List<ProductEntity>
}
