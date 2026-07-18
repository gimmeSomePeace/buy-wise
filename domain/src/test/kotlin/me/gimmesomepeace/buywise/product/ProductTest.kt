package me.gimmesomepeace.buywise.product

import me.gimmesomepeace.buywise.domain.product.product
import me.gimmesomepeace.buywise.domain.product.productId
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ProductTest {
    @Test
    fun `should create product`() {
        assertThatCode {
            product(
                id = productId(),
                name = "name",
            )
        }.doesNotThrowAnyException()
    }

    @Test
    fun `should reject blank name on creation`() {
        assertThatThrownBy {
            product(name = "   ")
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `should rename product`() {
        val product = product(name = "name")
        product.rename("newName")
        assertThat(product.name).isEqualTo("newName")
    }

    @Test
    fun `should reject blank name on rename`() {
        assertThatThrownBy {
            product(name = "name").rename("   ")
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
