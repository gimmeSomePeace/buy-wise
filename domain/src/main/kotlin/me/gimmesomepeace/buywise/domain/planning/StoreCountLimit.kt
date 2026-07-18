package me.gimmesomepeace.buywise.domain.planning

/**
 * Ограничение на максимальное количество магазинов.
 */
sealed interface StoreCountLimit {
    /**
     * Ограничение отсутствует.
     */
    data object Unlimited : StoreCountLimit

    /**
     * Максимально допустимое количество магазинов.
     *
     * Инварианты:
     *  - значение должно быть положительным.
     *
     * @property value Значение верхней границы.
     */
    @JvmInline
    value class Limited(
        val value: Int,
    ) : StoreCountLimit {
        init {
            require(value > 0) { "Limit value must be greater than zero." }
        }
    }
}
