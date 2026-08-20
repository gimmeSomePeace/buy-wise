package me.gimmesomepeace.buywise.application.store.rename

import me.gimmesomepeace.buywise.domain.store.StoreException
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.StoreRepository
import me.gimmesomepeace.buywise.domain.user.UserId

class RenameStoreUseCase(
    private val storeRepository: StoreRepository,
) {
    suspend fun execute(
        userId: UserId,
        storeId: StoreId,
        newName: String,
    ) {
        val store = storeRepository.get(storeId)
        if (store.ownerId != userId)
            throw StoreException.NotFound(storeId)

        store.rename(newName)
        storeRepository.update(store)
    }
}
