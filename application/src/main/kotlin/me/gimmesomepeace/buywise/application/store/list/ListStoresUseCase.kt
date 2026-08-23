package me.gimmesomepeace.buywise.application.store.list

import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.store.StoreFilters
import me.gimmesomepeace.buywise.application.store.StoreQuery
import me.gimmesomepeace.buywise.domain.user.UserId

class ListStoresUseCase(
    private val storeQuery: StoreQuery,
) {
    suspend fun execute(
        ownerId: UserId,
        request: PageRequest,
        filters: StoreFilters = StoreFilters()
    ) = storeQuery.list(request, filters.copy(ownerId = ownerId))
}
