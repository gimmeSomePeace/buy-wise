package me.gimmesomepeace.buywise.infrastructure.persistence.store

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.store.StoreFilters
import me.gimmesomepeace.buywise.application.store.toDetails
import me.gimmesomepeace.buywise.domain.store.store
import me.gimmesomepeace.buywise.domain.store.storeId
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
@Import(StoreQueryImpl::class, TestPersistence::class)
internal class StoreQueryImplTest : PostgresSqlContainer() {
    @Autowired
    lateinit var userRepository: UserJpaRepository

    @Autowired
    lateinit var storeRepository: StoreJpaRepository

    @Autowired
    lateinit var persistence: TestPersistence

    @Autowired
    lateinit var query: StoreQueryImpl

    private fun createOwner(): UserId {
        return persistence.persist(user()).id
    }

    @Nested
    inner class Find {
        @Test
        fun `should return store when exists`() = runTest {
            val ownerId = createOwner()
            val store = persistence.persist(store(ownerId = ownerId))

            val actual = query.find(store.id)

            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(store.toDetails())
        }

        @Test
        fun `should return null when not exists`() = runTest {
            val storeId = storeId()

            val actual = query.find(storeId)

            assertThat(actual).isNull()
        }
    }

    @Nested
    inner class List {

        @Nested
        inner class BasicPagination {

            @Test
            fun `should return stores for owner`() = runTest {
                val ownerId = createOwner()
                val store1 = persistence.persist(store(ownerId = ownerId, name = "Store 1"))
                val store2 = persistence.persist(store(ownerId = ownerId, name = "Store 2"))

                val result = query.list(
                    request = PageRequest(pageSize = 20),
                    filters = StoreFilters(ownerId = ownerId),
                )

                assertThat(result.items).hasSize(2)
                assertThat(result.items.map { it.id })
                    .containsExactlyInAnyOrder(store1.id, store2.id)
                assertThat(result.cursor).isNull()
            }

            @Test
            fun `should return empty page when no stores`() = runTest {
                val ownerId = createOwner()

                val result = query.list(
                    request = PageRequest(pageSize = 20),
                    filters = StoreFilters(ownerId = ownerId),
                )

                assertThat(result.items).isEmpty()
                assertThat(result.cursor).isNull()
            }

            @Test
            fun `should return page with cursor when more items exist`() = runTest {
                val ownerId = createOwner()

                persistence.persist(store(ownerId = ownerId, name = "Store 1"))
                persistence.persist(store(ownerId = ownerId, name = "Store 2"))
                persistence.persist(store(ownerId = ownerId, name = "Store 3"))

                val result = query.list(
                    request = PageRequest(pageSize = 2),
                    filters = StoreFilters(ownerId = ownerId),
                )

                assertThat(result.items).hasSize(2)
                assertThat(result.cursor).isNotNull()
            }

            @Test
            fun `should return page without cursor when no more items`() = runTest {
                val ownerId = createOwner()
                persistence.persist(store(ownerId = ownerId, name = "Store 1"))
                persistence.persist(store(ownerId = ownerId, name = "Store 2"))

                val result = query.list(
                    request = PageRequest(pageSize = 2),
                    filters = StoreFilters(ownerId = ownerId),
                )

                assertThat(result.items).hasSize(2)
                assertThat(result.cursor).isNull()
            }
        }

        @Nested
        inner class CursorPagination {

            @Test
            fun `should paginate using cursor`() = runTest {
                val ownerId = createOwner()

                val stores = (1..5).map { i ->
                    persistence.persist(store(ownerId = ownerId, name = "Store $i"))
                }

                val firstPage = query.list(
                    request = PageRequest(pageSize = 2),
                    filters = StoreFilters(ownerId = ownerId),
                )

                assertThat(firstPage.items).hasSize(2)
                assertThat(firstPage.cursor).isNotNull()
                assertThat(firstPage.items.map { it.id })
                    .containsExactly(stores[0].id, stores[1].id)

                val secondPage = query.list(
                    request = PageRequest(pageSize = 2, cursor = firstPage.cursor),
                    filters = StoreFilters(ownerId = ownerId),
                )

                assertThat(secondPage.items).hasSize(2)
                assertThat(secondPage.cursor).isNotNull()
                assertThat(secondPage.items.map { it.id })
                    .containsExactly(stores[2].id, stores[3].id)

                val thirdPage = query.list(
                    request = PageRequest(pageSize = 2, cursor = secondPage.cursor),
                    filters = StoreFilters(ownerId = ownerId),
                )

                assertThat(thirdPage.items).hasSize(1)
                assertThat(thirdPage.cursor).isNull()
                assertThat(thirdPage.items.map { it.id })
                    .containsExactly(stores[4].id)
            }
        }

        @Nested
        inner class Filters {

            @Test
            fun `should filter by name contains`() = runTest {
                val ownerId = createOwner()
                persistence.persist(store(ownerId = ownerId, name = "Coffee Shop"))
                persistence.persist(store(ownerId = ownerId, name = "Tea House"))
                persistence.persist(store(ownerId = ownerId, name = "Coffee Bar"))

                val result = query.list(
                    request = PageRequest(pageSize = 20),
                    filters = StoreFilters(nameContains = "Coffee", ownerId = ownerId),
                )

                assertThat(result.items).hasSize(2)
                assertThat(result.items.map { it.name })
                    .containsExactlyInAnyOrder("Coffee Shop", "Coffee Bar")
            }

            @Test
            fun `should filter by user id`() = runTest {
                val userId1 = createOwner()
                val userId2 = createOwner()
                persistence.persist(store(ownerId = userId1, name = "Coffee Shop"))
                persistence.persist(store(ownerId = userId1, name = "Tea House"))
                persistence.persist(store(ownerId = userId2, name = "Coffee Bar"))

                val result = query.list(
                    request = PageRequest(pageSize = 20),
                    filters = StoreFilters(ownerId = userId2),
                )

                assertThat(result.items).hasSize(1)
                assertThat(result.items[0].name)
                    .isEqualTo("Coffee Bar")
            }

            @Test
            fun `should combine filters`() = runTest {
                val userId1 = createOwner()
                val userId2 = createOwner()
                persistence.persist(store(ownerId = userId1, name = "Coffee Shop"))
                persistence.persist(store(ownerId = userId1, name = "Tea House"))
                persistence.persist(store(ownerId = userId2, name = "Coffee Bar"))
                persistence.persist(store(ownerId = userId2, name = "Tea House"))

                val result = query.list(
                    request = PageRequest(pageSize = 20),
                    filters = StoreFilters(
                        nameContains = "Coffee",
                        ownerId = userId2,
                    ),
                )

                assertThat(result.items).hasSize(1)
                assertThat(result.items[0].name)
                    .isEqualTo("Coffee Bar")
            }

            @Test
            fun `should apply filters with cursor pagination`() = runTest {
                val ownerId = createOwner()
                val coffeeStores = listOf(
                    persistence.persist(store(ownerId = ownerId, name = "Coffee 1")),
                    persistence.persist(store(ownerId = ownerId, name = "Coffee 2")),
                    persistence.persist(store(ownerId = ownerId, name = "Coffee 3")),
                )
                persistence.persist(store(ownerId = ownerId, name = "Tea House"))

                val firstPage = query.list(
                    request = PageRequest(pageSize = 2),
                    filters = StoreFilters(nameContains = "Coffee", ownerId = ownerId),
                )

                assertThat(firstPage.items).hasSize(2)
                assertThat(firstPage.cursor).isNotNull()

                val secondPage = query.list(
                    request = PageRequest(pageSize = 2, cursor = firstPage.cursor),
                    filters = StoreFilters(nameContains = "Coffee", ownerId = ownerId),
                )

                assertThat(secondPage.items).hasSize(1)
                assertThat(secondPage.cursor).isNull()

                val allIds = (firstPage.items + secondPage.items).map { it.id }
                assertThat(allIds).containsExactlyInAnyOrder(
                    coffeeStores[0].id,
                    coffeeStores[1].id,
                    coffeeStores[2].id,
                )
            }
        }
    }
}
