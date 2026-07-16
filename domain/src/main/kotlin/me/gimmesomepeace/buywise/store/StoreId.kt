package me.gimmesomepeace.buywise.store

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
