package me.gimmesomepeace.buywise.infrastructure.persistence.offer

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.offer.offer
import me.gimmesomepeace.buywise.domain.offer.offerId
import me.gimmesomepeace.buywise.domain.product.product
import me.gimmesomepeace.buywise.domain.store.store
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
@Import(OfferQueryImpl::class, TestPersistence::class)
internal class OfferQueryImplTest : PostgresSqlContainer() {
    @Autowired
    lateinit var jpaRepository: OfferJpaRepository

    @Autowired
    lateinit var persistence: TestPersistence

    @Autowired
    lateinit var query: OfferQueryImpl

    @Nested
    inner class Find {
        @Test
        fun `should return offer when exists`() = runTest {
            val store = persistence.persist(store())
            val product = persistence.persist(product())

            val expected = persistence.persist(
                offer(storeId = store.id, productId = product.id)
            )

            val actual = query.find(expected.id)
            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected)
        }

        @Test
        fun `should return null when not found`() = runTest {
            val actual = query.find(offerId())
            assertThat(actual).isNull()
        }
    }

    @Nested
    inner class List {
        @Test
        fun `should paginate offers using cursor`() = runTest {
            val offers = (1..5).map {
                val store = persistence.persist(store())
                val product = persistence.persist(product())
                persistence.persist(offer(storeId = store.id, productId = product.id))
            }

            val firstPage = query.list(PageRequest(2))

            assertThat(firstPage.items)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(offers[0], offers[1])
            assertThat(firstPage.cursor).isNotNull()

            val lastPage = query.list(PageRequest(3, firstPage.cursor))
            assertThat(lastPage.items)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(offers[2], offers[3], offers[4])
            assertThat(lastPage.cursor).isNull()
        }

        @Test
        fun `should return empty page when no offers exist`() = runTest {
            val firstPage = query.list(PageRequest(10))

            assertThat(firstPage.items).isEmpty()
            assertThat(firstPage.cursor).isNull()
        }

        @Test
        fun `should return all offers when less than page size`() = runTest {
            val offers = (1..3).map {
                val store = persistence.persist(store())
                val product = persistence.persist(product())
                persistence.persist(offer(storeId = store.id, productId = product.id))
            }

            val page = query.list(PageRequest(10))
            assertThat(page.items.size).isEqualTo(offers.size)
            assertThat(page.cursor).isNull()
        }
    }
}
