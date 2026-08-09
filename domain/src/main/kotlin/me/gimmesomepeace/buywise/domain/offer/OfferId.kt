package me.gimmesomepeace.buywise.domain.offer

import java.util.UUID

/**
 * Уникальный идентификатор предложения.
 */
@JvmInline
value class OfferId(
    val value: UUID,
)
