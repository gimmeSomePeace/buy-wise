package me.gimmesomepeace.buywise.infrastructure.persistence.offer

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.offer.OfferFilters
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.offer.Offer
import me.gimmesomepeace.buywise.domain.offer.offer
import me.gimmesomepeace.buywise.domain.offer.offerId
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.product
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.Currency
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.shared.rub
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.store
import me.gimmesomepeace.buywise.domain.store.storeId
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.user
import me.gimmesomepeace.buywise.infrastructure.PostgresSqlContainer
import me.gimmesomepeace.buywise.infrastructure.persistence.TestPersistence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import java.math.BigDecimal
import kotlin.test.Test

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(OfferQueryImpl::class, TestPersistence::class)
internal class OfferQueryImplTest : PostgresSqlContainer() {
    @Autowired
    lateinit var offerRepository: OfferJpaRepository

    @Autowired
    lateinit var persistence: TestPersistence

    @Autowired
    lateinit var query: OfferQueryImpl

    private fun createOwner(): UserId = persistence.persist(user()).id

    private fun createStoreWithOwner(ownerId: UserId): StoreId = persistence.persist(store(ownerId = ownerId)).id

    private fun createProductWithOwner(ownerId: UserId): ProductId = persistence.persist(product(ownerId = ownerId)).id

    private fun createOfferWithOwner(
        ownerId: UserId,
        unitPrice: Money = 100.usd(),
    ): Offer =
        persistence.persist(
            offer(
                storeId = createStoreWithOwner(ownerId),
                productId = createProductWithOwner(ownerId),
                unitPrice = unitPrice,
            ),
        )

    @Nested
    inner class Find {
        @Test
        fun `should return offer when exists`() =
            runTest {
                val ownerId = createOwner()
                val offer = createOfferWithOwner(ownerId)

                val actual = query.find(offer.id)

                assertThat(actual?.id)
                    .isEqualTo(offer.id)
            }

        @Test
        fun `should return null when not exists`() =
            runTest {
                val offerId = offerId()

                val actual = query.find(offerId)

                assertThat(actual).isNull()
            }
    }

    @Nested
    inner class List {
        @Nested
        inner class BasicPagination {
            @Test
            fun `should return offers for owner`() =
                runTest {
                    val ownerId = createOwner()
                    val offer1 = createOfferWithOwner(ownerId)
                    val offer2 = createOfferWithOwner(ownerId)

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters = OfferFilters(ownerId = ownerId),
                        )

                    assertThat(result.items).hasSize(2)
                    assertThat(result.items.map { it.id })
                        .containsExactlyInAnyOrder(offer1.id, offer2.id)
                    assertThat(result.cursor).isNull()
                }

            @Test
            fun `should return empty page when no offers`() =
                runTest {
                    val ownerId = createOwner()

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters = OfferFilters(ownerId = ownerId),
                        )

                    assertThat(result.items).isEmpty()
                    assertThat(result.cursor).isNull()
                }

            @Test
            fun `should not return offers of other users`() =
                runTest {
                    val ownerId = createOwner()
                    val anotherOwner = createOwner()
                    createOfferWithOwner(ownerId)
                    createOfferWithOwner(anotherOwner)

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters = OfferFilters(ownerId = ownerId),
                        )

