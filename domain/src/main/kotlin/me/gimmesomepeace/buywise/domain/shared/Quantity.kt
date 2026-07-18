package me.gimmesomepeace.buywise.domain.shared

/**
 * Количество экземпляров некоторого объекта.
 *
 * Инварианты:
 *  - Количество не может быть отрицательным.
 *
 *  @property value Значение количества.
 */
@JvmInline
value class Quantity(
    val value: Int,
) {
    init {
        require(value >= 0) { "Quantity must be greater than or equal to 0" }
    }

    /**
     * Является ли количество положительным числом (может быть равно нулю).
     */
    fun isPositive() = value > 0

    /**
     *  Равно ли количество нулю.
     */
    fun isZero() = value == 0

    /**
     * Возвращает количество, равное сумме текущего и переданного количества.
     */
    operator fun plus(quantity: Quantity): Quantity = Quantity(value + quantity.value)

    /**
     * Возвращает разницу двух количеств.
     *
     * @throws IllegalArgumentException если результат вычитания будет отрицательным.
     */
    operator fun minus(quantity: Quantity): Quantity {
        val newValue = value - quantity.value
        require(newValue >= 0) {
            "Cannot subtract ${quantity.value} from $value: got negative quantity"
        }
        return Quantity(newValue)
    }

    /**
     * Умножает количество на некоторый коэффициент.
     *
     * @throws IllegalArgumentException если переданный коэффициент отрицательный.
     */
    operator fun times(t: Int): Quantity {
        require(t >= 0) {
            "Multiplier must be greater than or equal to 0"
        }
        return Quantity(value * t)
    }

    /**
     * Сравнивает 2 количества.
     */
    operator fun compareTo(other: Quantity): Int = value.compareTo(other.value)

    companion object {
        /**
         * Количество, равное нулю.
         */
        val ZERO = Quantity(0)

        /**
         * Количество, равное единице.
         */
        val ONE = Quantity(1)
    }
}
