package me.gimmesomepeace.buywise.application.store.delete

import me.gimmesomepeace.buywise.domain.store.StoreException
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.StoreRepository
import me.gimmesomepeace.buywise.domain.user.UserId

class DeleteStoreUseCase(
    private val storeRepository: StoreRepository,
) {
    suspend fun execute(
        userId: UserId,
        storeId: StoreId,
    ) {
        val store = storeRepository.get(storeId)
        if (store.ownerId != userId)
            throw StoreException.NotFound(storeId)
        storeRepository.delete(storeId)
    }
}
