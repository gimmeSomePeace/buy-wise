package me.gimmesomepeace.buywise.infrastructure.persistence.user

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserJpaRepository : JpaRepository<UserEntity, UUID> {
    fun findByLogin(login: String): UserEntity?

    fun findByIdGreaterThanOrderByIdAsc(
        id: UUID,
        pageable: Pageable,
    ): List<UserEntity>
}
