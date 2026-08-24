package me.gimmesomepeace.buywise.infrastructure.persistence.user

import me.gimmesomepeace.buywise.domain.user.User
import me.gimmesomepeace.buywise.domain.user.UserException
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.UserRepository
import org.springframework.data.repository.findByIdOrNull

class UserRepositoryImpl(
    private val repository: UserJpaRepository,
) : UserRepository {
    override suspend fun get(userId: UserId): User =
        repository
            .findByIdOrNull(
                userId.value,
            )?.toDomain()
            ?: throw UserException.NotFound(
                userId,
            )

    override suspend fun add(user: User) {
        if (repository.existsById(
                user.id.value,
            )
        ) {
            throw UserException.AlreadyExists(
                user.id,
            )
        }
        repository.save(user.toEntity())
    }

    override suspend fun update(user: User) {
        if (!repository.existsById(
                user.id.value,
            )
        ) {
            throw UserException.NotFound(user.id)
        }
        repository.save(user.toEntity())
    }

    override suspend fun delete(userId: UserId) {
        if (!repository.existsById(
                userId.value,
            )
        ) {
            throw UserException.NotFound(userId)
        }
        repository.deleteById(userId.value)
    }
}
