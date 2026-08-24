package me.gimmesomepeace.buywise.infrastructure.persistence.offer

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface OfferJpaRepository :
    JpaRepository<OfferEntity, UUID>,
    JpaSpecificationExecutor<OfferEntity> {
    @Query(
        """
        SELECT COUNT(o) > 0
        FROM OfferEntity o
        JOIN StoreEntity s ON o.storeId = s.id
        WHERE o.id = :id AND s.ownerId = :ownerId
    """,
    )
    fun existsByIdAndOwnerId(
        id: UUID,
        ownerId: UUID,
    ): Boolean
}
