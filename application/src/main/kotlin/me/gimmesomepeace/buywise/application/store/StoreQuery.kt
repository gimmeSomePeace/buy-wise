package me.gimmesomepeace.buywise.application.store

import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.store.Store
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.user.UserId

interface StoreQuery {
    suspend fun find(
        id: StoreId,
    ): Store?

    suspend fun list(
        ownerId: UserId,
        request: PageRequest,
    ): Page<Store>
}
