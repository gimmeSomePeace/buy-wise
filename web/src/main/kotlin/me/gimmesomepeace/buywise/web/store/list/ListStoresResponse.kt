package me.gimmesomepeace.buywise.web.store.list

import me.gimmesomepeace.buywise.application.store.StoreListItem

data class ListStoresResponse(
    val stores: List<StoreListItem>,
    val nextPageToken: String?,
)
