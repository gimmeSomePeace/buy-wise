package me.gimmesomepeace.buywise.infrastructure.persistence.product

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.product.ProductFilters
import me.gimmesomepeace.buywise.application.product.toDetails
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.product.product
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.domain.user.user
import me.gimmesomepeace.buywise.infrastructure.PostgresSqlContainer
import me.gimmesomepeace.buywise.infrastructure.persistence.TestPersistence
import me.gimmesomepeace.buywise.infrastructure.persistence.user.UserJpaRepository
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
    lateinit var userRepository: UserJpaRepository

    @Autowired
    lateinit var productRepository: ProductJpaRepository

    @Autowired
    lateinit var persistence: TestPersistence

    @Autowired
    lateinit var query: ProductQueryImpl

    private fun createOwner(): UserId = persistence.persist(user()).id

    @Nested
    inner class Find {
        @Test
        fun `should return product when exists`() =
            runTest {
                val ownerId = createOwner()
                val product = persistence.persist(product(ownerId = ownerId))

                val actual = query.find(product.id)

                assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(product.toDetails())
            }

        @Test
        fun `should return null when not exists`() =
            runTest {
                val productId = productId()

                val actual = query.find(productId)

                assertThat(actual).isNull()
            }
    }

    @Nested
    inner class List {
        @Nested
        inner class BasicPagination {
            @Test
            fun `should return products for owner`() =
                runTest {
                    val ownerId = createOwner()
                    val product1 =
                        persistence.persist(
                            product(ownerId = ownerId, name = "Product 1"),
                        )
                    val product2 =
                        persistence.persist(
                            product(ownerId = ownerId, name = "Product 2"),
                        )

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters = ProductFilters(ownerId = ownerId),
                        )

                    assertThat(result.items).hasSize(2)
                    assertThat(result.items.map { it.id })
                        .containsExactlyInAnyOrder(product1.id, product2.id)
                    assertThat(result.cursor).isNull()
                }

            @Test
            fun `should return empty page when no products`() =
                runTest {
                    val ownerId = createOwner()

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters = ProductFilters(ownerId = ownerId),
                        )

                    assertThat(result.items).isEmpty()
                    assertThat(result.cursor).isNull()
                }

            @Test
            fun `should return page with cursor when more items exist`() =
                runTest {
                    val ownerId = createOwner()

                    persistence.persist(
                        product(ownerId = ownerId, name = "Product 1"),
                    )
                    persistence.persist(
                        product(ownerId = ownerId, name = "Product 2"),
                    )
                    persistence.persist(
                        product(ownerId = ownerId, name = "Product 3"),
                    )

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 2),
                            filters = ProductFilters(ownerId = ownerId),
                        )

                    assertThat(result.items).hasSize(2)
                    assertThat(result.cursor).isNotNull()
                }

            @Test
            fun `should return page without cursor when no more items`() =
                runTest {
                    val ownerId = createOwner()
                    persistence.persist(
                        product(ownerId = ownerId, name = "Product 1"),
                    )
                    persistence.persist(
                        product(ownerId = ownerId, name = "Product 2"),
                    )

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 2),
                            filters = ProductFilters(ownerId = ownerId),
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

                    val products =
                        (1..5).map { i ->
                            persistence.persist(
                                product(ownerId = ownerId, name = "Product $i"),
                            )
                        }

                    val firstPage =
                        query.list(
                            request = PageRequest(pageSize = 2),
                            filters = ProductFilters(ownerId = ownerId),
                        )

                    assertThat(firstPage.items).hasSize(2)
                    assertThat(firstPage.cursor).isNotNull()
                    assertThat(firstPage.items.map { it.id })
                        .containsExactly(products[0].id, products[1].id)

                    val secondPage =
                        query.list(
                            request =
                                PageRequest(
                                    pageSize = 2,
                                    cursor = firstPage.cursor,
                                ),
                            filters = ProductFilters(ownerId = ownerId),
                        )

                    assertThat(secondPage.items).hasSize(2)
                    assertThat(secondPage.cursor).isNotNull()
                    assertThat(secondPage.items.map { it.id })
                        .containsExactly(products[2].id, products[3].id)

                    val thirdPage =
                        query.list(
                            request =
                                PageRequest(
                                    pageSize = 2,
                                    cursor = secondPage.cursor,
                                ),
                            filters = ProductFilters(ownerId = ownerId),
                        )

                    assertThat(thirdPage.items).hasSize(1)
                    assertThat(thirdPage.cursor).isNull()
                    assertThat(thirdPage.items.map { it.id })
                        .containsExactly(products[4].id)
                }
        }

        @Nested
        inner class Filters {
            @Test
            fun `should filter by name contains`() =
                runTest {
                    val ownerId = createOwner()
                    persistence.persist(
                        product(ownerId = ownerId, name = "Coffee Beans"),
                    )
                    persistence.persist(
                        product(ownerId = ownerId, name = "Tea Leaves"),
                    )
                    persistence.persist(
                        product(ownerId = ownerId, name = "Coffee Grounds"),
                    )

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                ProductFilters(
                                    nameContains = "Coffee",
                                    ownerId = ownerId,
                                ),
                        )

                    assertThat(result.items).hasSize(2)
                    assertThat(result.items.map { it.name })
                        .containsExactlyInAnyOrder(
                            "Coffee Beans",
                            "Coffee Grounds",
                        )
                }

            @Test
            fun `should filter by user id`() =
                runTest {
                    val userId1 = createOwner()
                    val userId2 = createOwner()
                    persistence.persist(
                        product(ownerId = userId1, name = "Coffee Beans"),
                    )
                    persistence.persist(
                        product(ownerId = userId1, name = "Tea Leaves"),
                    )
                    persistence.persist(
                        product(ownerId = userId2, name = "Coffee Grounds"),
                    )

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters = ProductFilters(ownerId = userId2),
                        )

                    assertThat(result.items).hasSize(1)
                    assertThat(result.items[0].name)
                        .isEqualTo("Coffee Grounds")
                }

            @Test
            fun `should combine filters`() =
                runTest {
                    val userId1 = createOwner()
                    val userId2 = createOwner()
                    persistence.persist(
                        product(ownerId = userId1, name = "Coffee Beans"),
                    )
                    persistence.persist(
                        product(ownerId = userId1, name = "Tea Leaves"),
                    )
                    persistence.persist(
                        product(ownerId = userId2, name = "Coffee Grounds"),
                    )
                    persistence.persist(
                        product(ownerId = userId2, name = "Tea Leaves"),
                    )

                    val result =
                        query.list(
                            request = PageRequest(pageSize = 20),
                            filters =
                                ProductFilters(
                                    nameContains = "Coffee",
                                    ownerId = userId2,
                                ),
                        )

                    assertThat(result.items).hasSize(1)
                    assertThat(result.items[0].name)
                        .isEqualTo("Coffee Grounds")
                }

            @Test
            fun `should apply filters with cursor pagination`() =
                runTest {
                    val ownerId = createOwner()
                    val coffeeProducts =
                        listOf(
                            persistence.persist(
                                product(ownerId = ownerId, name = "Coffee 1"),
                            ),
                            persistence.persist(
                                product(ownerId = ownerId, name = "Coffee 2"),
                            ),
                            persistence.persist(
                                product(ownerId = ownerId, name = "Coffee 3"),
                            ),
                        )
                    persistence.persist(
                        product(ownerId = ownerId, name = "Tea Leaves"),
                    )

                    val firstPage =
                        query.list(
                            request = PageRequest(pageSize = 2),
                            filters =
                                ProductFilters(
                                    nameContains = "Coffee",
                                    ownerId = ownerId,
                                ),
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
                            filters =
                                ProductFilters(
                                    nameContains = "Coffee",
                                    ownerId = ownerId,
                                ),
                        )

                    assertThat(secondPage.items).hasSize(1)
                    assertThat(secondPage.cursor).isNull()

                    val allIds =
                        (firstPage.items + secondPage.items).map {
                            it.id
                        }
                    assertThat(allIds).containsExactlyInAnyOrder(
                        coffeeProducts[0].id,
                        coffeeProducts[1].id,
                        coffeeProducts[2].id,
                    )
                }
        }
    }
}
