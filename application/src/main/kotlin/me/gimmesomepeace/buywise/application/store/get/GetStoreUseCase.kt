package me.gimmesomepeace.buywise.application.store.get

import me.gimmesomepeace.buywise.application.store.StoreDetails
import me.gimmesomepeace.buywise.application.store.StoreQuery
import me.gimmesomepeace.buywise.domain.store.StoreException
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.user.UserId

class GetStoreUseCase(
    private val query: StoreQuery,
) {
    suspend fun execute(
        userId: UserId,
        storeId: StoreId,
    ): StoreDetails {
        val store =
            query.find(storeId)
                ?: throw StoreException.NotFound(
                    storeId,
                )
        if (store.ownerId !=
            userId
        ) {
            throw StoreException.NotFound(
                storeId,
            )
        }

        return store
    }
}
