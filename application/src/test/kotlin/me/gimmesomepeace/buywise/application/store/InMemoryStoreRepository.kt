package me.gimmesomepeace.buywise.application.store

import me.gimmesomepeace.buywise.domain.store.Store
import me.gimmesomepeace.buywise.domain.store.StoreException
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.StoreRepository

class InMemoryStoreRepository : StoreRepository {
    private val data =
        mutableMapOf<StoreId, Store>()

    override suspend fun get(storeId: StoreId): Store =
        data[storeId]
            ?: throw StoreException.NotFound(storeId)

    override suspend fun add(store: Store) {
        if (store.id in data) {
            throw StoreException.AlreadyExists(
                store.id,
            )
        }
        data[store.id] = store
    }

    override suspend fun update(store: Store) {
        if (store.id !in data) {
            throw StoreException.NotFound(
                store.id,
            )
        }
        data[store.id] = store
    }

    override suspend fun delete(id: StoreId) {
        if (id !in data) throw StoreException.NotFound(id)
        data.remove(id)
    }
}
