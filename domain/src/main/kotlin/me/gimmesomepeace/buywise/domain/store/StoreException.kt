package me.gimmesomepeace.buywise.domain.store

import me.gimmesomepeace.buywise.domain.shared.DomainException

sealed class StoreException(
    message: String,
) : DomainException(message) {
    class NotFound(
        storeId: StoreId,
    ) : StoreException("Store with id $storeId not found")

    class AlreadyExists(
        storeId: StoreId,
    ) : StoreException("Store with id $storeId already exists")
}
