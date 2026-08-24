package me.gimmesomepeace.buywise.application.store

import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.user.UserId

data class StoreListItem(
    val id: StoreId,
    val ownerId: UserId,
    val name: String,
)
