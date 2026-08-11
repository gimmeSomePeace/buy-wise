package me.gimmesomepeace.buywise.domain.shared.password

/**
 * Сервис для безопасного хеширования и проверки паролей пользователей.
 *
 * Сервис преобразует исходный пароль в [PasswordHash], который может быть
 * безопасно сохранён и использован для последующей проверки пароля.
 */
interface PasswordHasher {
    /**
     * Создаёт хеш для указанного пароля.
     *
     * @param password исходный пароль пользователя
     * @return хеш указанного пароля
     */
    fun hash(password: String): PasswordHash

    /**
     * Проверяет, соответствует ли исходный пароль сохранённому хешу.
     *
     * @param password проверяемый исходный пароль
     * @param hash сохранённый хеш пароля
     * @return `true`, если пароль соответствует хешу, иначе `false`
     */
    fun matches(
        password: String,
        hash: PasswordHash,
    ): Boolean
}
