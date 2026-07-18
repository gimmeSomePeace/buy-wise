package me.gimmesomepeace.buywise.domain.shared

import java.math.BigDecimal

/**
 * Денежная сумма в некоторой валюте.
 *
 * Инварианты:
 *   - Значение денежной суммы не может быть отрицательным.
 *
 * @property value Значение денежной суммы.
 * @property currency Валюта, в которой выражается денежная сумма.
 */
data class Money(
    val value: BigDecimal,
    val currency: Currency,
) : Comparable<Money> {
    init {
        require(value >= BigDecimal.ZERO) {
            "Money amount cannot be negative"
        }
    }

    /**
     * Возвращает новую денежную сумму, равную сумме текущей и переданной.
     *
     * @param money Денежная сумма, которую нужно прибавить к текущей.
     * @throws IllegalArgumentException если денежные суммы указаны в разных валютах.
     */
    operator fun plus(money: Money): Money {
        requireSameCurrency(money)
        return Money(value + money.value, currency)
    }

    /**
     * Возвращает новую денежную сумму, равную разнице текущей и переданной.
     *
     * @param money Денежная сумма, которую нужно вычесть из текущей.
     * @throws IllegalArgumentException если полученная разница отрицательная.
     */
    operator fun minus(money: Money): Money {
        requireSameCurrency(money)

        val newValue = value - money.value
        require(newValue >= BigDecimal.ZERO) {
            "After substraction money amount negative"
        }
        return Money(value - money.value, currency)
    }

    /**
     * Возвращает стоимость указанного количества единиц товара.
     *
     * @param quantity Количество единиц товара.
     * @return Стоимость указанного количества товара.
     */
    operator fun times(quantity: Quantity) = Money(value * quantity.value.toBigDecimal(), currency)

    /**
     * Сравнивает две денежные суммы.
     *
     * @param other Денежная сумма для сравнения.
     * @throws IllegalArgumentException если суммы выражены в разных валютах.
     */
    override fun compareTo(other: Money): Int {
        requireSameCurrency(other)
        return value.compareTo(other.value)
    }

    private fun requireSameCurrency(other: Money) {
        require(currency == other.currency) {
            "Currency mismatch: $currency vs ${other.currency}"
        }
    }

    companion object {
        /**
         * Возвращает денежную сумму, равную нулю в указанной валюте.
         *
         * @param currency Денежная валюта, в которой необходимо выразить денежную сумму, равную нулю.
         */
        fun zero(currency: Currency): Money = Money(BigDecimal.ZERO, currency)
    }
}
