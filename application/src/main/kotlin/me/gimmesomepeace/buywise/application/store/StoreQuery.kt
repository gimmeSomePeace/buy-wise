package me.gimmesomepeace.buywise.application.store

import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.store.Store
import me.gimmesomepeace.buywise.domain.store.StoreId

interface StoreQuery {
    suspend fun find(id: StoreId) : Store?
    suspend fun list(request: PageRequest) : Page<Store>
}
