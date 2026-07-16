package me.gimmesomepeace.buywise.product

import java.util.UUID

/**
 * Уникальный идентификатор продукта.
 *
 * @property value Значение идентификатора.
 */
@JvmInline
value class ProductId(
    val value: UUID,
)
