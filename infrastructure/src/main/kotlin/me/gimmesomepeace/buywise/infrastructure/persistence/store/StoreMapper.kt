package me.gimmesomepeace.buywise.infrastructure.persistence.store

import me.gimmesomepeace.buywise.domain.store.Store
import me.gimmesomepeace.buywise.domain.store.StoreId

internal fun StoreEntity.toDomain() =
    Store(
        id = StoreId(this.id),
        name = this.name,
    )

internal fun Store.toEntity() =
    StoreEntity(
        id = this.id.value,
        name = this.name,
    )
