package me.gimmesomepeace.buywise.application.store

import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.storeId
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.userId

fun storeDetails(
    id: StoreId = storeId(),
    ownerId: UserId = userId(),
    name: String = "STORE DETAILS NAME"
) = StoreDetails(id, ownerId, name)

fun storeListItem(
    id: StoreId = storeId(),
    ownerId: UserId = userId(),
    name: String = "STORE LIST ITEM NAME"
) = StoreListItem(id, ownerId, name)
