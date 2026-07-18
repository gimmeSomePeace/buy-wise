package me.gimmesomepeace.buywise.domain.store

import java.util.UUID

fun storeId() = StoreId(UUID.randomUUID())

fun store(
    id: StoreId = storeId(),
    name: String = "Nameless store",
) = Store(
    id = id,
    name = name,
)
