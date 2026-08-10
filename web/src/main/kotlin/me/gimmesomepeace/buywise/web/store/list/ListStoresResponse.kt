package me.gimmesomepeace.buywise.web.store.list

import me.gimmesomepeace.buywise.domain.store.Store

data class ListStoresResponse(
    val stores: List<Store>,
    val nextPageToken: String?,
)
