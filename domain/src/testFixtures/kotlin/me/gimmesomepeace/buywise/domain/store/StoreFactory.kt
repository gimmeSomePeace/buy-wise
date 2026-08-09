package me.gimmesomepeace.buywise.domain.store

import com.github.f4b6a3.uuid.UuidCreator

fun storeId() = StoreId(UuidCreator.getTimeOrderedEpoch())

fun store(
    id: StoreId = storeId(),
    name: String = "Nameless store",
) = Store(
    id = id,
    name = name,
)
