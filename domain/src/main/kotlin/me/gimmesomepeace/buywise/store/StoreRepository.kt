package me.gimmesomepeace.buywise.store

interface StoreRepository {
    suspend fun get(storeId: StoreId): Store

    suspend fun add(store: Store)

    suspend fun update(store: Store)

    suspend fun delete(id: StoreId)
}