                    assertThat(result.items).hasSize(1)
                }

            @Test
            fun `should return page with cursor when more items exist`() =
                runTest {
                    val ownerId = createOwner()
                    repeat(3) { createOfferWithOwner(ownerId) }

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 2),
                            filters = OfferFilters(ownerId = ownerId),
                        )

                    assertThat(result.items).hasSize(2)
                    assertThat(result.cursor).isNotNull()
                }

            @Test
            fun `should return page without cursor when no more items`() =
                runTest {
                    val ownerId = createOwner()
                    repeat(2) { createOfferWithOwner(ownerId) }

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 2),
                            filters = OfferFilters(ownerId = ownerId),
                        )

                    assertThat(result.items).hasSize(2)
                    assertThat(result.cursor).isNull()
                }
        }

        @Nested
        inner class CursorPagination {
            @Test
            fun `should paginate using cursor`() =
                runTest {
                    val ownerId = createOwner()
                    val offers = (1..5).map { createOfferWithOwner(ownerId) }

                    val firstPage =
                        query.list(
                            request = PageRequest(pageSize = 2),
                            filters = OfferFilters(ownerId = ownerId),
                        )

                    assertThat(firstPage.items).hasSize(2)
                    assertThat(firstPage.cursor).isNotNull()
                    assertThat(firstPage.items.map { it.id })
                        .containsExactly(offers[0].id, offers[1].id)

                    val secondPage =
                        query.list(
                            request =
                                PageRequest(
                                    pageSize = 2,
                                    cursor = firstPage.cursor,
                                ),
                            filters = OfferFilters(ownerId = ownerId),
                        )

                    assertThat(secondPage.items).hasSize(2)
                    assertThat(secondPage.cursor).isNotNull()
                    assertThat(secondPage.items.map { it.id })
                        .containsExactly(offers[2].id, offers[3].id)

                    val thirdPage =
                        query.list(
                            request =
                                PageRequest(
                                    pageSize = 2,
                                    cursor = secondPage.cursor,
                                ),
                            filters = OfferFilters(ownerId = ownerId),
                        )

                    assertThat(thirdPage.items).hasSize(1)
                    assertThat(thirdPage.cursor).isNull()
                    assertThat(thirdPage.items.map { it.id })
                        .containsExactly(offers[4].id)
                }
        }

        @Nested
        inner class OwnerFilter {
            @Test
            fun `should filter by owner id`() =
                runTest {
                    val ownerId1 = createOwner()
                    val ownerId2 = createOwner()
                    createOfferWithOwner(ownerId1)
                    createOfferWithOwner(ownerId1)
                    createOfferWithOwner(ownerId2)

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters = OfferFilters(ownerId = ownerId2),
                        )

                    assertThat(result.items).hasSize(1)
                }

            @Test
            fun `should return empty when owner has no offers`() =
                runTest {
                    val ownerId = createOwner()
                    val anotherOwner = createOwner()
                    createOfferWithOwner(anotherOwner)

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters = OfferFilters(ownerId = ownerId),
                        )

                    assertThat(result.items).isEmpty()
                }
        }

        @Nested
        inner class ProductIdsFilter {
            @Test
            fun `should filter by single product id`() =
                runTest {
                    val ownerId = createOwner()
                    val productId = createProductWithOwner(ownerId)
                    val storeId = createStoreWithOwner(ownerId)

                    val offer1 =
                        persistence.persist(
                            offer(storeId = storeId, productId = productId),
                        )
                    createOfferWithOwner(ownerId)

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    productIds = listOf(productId),
                                ),
                        )

                    assertThat(result.items).hasSize(1)
                    assertThat(result.items.first().id).isEqualTo(offer1.id)
                }

            @Test
            fun `should filter by multiple product ids`() =
                runTest {
                    val ownerId = createOwner()
                    val productId1 = createProductWithOwner(ownerId)
                    val productId2 = createProductWithOwner(ownerId)
                    val storeId = createStoreWithOwner(ownerId)

                    val offer1 =
                        persistence.persist(
                            offer(storeId = storeId, productId = productId1),
                        )
                    val offer2 =
                        persistence.persist(
                            offer(storeId = storeId, productId = productId2),
                        )
                    createOfferWithOwner(ownerId)

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    productIds = listOf(productId1, productId2),
                                ),
                        )

                    assertThat(result.items).hasSize(2)
                    assertThat(result.items.map { it.id })
                        .containsExactlyInAnyOrder(offer1.id, offer2.id)
                }

            @Test
            fun `should return empty when product ids not match`() =
                runTest {
                    val ownerId = createOwner()
                    createOfferWithOwner(ownerId)

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    productIds = listOf(productId()),
                                ),
                        )

                    assertThat(result.items).isEmpty()
                }

            @Test
            fun `should not return offers of other users even with matching product ids`() =
                runTest {
                    val ownerId = createOwner()
                    val anotherOwner = createOwner()
                    val productId = createProductWithOwner(anotherOwner)
                    val storeId = createStoreWithOwner(anotherOwner)

                    persistence.persist(
                        offer(storeId = storeId, productId = productId),
                    )

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    productIds = listOf(productId),
                                ),
                        )

                    assertThat(result.items).isEmpty()
                }
        }

        @Nested
        inner class StoreIdsFilter {
            @Test
            fun `should filter by single store id`() =
                runTest {
                    val ownerId = createOwner()
                    val storeId = createStoreWithOwner(ownerId)
                    val productId = createProductWithOwner(ownerId)

                    val offer1 =
                        persistence.persist(
                            offer(storeId = storeId, productId = productId),
                        )
                    createOfferWithOwner(ownerId)

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    storeIds = listOf(storeId),
                                ),
                        )

                    assertThat(result.items).hasSize(1)
                    assertThat(result.items.first().id).isEqualTo(offer1.id)
                }

            @Test
            fun `should filter by multiple store ids`() =
                runTest {
                    val ownerId = createOwner()
                    val storeId1 = createStoreWithOwner(ownerId)
                    val storeId2 = createStoreWithOwner(ownerId)
                    val productId = createProductWithOwner(ownerId)

                    val offer1 =
                        persistence.persist(
                            offer(storeId = storeId1, productId = productId),
                        )
                    val offer2 =
                        persistence.persist(
                            offer(storeId = storeId2, productId = productId),
                        )
                    createOfferWithOwner(ownerId)

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    storeIds = listOf(storeId1, storeId2),
                                ),
                        )

                    assertThat(result.items).hasSize(2)
                    assertThat(result.items.map { it.id })
                        .containsExactlyInAnyOrder(offer1.id, offer2.id)
                }

            @Test
            fun `should return empty when store ids not match`() =
                runTest {
                    val ownerId = createOwner()
                    createOfferWithOwner(ownerId)

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    storeIds = listOf(storeId()),
                                ),
                        )

                    assertThat(result.items).isEmpty()
                }
        }

        @Nested
        inner class PriceFilter {
            @Test
            fun `should filter by min price`() =
                runTest {
                    val ownerId = createOwner()
                    createOfferWithOwner(ownerId, unitPrice = 50.usd())
                    val expensiveOffer =
                        createOfferWithOwner(ownerId, unitPrice = 150.usd())
                    createOfferWithOwner(ownerId, unitPrice = 200.usd())

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    minPrice = BigDecimal("100"),
                                ),
                        )

                    assertThat(result.items).hasSize(2)
                    assertThat(result.items.map { it.id })
                        .contains(expensiveOffer.id)
                }

            @Test
            fun `should filter by max price`() =
                runTest {
                    val ownerId = createOwner()
                    val cheapOffer =
                        createOfferWithOwner(ownerId, unitPrice = 50.usd())
                    createOfferWithOwner(ownerId, unitPrice = 150.usd())
                    createOfferWithOwner(ownerId, unitPrice = 200.usd())

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    maxPrice = BigDecimal("100"),
                                ),
                        )

                    assertThat(result.items).hasSize(1)
                    assertThat(result.items.first().id).isEqualTo(cheapOffer.id)
                }

            @Test
            fun `should filter by price range`() =
                runTest {
                    val ownerId = createOwner()
                    createOfferWithOwner(ownerId, unitPrice = 50.usd())
                    val middleOffer =
                        createOfferWithOwner(ownerId, unitPrice = 150.usd())
                    createOfferWithOwner(ownerId, unitPrice = 250.usd())

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    minPrice = BigDecimal("100"),
                                    maxPrice = BigDecimal("200"),
                                ),
                        )

                    assertThat(result.items).hasSize(1)
                    assertThat(
                        result.items.first().id,
                    ).isEqualTo(middleOffer.id)
                }

            @Test
            fun `should include boundary values in price range`() =
                runTest {
                    val ownerId = createOwner()
                    val minOffer =
                        createOfferWithOwner(ownerId, unitPrice = 100.usd())
                    val maxOffer =
                        createOfferWithOwner(ownerId, unitPrice = 200.usd())
                    createOfferWithOwner(ownerId, unitPrice = 300.usd())

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    minPrice = BigDecimal("100"),
                                    maxPrice = BigDecimal("200"),
                                ),
                        )

                    assertThat(result.items).hasSize(2)
                    assertThat(result.items.map { it.id })
                        .containsExactlyInAnyOrder(minOffer.id, maxOffer.id)
                }

            @Test
            fun `should return empty when no prices in range`() =
                runTest {
                    val ownerId = createOwner()
                    createOfferWithOwner(ownerId, unitPrice = 50.usd())
                    createOfferWithOwner(ownerId, unitPrice = 300.usd())

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    minPrice = BigDecimal("100"),
                                    maxPrice = BigDecimal("200"),
                                ),
                        )

                    assertThat(result.items).isEmpty()
                }
        }

        @Nested
        inner class CurrencyFilter {
            @Test
            fun `should filter by currency`() =
                runTest {
                    val ownerId = createOwner()
                    val usdOffer =
                        createOfferWithOwner(ownerId, unitPrice = 50.usd())
                    createOfferWithOwner(ownerId, unitPrice = 10.rub())
                    createOfferWithOwner(ownerId, unitPrice = 20.rub())

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    currencies = listOf(Currency.USD),
                                ),
                        )

                    assertThat(result.items).hasSize(1)
                    assertThat(result.items.first().id).isEqualTo(usdOffer.id)
                }

            @Test
            fun `should return empty when currency not match`() =
                runTest {
                    val ownerId = createOwner()
                    createOfferWithOwner(ownerId, unitPrice = 10.usd())

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    currencies = listOf(Currency.RUB),
                                ),
                        )

                    assertThat(result.items).isEmpty()
                }
        }

        @Nested
        inner class CombinedFilters {
            @Test
            fun `should combine owner and price filters`() =
                runTest {
                    val ownerId1 = createOwner()
                    val ownerId2 = createOwner()
                    createOfferWithOwner(ownerId1, unitPrice = 50.usd())
                    val expensiveOffer =
                        createOfferWithOwner(ownerId1, unitPrice = 150.usd())
                    createOfferWithOwner(ownerId2, unitPrice = 200.usd())

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId1,
                                    minPrice = BigDecimal("100"),
                                    currencies = listOf(Currency.USD),
                                ),
                        )

                    assertThat(result.items).hasSize(1)
                    assertThat(
                        result.items.first().id,
                    ).isEqualTo(expensiveOffer.id)
                }

            @Test
            fun `should combine product ids and price filters`() =
                runTest {
                    val ownerId = createOwner()
                    val productId = createProductWithOwner(ownerId)

                    persistence.persist(
                        offer(
                            storeId = createStoreWithOwner(ownerId),
                            productId = productId,
                            unitPrice = 50.usd(),
                        ),
                    )
                    val expensiveOffer =
                        persistence.persist(
                            offer(
                                storeId = createStoreWithOwner(ownerId),
                                productId = productId,
                                unitPrice = 150.usd(),
                            ),
                        )

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    productIds = listOf(productId),
                                    minPrice = BigDecimal("100"),
                                    currencies = listOf(Currency.USD),
                                ),
                        )

                    assertThat(result.items).hasSize(1)
                    assertThat(
                        result.items.first().id,
                    ).isEqualTo(expensiveOffer.id)
                }

            @Test
            fun `should combine store ids and currency filters`() =
                runTest {
                    val ownerId = createOwner()
                    val storeId = createStoreWithOwner(ownerId)

                    val usdOffer =
                        persistence.persist(
                            offer(
                                storeId = storeId,
                                productId = createProductWithOwner(ownerId),
                                unitPrice = 10.usd(),
                            ),
                        )
                    persistence.persist(
                        offer(
                            storeId = storeId,
                            productId = createProductWithOwner(ownerId),
                            unitPrice = 10.rub(),
                        ),
                    )

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    storeIds = listOf(storeId),
                                    currencies = listOf(Currency.USD),
                                ),
                        )

                    assertThat(result.items).hasSize(1)
                    assertThat(result.items.first().id).isEqualTo(usdOffer.id)
                }

            @Test
            fun `should combine all filters`() =
                runTest {
                    val ownerId = createOwner()
                    val productId1 = createProductWithOwner(ownerId)
                    val productId2 = createProductWithOwner(ownerId)

                    val storeId1 = createStoreWithOwner(ownerId)
                    val storeId2 = createStoreWithOwner(ownerId)

                    val matchingOffer =
                        persistence.persist(
                            offer(
                                storeId = storeId1,
                                productId = productId1,
                                unitPrice = 150.usd(),
                            ),
                        )

                    // Не подходит по цене
                    persistence.persist(
                        offer(
                            storeId = storeId2,
                            productId = productId1,
                            unitPrice = 50.usd(),
                        ),
                    )

                    // Не подходит по валюте
                    persistence.persist(
                        offer(
                            storeId = storeId1,
                            productId = productId2,
                            unitPrice = 150.rub(),
                        ),
                    )

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    productIds = listOf(productId1, productId2),
                                    storeIds = listOf(storeId1, storeId2),
                                    minPrice = BigDecimal("100"),
                                    currencies = listOf(Currency.USD),
                                ),
                        )

                    assertThat(result.items).hasSize(1)
                    assertThat(
                        result.items.first().id,
                    ).isEqualTo(matchingOffer.id)
                }

            @Test
            fun `should apply filters with cursor pagination`() =
                runTest {
                    val ownerId = createOwner()
                    val productId = createProductWithOwner(ownerId)
                    val storeId = createStoreWithOwner(ownerId)

                    val matchingOffers =
                        (1..3).map {
                            persistence.persist(
                                offer(
                                    storeId = createStoreWithOwner(ownerId),
                                    productId = productId,
                                    unitPrice = 150.usd(),
                                ),
                            )
                        }

                    // Не подходит по цене
                    persistence.persist(
                        offer(
                            storeId = storeId,
                            productId = productId,
                            unitPrice = 50.usd(),
                        ),
                    )

                    val filters =
                        OfferFilters(
                            ownerId = ownerId,
                            productIds = listOf(productId),
                            minPrice = BigDecimal("100"),
                            currencies = listOf(Currency.USD),
                        )

                    val firstPage =
                        query.list(
                            request = PageRequest(pageSize = 2),
                            filters = filters,
                        )

                    assertThat(firstPage.items).hasSize(2)
                    assertThat(firstPage.cursor).isNotNull()

                    val secondPage =
                        query.list(
                            request =
                                PageRequest(
                                    pageSize = 2,
                                    cursor = firstPage.cursor,
                                ),
                            filters = filters,
                        )

                    assertThat(secondPage.items).hasSize(1)
                    assertThat(secondPage.cursor).isNull()

                    val allIds =
                        (firstPage.items + secondPage.items).map {
                            it.id
                        }
                    assertThat(allIds).containsExactlyInAnyOrder(
                        matchingOffers[0].id,
                        matchingOffers[1].id,
                        matchingOffers[2].id,
                    )
                }

            @Test
            fun `should return empty when combined filters exclude all`() =
                runTest {
                    val ownerId = createOwner()
                    createOfferWithOwner(ownerId, unitPrice = 50.usd())

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                OfferFilters(
                                    ownerId = ownerId,
                                    minPrice = BigDecimal("100"),
                                    currencies = listOf(Currency.USD),
                                    productIds = listOf(productId()),
                                ),
                        )

                    assertThat(result.items).isEmpty()
                }
        }
    }
}
