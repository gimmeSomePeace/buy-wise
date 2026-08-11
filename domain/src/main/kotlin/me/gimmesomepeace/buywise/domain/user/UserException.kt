package me.gimmesomepeace.buywise.domain.user

import me.gimmesomepeace.buywise.domain.shared.DomainException

sealed class UserException(
    message: String,
) : DomainException(message) {
    class NotFound(
        val userId: UserId,
    ) : UserException("User with id $userId not found")

    class AlreadyExists(
        val userId: UserId,
    ) : UserException("User with id $userId already exists")
}
