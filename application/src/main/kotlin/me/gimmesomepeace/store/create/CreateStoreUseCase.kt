package me.gimmesomepeace.store.create

import me.gimmesomepeace.IdGenerator
import me.gimmesomepeace.buywise.store.Store
import me.gimmesomepeace.buywise.store.StoreId
import me.gimmesomepeace.buywise.store.StoreRepository

class CreateStoreUseCase(
    private val storeRepository: StoreRepository,
    private val idGenerator: IdGenerator<StoreId>,
) {
    suspend fun execute(command: CreateStoreCommand) {
        val id = idGenerator.generate()
        val store = Store(id, command.name)

        storeRepository.add(store)
    }
}
