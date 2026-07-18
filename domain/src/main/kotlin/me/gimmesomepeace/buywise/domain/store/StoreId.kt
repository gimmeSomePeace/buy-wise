package me.gimmesomepeace.buywise.domain.store

import java.util.UUID

/**
 * Уникальный идентификатор магазина.
 *
 * @property value Значение идентификатора.
 */
@JvmInline
value class StoreId(
    val value: UUID,
)
