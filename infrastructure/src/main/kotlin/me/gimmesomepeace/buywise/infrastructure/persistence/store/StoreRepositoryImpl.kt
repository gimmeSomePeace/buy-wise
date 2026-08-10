package me.gimmesomepeace.buywise.infrastructure.persistence.store

import me.gimmesomepeace.buywise.domain.store.Store
import me.gimmesomepeace.buywise.domain.store.StoreException
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.StoreRepository

class StoreRepositoryImpl(
    private val repository: StoreJpaRepository,
) : StoreRepository {
    override suspend fun get(
        storeId: StoreId,
    ) = repository
        .findById(storeId.value)
        .orElseThrow { StoreException.NotFound(storeId) }
        .toDomain()

    override suspend fun add(
        store: Store,
    ) {
        if (repository.existsById(store.id.value)) {
            throw StoreException.AlreadyExists(store.id)
        }
        repository.save(store.toEntity())
    }

    override suspend fun update(
        store: Store,
    ) {
        if (!repository.existsById(store.id.value)) {
            throw StoreException.NotFound(store.id)
        }
        repository.save(store.toEntity())
    }

    override suspend fun delete(
        id: StoreId,
    ) {
        if (!repository.existsById(id.value)) {
            throw StoreException.NotFound(id)
        }
        repository.deleteById(id.value)
    }
}
