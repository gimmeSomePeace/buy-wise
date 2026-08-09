package me.gimmesomepeace.buywise.infrastructure.persistence.store

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StoreJpaRepository : JpaRepository<StoreEntity, UUID> {
    fun findByIdGreaterThanOrderByIdAsc(
        id: UUID,
        pageable: Pageable
    ): List<StoreEntity>
}
