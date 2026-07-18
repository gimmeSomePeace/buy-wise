package me.gimmesomepeace.buywise.application.store

import me.gimmesomepeace.buywise.domain.store.Store

suspend fun storeRepository(vararg stores: Store) =
    InMemoryStoreRepository().apply {
        stores.forEach {
            add(it)
        }
    }
