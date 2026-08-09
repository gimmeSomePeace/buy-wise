package me.gimmesomepeace.buywise.web.store

import me.gimmesomepeace.buywise.web.store.create.CreateStoreRequest
import me.gimmesomepeace.buywise.web.store.rename.RenameStoreRequest

internal fun createStoreRequest(
    name: String = "TEST NAME"
) = CreateStoreRequest(
    name = name
)

internal fun renameStoreRequest(
    name: String = "NEW NAME"
) = RenameStoreRequest(
    name = name
)
