package me.gimmesomepeace.store.rename

import me.gimmesomepeace.buywise.store.StoreRepository

class RenameStoreUseCase(
    private val storeRepository: StoreRepository
) {
    suspend fun execute(command: RenameStoreCommand) {
        val store = storeRepository.get(command.storeId)
        store.rename(command.newName)

        storeRepository.update(store)
    }
}
