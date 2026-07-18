package me.gimmesomepeace.buywise.domain.shared

/**
 * Базовый тип всех исключений доменного слоя.
 *
 * Сигнализирует о нарушении бизнес-инвариантов и контрактов предметной области.
 */
open class DomainException(
    message: String,
) : RuntimeException(message)
