package me.gimmesomepeace.buywise.domain.user

interface UserRepository {
    /**
     * Возвращает пользователя по его идентификатору.
     *
     * @throws UserException.NotFound если пользователя с указанным идентификатором не существует.
     */
    suspend fun get(userId: UserId): User

    /**
     * Сохраняет переданного пользователя, воспринимая его как нового.
     *
     * @throws UserException.AlreadyExists если пользователь с таким
     * идентификатором уже существует.
     * @throws UserException.LoginBusy если пользователь с таким
     * логином уже существует
     */
    suspend fun add(user: User)

    /**
     * Сохраняет изменения существующего пользователя.
     *
     * @throws UserException.NotFound если пользователя не существует.
     * @throws UserException.LoginBusy если пользователь с новым
     * логином уже существует
     */
    suspend fun update(user: User)

    /**
     * Удаляет пользователя по указанному идентификатору.
     *
     * @throws UserException.NotFound если пользователя не существует.
     */
    suspend fun delete(userId: UserId)
}
