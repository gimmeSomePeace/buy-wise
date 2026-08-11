package me.gimmesomepeace.buywise.domain.user

import me.gimmesomepeace.buywise.domain.shared.password.PasswordHash

/**
 * Пользователь системы.
 *
 * Каждый пользователь имеет уникальный [login] и хеш пароля [passwordHash].
 * Исходный пароль пользователя не хранится.
 *
 * [role] определяет набор административных возможностей пользователя.
 *
 * Инварианты:
 *  - Логин должен быть уникален среди пользователей.
 */
class User(
    val id: UserId,
    val passwordHash: PasswordHash,
    val login: Login,
    val role: UserRole,
)
