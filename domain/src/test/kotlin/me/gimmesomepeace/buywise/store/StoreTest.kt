package me.gimmesomepeace.buywise.store

import me.gimmesomepeace.buywise.domain.store.store
import me.gimmesomepeace.buywise.domain.store.storeId
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class StoreTest {
    @Test
    fun `should create store`() {
        assertThatCode {
            store(
                id = storeId(),
                name = "name",
            )
        }.doesNotThrowAnyException()
    }

    @Test
    fun `should reject blank name on creation`() {
        assertThatThrownBy {
            store(name = "   ")
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `should reject blank name on rename`() {
        val store = store()
        assertThatThrownBy {
            store.rename("    ")
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `should rename store`() {
        val store = store(name = "name")
        store.rename("new name")
        assertThat(store.name).isEqualTo("new name")
    }
}
