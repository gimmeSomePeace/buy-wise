package me.gimmesomepeace.store.delete

import me.gimmesomepeace.buywise.store.StoreRepository

class DeleteStoreUseCase(
    private val storeRepository: StoreRepository
) {
    suspend fun execute(command: DeleteStoreCommand) {
        storeRepository.delete(command.storeId)
    }
}
