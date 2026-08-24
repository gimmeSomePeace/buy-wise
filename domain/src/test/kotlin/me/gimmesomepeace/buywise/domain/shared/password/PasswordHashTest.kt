package me.gimmesomepeace.buywise.domain.shared.password

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PasswordHashTest {
    @ParameterizedTest
    @ValueSource(
        strings = ["", " ", "   ", "\t", "\n"],
    )
    fun `should fail when password hash is blank`(value: String) {
        assertThatThrownBy {
            PasswordHash(value)
        }.isInstanceOf(
            IllegalArgumentException::class.java,
        )
    }
}
