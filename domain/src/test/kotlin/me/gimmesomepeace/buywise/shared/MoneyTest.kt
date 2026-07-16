package me.gimmesomepeace.buywise.shared

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class MoneyTest {
    @Nested
    inner class Initialization {
        @Test
        fun `should fail to initialize with negative value`() {
            assertThatThrownBy {
                Money(BigDecimal.valueOf(-1), Currency.RUB)
            }.isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Nested
    inner class Plus {
        @Test
        fun `should add money with equal currency`() {
            val money1 = Money(BigDecimal.valueOf(1), Currency.RUB)
            val money2 = Money(BigDecimal.valueOf(1), Currency.RUB)

            assertThat(money1 + money2)
                .isEqualTo(
                    Money(
                        BigDecimal.valueOf(2),
                        Currency.RUB,
                    ),
                )
        }

        @Test
        fun `should fail when adding money with different currency`() {
            assertThatThrownBy {
                Money(
                    BigDecimal.valueOf(1),
                    Currency.RUB,
                ) +
                    Money(
                        BigDecimal.valueOf(2),
                        Currency.EUR,
                    )
            }.isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Nested
    inner class Minus {
        @Test
        fun `should subtract money with equal currency`() {
            val money1 = Money(BigDecimal.valueOf(10), Currency.RUB)
            val money2 = Money(BigDecimal.valueOf(5), Currency.RUB)

            assertThat(money1 - money2)
                .isEqualTo(Money(BigDecimal.valueOf(5), Currency.RUB))
        }

        @Test
        fun `should fail when subtracting money with different currency`() {
            val money1 = Money(BigDecimal.valueOf(1), Currency.RUB)
            val money2 = Money(BigDecimal.valueOf(1), Currency.EUR)

            assertThatThrownBy {
                money1 - money2
            }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `should fail when subtracting larger money amount from smaller one`() {
            val money1 = Money(BigDecimal.valueOf(1), Currency.RUB)
            val money2 = Money(BigDecimal.valueOf(10), Currency.RUB)

            assertThatThrownBy {
                money1 - money2
            }.isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Nested
    inner class Times {
        @Test
        fun `should multiply by multiplier`() {
            val money = Money(BigDecimal.valueOf(1), Currency.RUB)

            assertThat(money * Quantity(5))
                .isEqualTo(Money(BigDecimal.valueOf(5), Currency.RUB))
        }
    }

    @Nested
    inner class Comparable {
        @Test
        fun `should compare money with equal currency`() {
            val money1 = Money(BigDecimal.valueOf(1), Currency.RUB)
            val money2 = Money(BigDecimal.valueOf(2), Currency.RUB)

            assertThat(money1 <= money2).isTrue()
            assertThat(money1 > money2).isFalse()
        }

        @Test
        fun `should fail when compare money with different currency`() {
            val money1 = Money(BigDecimal.valueOf(1), Currency.RUB)
            val money2 = Money(BigDecimal.valueOf(2), Currency.EUR)

            assertThatThrownBy { money1 <= money2 }.isInstanceOf(IllegalArgumentException::class.java)
            assertThatThrownBy { money1 > money2 }.isInstanceOf(IllegalArgumentException::class.java)
        }
    }
}
