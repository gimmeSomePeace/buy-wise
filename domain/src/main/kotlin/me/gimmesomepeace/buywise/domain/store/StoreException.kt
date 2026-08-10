package me.gimmesomepeace.buywise.domain.store

import me.gimmesomepeace.buywise.domain.shared.DomainException

sealed class StoreException(
    message: String,
) : DomainException(message) {
    class NotFound(
        val storeId: StoreId,
    ) : StoreException("Store with id $storeId not found")

    class AlreadyExists(
        val storeId: StoreId,
    ) : StoreException("Store with id $storeId already exists")
}
