package me.gimmesomepeace.buywise.domain.user

/**
 * Логин пользователя.
 *
 * Инварианты:
 *  - Логин не должен быть пустым.
 */
@JvmInline
value class Login(val value: String) {
    init {
        require(value.isNotBlank()) { "Login must not be blank" }
    }
}
