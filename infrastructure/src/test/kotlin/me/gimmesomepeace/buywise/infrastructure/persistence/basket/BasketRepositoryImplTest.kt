package me.gimmesomepeace.buywise.infrastructure.persistence.basket

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.product.product
import me.gimmesomepeace.buywise.domain.shared.qty
import me.gimmesomepeace.buywise.infrastructure.PostgresSqlContainer
import me.gimmesomepeace.buywise.infrastructure.persistence.TestPersistence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import kotlin.test.Test

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(BasketRepositoryImpl::class, TestPersistence::class)
internal class BasketRepositoryImplTest : PostgresSqlContainer() {

    @Autowired
    lateinit var jpaRepository: BasketJpaRepository

    @Autowired
    lateinit var persistence: TestPersistence

    @Autowired
    lateinit var repository: BasketRepositoryImpl

    @Nested
    inner class Find {

        @Test
        fun `should return null when basket is empty`() = runTest {
            val actual = repository.find()

            assertThat(actual).isNull()
        }

        @Test
        fun `should return basket with all items`() = runTest {
            val product1 = persistence.persist(product())
            val product2 = persistence.persist(product())

            jpaRepository.save(
                BasketEntity(
                    productId = product1.id.value,
                    quantity = 2,
                )
            )

            jpaRepository.save(
                BasketEntity(
                    productId = product2.id.value,
                    quantity = 5,
                )
            )

            val actual = repository.find()

            assertThat(actual).isNotNull

            assertThat(actual!!.quantityOf(product1.id))
                .isEqualTo(2.qty())

            assertThat(actual.quantityOf(product2.id))
                .isEqualTo(5.qty())
        }
    }

    @Nested
    inner class Save {

        @Test
        fun `should persist basket items`() = runTest {
            val product1 = persistence.persist(product())
            val product2 = persistence.persist(product())

            val basket = basket {
                add(product1.id, 2.qty())
                add(product2.id, 5.qty())
            }

            repository.save(basket)

            val actual = repository.find()

            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(basket)
        }

        @Test
        fun `should replace existing basket`() = runTest {
            val product1 = persistence.persist(product())
            val product2 = persistence.persist(product())
            val product3 = persistence.persist(product())

            repository.save(
                basket {
                    add(product1.id, 2.qty())
                    add(product2.id, 1.qty())
                }
            )

            val expected = basket {
                add(product3.id, 5.qty())
            }

            repository.save(expected)

            val actual = repository.find()

            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected)

            assertThat(jpaRepository.findAll())
                .hasSize(1)

            assertThat(jpaRepository.findAll().first().productId)
                .isEqualTo(product3.id.value)
        }
    }
}
