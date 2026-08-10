package me.gimmesomepeace.buywise.application.store.list

import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.store.StoreQuery

class ListStoresUseCase(
    private val storeQuery: StoreQuery,
) {
    suspend fun execute(
        request: PageRequest,
    ) = storeQuery.list(request)
}
