package me.gimmesomepeace.buywise.infrastructure.persistence.offer

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OfferJpaRepository : JpaRepository<OfferEntity, UUID> {
    fun findByIdGreaterThanOrderByIdAsc(
        id: UUID,
        pageable: Pageable,
    ): List<OfferEntity>
}
