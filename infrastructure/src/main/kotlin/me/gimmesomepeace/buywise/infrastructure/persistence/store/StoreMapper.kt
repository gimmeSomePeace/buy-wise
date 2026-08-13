package me.gimmesomepeace.buywise.infrastructure.persistence.store

import me.gimmesomepeace.buywise.domain.store.Store
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.user.UserId

internal fun StoreEntity.toDomain() =
    Store(
        id = StoreId(this.id),
        ownerId = UserId(this.ownerId),
        name = this.name,
    )

internal fun Store.toEntity() =
    StoreEntity(
        id = this.id.value,
        ownerId = this.ownerId.value,
        name = this.name,
    )
