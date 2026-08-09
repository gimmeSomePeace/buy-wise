package me.gimmesomepeace.buywise.infrastructure.persistence.basket

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BasketJpaRepository : JpaRepository<BasketEntity, UUID>
