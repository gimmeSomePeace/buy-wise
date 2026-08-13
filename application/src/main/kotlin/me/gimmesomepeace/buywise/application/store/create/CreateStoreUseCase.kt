package me.gimmesomepeace.buywise.application.store.create

import me.gimmesomepeace.buywise.application.shared.IdGenerator
import me.gimmesomepeace.buywise.domain.store.Store
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.StoreRepository
import me.gimmesomepeace.buywise.domain.user.UserId

class CreateStoreUseCase(
    private val storeRepository: StoreRepository,
    private val idGenerator: IdGenerator<StoreId>,
) {
    suspend fun execute(
        ownerId: UserId,
        name: String,
    ): Store {
        val id = idGenerator.generate()
        val store = Store(id, ownerId, name)

        storeRepository.add(store)
        return store
    }
}
