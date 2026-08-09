package me.gimmesomepeace.buywise.infrastructure.persistence.store

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.store.store
import me.gimmesomepeace.buywise.domain.store.storeId
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
@Import(StoreQueryImpl::class, TestPersistence::class)
internal class StoreQueryImplTest : PostgresSqlContainer() {
    @Autowired
    lateinit var jpaRepository: StoreJpaRepository

    @Autowired
    lateinit var persistence: TestPersistence

    @Autowired
    lateinit var query: StoreQueryImpl

    @Nested
    inner class Find {
        @Test
        fun `should return store when exists`() = runTest {
            val expected = persistence.persist(store())

            val actual = query.find(expected.id)
            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected)
        }

        @Test
        fun `should return null when not found`() = runTest {
            val actual = query.find(storeId())
            assertThat(actual).isNull()
        }
    }

    @Nested
    inner class List {
        @Test
        fun `should paginate stores using cursor`() = runTest {
            val stores = (1..5).map { persistence.persist(store()) }

            val firstPage = query.list(PageRequest(2))

            assertThat(firstPage.items)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(stores[0], stores[1])
            assertThat(firstPage.cursor).isNotNull()

            val lastPage = query.list(PageRequest(3, firstPage.cursor))
            assertThat(lastPage.items)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(stores[2], stores[3], stores[4])
            assertThat(lastPage.cursor).isNull()
        }

        @Test
        fun `should return empty page when no stores exist`() = runTest {
            val firstPage = query.list(PageRequest(10))

            assertThat(firstPage.items).isEmpty()
            assertThat(firstPage.cursor).isNull()
        }

        @Test
        fun `should return all stores when less than page size`() = runTest {
            val stores = (1..3).map { persistence.persist(store()) }

            val page = query.list(PageRequest(10))
            assertThat(page.items.size).isEqualTo(stores.size)
            assertThat(page.cursor).isNull()
        }
    }
}
