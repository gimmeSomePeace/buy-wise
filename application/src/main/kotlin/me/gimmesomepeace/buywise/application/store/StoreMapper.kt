package me.gimmesomepeace.buywise.application.store

import me.gimmesomepeace.buywise.domain.store.Store


fun Store.toDetails() = StoreDetails(
    id = this.id,
    ownerId = this.ownerId,
    name = this.name,
)
