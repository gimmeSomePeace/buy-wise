package me.gimmesomepeace.store.rename

import me.gimmesomepeace.buywise.store.StoreId

data class RenameStoreCommand(
    val storeId: StoreId,
    val newName: String,
)
