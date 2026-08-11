package me.gimmesomepeace.buywise.domain.user

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LoginTest {
    @ParameterizedTest
    @ValueSource(strings = ["", " ", "   ", "\t", "\n"])
    fun `should fail when login is blank`(value: String) {
        assertThatThrownBy {
            Login(value)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
