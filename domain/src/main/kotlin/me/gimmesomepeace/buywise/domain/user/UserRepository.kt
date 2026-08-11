package me.gimmesomepeace.buywise.domain.user

import me.gimmesomepeace.buywise.domain.offer.Offer
import me.gimmesomepeace.buywise.domain.offer.OfferId

interface UserRepository {
    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @throws UserException.NotFound если пользователя с указанным идентификатором не существует.
     */
    suspend fun get(
        userId: UserId,
    ): User

    /**
     * Сохраняет переданного пользователя, воспринимая его как нового.
     *
     * @throws UserException.AlreadyExists если пользователь с таким
     * идентификатором уже существует.
     */
    suspend fun add(
        offer: Offer,
    )

    /**
     * Сохраняет изменения существующего пользователя.
     *
     * @throws UserException.NotFound если пользователя не существует.
     */
    suspend fun update(
        offer: Offer,
    )

    /**
     * Удаляет пользователя по указанному идентификатору.
     *
     * @throws UserException.NotFound если пользователя не существует.
     */
    suspend fun delete(
        offerId: OfferId,
    )
}
