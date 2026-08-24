package me.gimmesomepeace.buywise.domain.user

import java.util.UUID

/**
 * Уникальный идентификатор пользователя.
 */
@JvmInline
value class UserId(
    val value: UUID,
)
