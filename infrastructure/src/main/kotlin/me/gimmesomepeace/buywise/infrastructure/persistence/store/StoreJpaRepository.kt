package me.gimmesomepeace.buywise.infrastructure.persistence.store

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface StoreJpaRepository :
    JpaRepository<StoreEntity, UUID>,
    JpaSpecificationExecutor<StoreEntity>
