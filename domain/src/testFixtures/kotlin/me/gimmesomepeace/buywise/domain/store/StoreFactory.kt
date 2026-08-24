package me.gimmesomepeace.buywise.domain.store

import com.github.f4b6a3.uuid.UuidCreator
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.userId

fun storeId() = StoreId(UuidCreator.getTimeOrderedEpoch())

fun store(
    id: StoreId = storeId(),
    ownerId: UserId = userId(),
    name: String = "Nameless store",
) = Store(
    id = id,
    ownerId = ownerId,
    name = name,
)
