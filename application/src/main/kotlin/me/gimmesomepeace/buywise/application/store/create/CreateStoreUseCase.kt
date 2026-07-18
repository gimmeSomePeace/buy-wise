package me.gimmesomepeace.buywise.application.store.create

import me.gimmesomepeace.buywise.application.IdGenerator
import me.gimmesomepeace.buywise.domain.store.Store
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.StoreRepository

class CreateStoreUseCase(
    private val storeRepository: StoreRepository,
    private val idGenerator: IdGenerator<StoreId>,
) {
    suspend fun execute(name: String) {
        val id = idGenerator.generate()
        val store = Store(id, name)

        storeRepository.add(store)
    }
}
