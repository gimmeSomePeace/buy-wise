package me.gimmesomepeace.buywise.application.store.rename

import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.StoreRepository

class RenameStoreUseCase(
    private val storeRepository: StoreRepository,
) {
    suspend fun execute(
        storeId: StoreId,
        newName: String,
    ) {
        val store = storeRepository.get(storeId)
        store.rename(newName)

        storeRepository.update(store)
    }
}
