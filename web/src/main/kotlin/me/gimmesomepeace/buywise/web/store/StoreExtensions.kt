package me.gimmesomepeace.buywise.web.store

import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.domain.store.Store
import me.gimmesomepeace.buywise.web.store.list.ListStoresResponse

internal fun Store.toDetailsResponse() = StoreDetailsResponse(
    id = this.id,
    name = this.name,
)

internal fun Page<Store>.toListStoresResponse() = ListStoresResponse(
    stores = this.items,
    nextPageToken = this.cursor?.value
)
