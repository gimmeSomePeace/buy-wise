package me.gimmesomepeace.buywise.web.store

import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.store.StoreDetails
import me.gimmesomepeace.buywise.application.store.StoreListItem
import me.gimmesomepeace.buywise.web.store.list.ListStoresResponse

internal fun StoreDetails.toDetailsResponse() =
    StoreDetailsResponse(
        id = this.id,
        name = this.name,
    )

internal fun Page<StoreListItem>.toListStoresResponse() =
    ListStoresResponse(
        stores = this.items,
        nextPageToken = this.cursor?.value,
    )
