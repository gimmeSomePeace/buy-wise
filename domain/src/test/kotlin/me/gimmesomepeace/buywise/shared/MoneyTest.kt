package me.gimmesomepeace.buywise.shared

import me.gimmesomepeace.buywise.domain.shared.qty
import me.gimmesomepeace.buywise.domain.shared.rub
import me.gimmesomepeace.buywise.domain.shared.usd
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MoneyTest {
    @Nested
    inner class Initialization {
        @Test
        fun `should fail to initialize with negative value`() {
            assertThatThrownBy {
                (-1).usd()
            }.isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Nested
    inner class Plus {
        @Test
        fun `should add money with equal currency`() {
            assertThat(1.usd() + 1.usd())
                .isEqualTo(2.usd())
        }

        @Test
        fun `should fail when adding money with different currency`() {
            assertThatThrownBy {
                1.usd() + 1.rub()
            }.isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Nested
    inner class Minus {
        @Test
        fun `should subtract money with equal currency`() {
            assertThat(10.usd() - 5.usd())
                .isEqualTo(5.usd())
        }

        @Test
        fun `should fail when subtracting money with different currency`() {
            assertThatThrownBy {
                5.usd() - 1.rub()
            }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `should fail when subtracting larger money amount from smaller one`() {
            assertThatThrownBy {
                1.usd() - 10.usd()
            }.isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Nested
    inner class Times {
        @Test
        fun `should multiply by multiplier`() {
            assertThat(1.usd() * 5.qty())
                .isEqualTo(5.usd())
        }
    }

    @Nested
    inner class Comparable {
        @Test
        fun `should compare money with equal currency`() {
            val money1 = 1.usd()
            val money2 = 2.usd()

            assertThat(money1 <= money2).isTrue()
            assertThat(money1 > money2).isFalse()
        }

        @Test
        fun `should fail when compare money with different currency`() {
            val money1 = 1.rub()
            val money2 = 2.usd()

            assertThatThrownBy { money1 <= money2 }.isInstanceOf(IllegalArgumentException::class.java)
            assertThatThrownBy { money1 > money2 }.isInstanceOf(IllegalArgumentException::class.java)
        }
    }
}
