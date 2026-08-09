package me.gimmesomepeace.buywise.web.store

import me.gimmesomepeace.buywise.domain.store.StoreId

internal data class StoreDetailsResponse(
    val id: StoreId,
    val name: String,
)
