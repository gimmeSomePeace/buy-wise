package me.gimmesomepeace.buywise.domain.shared.password

/**
 * Хешированное представление пароля пользователя.
 *
 * Исходный пароль не должен храниться напрямую. Вместо него сохраняется
 * результат работы алгоритма хеширования.
 *
 * Инварианты:
 *  - Хеш не должен быть пустым или состоять только из пробельных символов.
 */
@JvmInline
value class PasswordHash(val value: String) {
    init {
        require(value.isNotBlank()) { "Password hash must not be blank" }
    }
}
