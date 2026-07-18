package me.gimmesomepeace.buywise.application.store.delete

import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.StoreRepository

class DeleteStoreUseCase(
    private val storeRepository: StoreRepository,
) {
    suspend fun execute(storeId: StoreId) {
        storeRepository.delete(storeId)
    }
}
