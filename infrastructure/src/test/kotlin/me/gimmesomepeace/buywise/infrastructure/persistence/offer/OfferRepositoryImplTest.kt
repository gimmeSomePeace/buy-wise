package me.gimmesomepeace.buywise.infrastructure.persistence.offer

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.offer.OfferException
import me.gimmesomepeace.buywise.domain.offer.offer
import me.gimmesomepeace.buywise.domain.offer.offerId
import me.gimmesomepeace.buywise.domain.product.product
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.domain.store.store
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
@Import(OfferRepositoryImpl::class, TestPersistence::class)
internal class OfferRepositoryImplTest : PostgresSqlContainer() {
    @Autowired
    lateinit var jpaRepository: OfferJpaRepository

    @Autowired
    lateinit var persistence: TestPersistence

    @Autowired
    lateinit var repository: OfferRepositoryImpl

    @Nested
    inner class Get {
        @Test
        fun `should return offer when exists`() =
            runTest {
                val store = persistence.persist(store())
                val product = persistence.persist(product())

                val expected =
                    persistence.persist(
                        offer(storeId = store.id, productId = product.id),
                    )

                val actual = repository.get(expected.id)
                assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected)
            }

        @Test
        fun `should throw exception when not found`() =
            runTest {
                val expectedId = offerId()
                val ex =
                    assertFailsWith<OfferException.NotFound> {
                        repository.get(expectedId)
                    }

                assertThat(ex.offerId).isEqualTo(expectedId)
            }
    }

    @Nested
    inner class Add {
        @Test
        fun `should save new offer`() =
            runTest {
                val store = persistence.persist(store())
                val product = persistence.persist(product())

                val expected = offer(storeId = store.id, productId = product.id)
                repository.add(expected)

                val actual = repository.get(expected.id)
                assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected)
            }

        @Test
        fun `should fail when id is busy`() =
            runTest {
                val store = persistence.persist(store())
                val product = persistence.persist(product())

                val offer = offer(storeId = store.id, productId = product.id)
                repository.add(offer)

                val ex =
                    assertFailsWith<OfferException.AlreadyExists> {
                        repository.add(offer)
                    }
                assertThat(ex.offerId).isEqualTo(offer.id)
            }
    }

    @Nested
    inner class Update {
        @Test
        fun `should update offer`() =
            runTest {
                val store = persistence.persist(store())
                val product = persistence.persist(product())

                val expected =
                    offer(
                        storeId = store.id,
                        productId = product.id,
                        unitPrice = 5.usd(),
                    )
                repository.add(expected)

                expected.changePrice(10.usd())
                repository.update(expected)

                val actual = repository.get(expected.id)
                assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected)
            }

        @Test
        fun `should fail when not found`() =
            runTest {
                val offer = offer()
                val ex =
                    assertFailsWith<OfferException.NotFound> {
                        repository.update(offer)
                    }
                assertThat(ex.offerId).isEqualTo(offer.id)
            }
    }

    @Nested
    inner class Delete {
        @Test
        fun `should delete offer`() =
            runTest {
                val store = persistence.persist(store())
                val product = persistence.persist(product())
                val offer =
                    persistence.persist(
                        offer(storeId = store.id, productId = product.id),
                    )

                repository.delete(offer.id)
                val ex =
                    assertFailsWith<OfferException.NotFound> {
                        repository.get(offer.id)
                    }
                assertThat(ex.offerId).isEqualTo(offer.id)
            }

        @Test
        fun `should fail when not found`() =
            runTest {
                val offerId = offerId()
                val ex =
                    assertFailsWith<OfferException.NotFound> {
                        repository.delete(offerId)
                    }
                assertThat(ex.offerId).isEqualTo(offerId)
            }
    }

    @Nested
    inner class AvailableOffers {
        @Test
        fun `should return all available offers`() =
            runTest {
                val store = persistence.persist(store())
                val product1 = persistence.persist(product())
                val product2 = persistence.persist(product())
                val product3 = persistence.persist(product())

                with(repository) {
                    add(offer(storeId = store.id, productId = product1.id))
                    add(offer(storeId = store.id, productId = product2.id))
                    add(offer(storeId = store.id, productId = product3.id))
                }

                val result = repository.availableOffers()
                assertThat(result.stores().size).isEqualTo(1)
            }
    }
}
