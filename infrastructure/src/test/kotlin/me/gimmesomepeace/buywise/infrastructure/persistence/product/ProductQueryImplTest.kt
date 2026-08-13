package me.gimmesomepeace.buywise.infrastructure.persistence.product

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.product.product
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.user.user
import me.gimmesomepeace.buywise.domain.user.userId
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
@Import(ProductQueryImpl::class, TestPersistence::class)
internal class ProductQueryImplTest : PostgresSqlContainer() {
    @Autowired
    lateinit var jpaRepository: ProductJpaRepository

    @Autowired
    lateinit var persistence: TestPersistence

    @Autowired
    lateinit var query: ProductQueryImpl

    @Nested
    inner class Find {
        @Test
        fun `should return product when exists`() =
            runTest {
                val expected = persistence.persist(product())

                val actual = query.find(expected.id)
                assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected)
            }

        @Test
        fun `should return null when not found`() =
            runTest {
                val actual = query.find(productId())
                assertThat(actual).isNull()
            }
    }

    @Nested
    inner class List {
        @Test
        fun `should paginate products using cursor`() =
            runTest {
                val ownerId = persistence.persist(user()).id
                val products = (1..5).map { persistence.persist(product(ownerId = ownerId)) }

                val firstPage = query.list(ownerId = ownerId, PageRequest(2))

                assertThat(firstPage.items)
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(products[0], products[1])
                assertThat(firstPage.cursor).isNotNull()

                val lastPage = query.list(ownerId = ownerId, PageRequest(3, firstPage.cursor))
                assertThat(lastPage.items)
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(products[2], products[3], products[4])
                assertThat(lastPage.cursor).isNull()
            }

        @Test
        fun `should return empty page when no products exist`() =
            runTest {
                val firstPage = query.list(ownerId = userId(), PageRequest(10))

                assertThat(firstPage.items).isEmpty()
                assertThat(firstPage.cursor).isNull()
            }

        @Test
        fun `should return all products when less than page size`() =
            runTest {
                val ownerId = persistence.persist(user()).id
                val products = (1..3).map { persistence.persist(product(ownerId = ownerId)) }

                val page = query.list(ownerId = ownerId, PageRequest(10))
                assertThat(page.items.size).isEqualTo(products.size)
                assertThat(page.cursor).isNull()
            }
    }
}
