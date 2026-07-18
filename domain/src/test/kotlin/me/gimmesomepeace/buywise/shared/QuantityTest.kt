package me.gimmesomepeace.buywise.shared

import me.gimmesomepeace.buywise.domain.shared.Quantity
import me.gimmesomepeace.buywise.domain.shared.qty
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class QuantityTest {
    @Nested
    inner class Initialization {
        @Test
        fun `should throw exception when creating new quantity with negative value`() {
            assertThatThrownBy {
                (-1).qty()
            }.isInstanceOf(IllegalArgumentException::class.java)
        }

        @Test
        fun `should create new quantity with zero value`() {
            assertThatCode {
                0.qty()
            }.doesNotThrowAnyException()
        }
    }

    @Nested
    inner class Plus {
        @Test
        fun `should add quantities`() {
            assertThat(Quantity.ONE + Quantity.ONE)
                .isEqualTo(2.qty())
        }
    }

    @Nested
    inner class Minus {
        @Test
        fun `should throw exception when value is bigger`() {
            assertThatThrownBy {
                Quantity.ONE - 999.qty()
            }.isInstanceOf(IllegalArgumentException::class.java)
        }
    }

    @Nested
    inner class Comparable {
        @Test
        fun `should compare quantities`() {
            assertThat(Quantity.ONE.compareTo(Quantity.ONE)).isZero()
            assertThat(Quantity.ONE.compareTo(Quantity.ZERO)).isPositive()
            assertThat(Quantity.ZERO.compareTo(Quantity.ONE)).isNegative()
        }
    }

    @Nested
    inner class Times {
        @Test
        fun `should multiply quantity by multiplier`() {
            assertThat(Quantity.ONE * 2).isEqualTo(2.qty())
        }

        @Test
        fun `should throw when multiplied by negative multiplier`() {
            assertThatThrownBy {
                Quantity.ONE * -1
            }.isInstanceOf(IllegalArgumentException::class.java)
        }
    }
}
