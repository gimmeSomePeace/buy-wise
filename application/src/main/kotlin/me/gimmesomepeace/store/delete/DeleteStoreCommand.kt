package me.gimmesomepeace.store.delete

import me.gimmesomepeace.buywise.store.StoreId

data class DeleteStoreCommand(
    val storeId: StoreId,
)
