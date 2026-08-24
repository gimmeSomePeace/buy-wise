package me.gimmesomepeace.buywise.infrastructure.persistence.product

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.product.ProductException
import me.gimmesomepeace.buywise.domain.product.product
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.user.user
import me.gimmesomepeace.buywise.infrastructure.PostgresSqlContainer
import me.gimmesomepeace.buywise.infrastructure.persistence.TestPersistence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import kotlin.test.assertFailsWith

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ProductRepositoryImpl::class, TestPersistence::class)
internal class ProductRepositoryImplTest : PostgresSqlContainer() {
    @Autowired
    lateinit var jpaRepository: ProductJpaRepository

    @Autowired
    lateinit var persistence: TestPersistence

    @Autowired
    lateinit var repository: ProductRepositoryImpl

    @Nested
    inner class Get {
        @Test
        fun `should return product when exists`() =
            runTest {
                val expected = persistence.persist(product())

                val actual = repository.get(expected.id)
                assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected)
            }

        @Test
        fun `should throw exception when not found`() =
            runTest {
                val expectedId = productId()
                val ex =
                    assertFailsWith<ProductException.NotFound> {
                        repository.get(expectedId)
                    }

                assertThat(ex.productId).isEqualTo(expectedId)
            }
    }

    @Nested
    inner class Add {
        @Test
        fun `should save new product`() =
            runTest {
                val expected = product()
                repository.add(expected)

                val actual = repository.get(expected.id)
                assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected)
            }

        @Test
        fun `should fail when id is busy`() =
            runTest {
                val ownerId = persistence.persist(user()).id
                val product = persistence.persist(product(ownerId = ownerId))

                val ex =
                    assertFailsWith<ProductException.AlreadyExists> {
                        repository.add(product)
                    }
                assertThat(ex.productId).isEqualTo(product.id)
            }
    }

    @Nested
    inner class Update {
        @Test
        fun `should update product`() =
            runTest {
                val ownerId = persistence.persist(user()).id
                val product =
                    persistence.persist(
                        product(
                            ownerId = ownerId,
                            name = "OLD NAME",
                        ),
                    )

                product.rename("NEW NAME")
                repository.update(product)

                val actual = repository.get(product.id)
                assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(product)
            }

        @Test
        fun `should fail when not found`() =
            runTest {
                val product = product()
                val ex =
                    assertFailsWith<ProductException.NotFound> {
                        repository.update(product)
                    }
                assertThat(ex.productId).isEqualTo(product.id)
            }
    }

    @Nested
    inner class Delete {
        @Test
        fun `should delete product`() =
            runTest {
                val ownerId = persistence.persist(user()).id
                val product = persistence.persist(product(ownerId = ownerId))

                repository.delete(product.id)
                val ex =
                    assertFailsWith<ProductException.NotFound> {
                        repository.get(product.id)
                    }
                assertThat(ex.productId).isEqualTo(product.id)
            }

        @Test
        fun `should fail when not found`() =
            runTest {
                val productId = productId()
                val ex =
                    assertFailsWith<ProductException.NotFound> {
                        repository.delete(productId)
                    }
                assertThat(ex.productId).isEqualTo(productId)
            }
    }
}
