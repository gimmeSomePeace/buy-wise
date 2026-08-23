package me.gimmesomepeace.buywise.application.store

import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.store.StoreId

interface StoreQuery {
    suspend fun find(
        id: StoreId,
    ): StoreDetails?

    suspend fun list(
        request: PageRequest,
        filters: StoreFilters = StoreFilters(),
    ): Page<StoreListItem>
}
