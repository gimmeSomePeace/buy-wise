package me.gimmesomepeace.buywise.infrastructure.persistence.store

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.store.StoreException
import me.gimmesomepeace.buywise.domain.store.store
import me.gimmesomepeace.buywise.domain.store.storeId
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
@Import(StoreRepositoryImpl::class, TestPersistence::class)
internal class StoreRepositoryImplTest : PostgresSqlContainer() {
    @Autowired
    lateinit var jpaRepository: StoreJpaRepository

    @Autowired
    lateinit var persistence: TestPersistence

    @Autowired
    lateinit var repository: StoreRepositoryImpl

    @Nested
    inner class Get {
        @Test
        fun `should return store when exists`() =
            runTest {
                val expected = persistence.persist(store())

                val actual = repository.get(expected.id)
                assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected)
            }

        @Test
        fun `should throw exception when not found`() =
            runTest {
                val expectedId = storeId()
                val ex =
                    assertFailsWith<StoreException.NotFound> {
                        repository.get(expectedId)
                    }

                assertThat(ex.storeId).isEqualTo(expectedId)
            }
    }

    @Nested
    inner class Add {
        @Test
        fun `should save new store`() =
            runTest {
                val expected = store()
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
                val store = persistence.persist(store(ownerId = ownerId))

                val ex =
                    assertFailsWith<StoreException.AlreadyExists> {
                        repository.add(store)
                    }
                assertThat(ex.storeId).isEqualTo(store.id)
            }
    }

    @Nested
    inner class Update {
        @Test
        fun `should update store`() =
            runTest {
                val ownerId = persistence.persist(user()).id
                val store = persistence.persist(
                    store(name = "OLD NAME", ownerId = ownerId)
                )

                store.rename("NEW NAME")
                repository.update(store)

                val actual = repository.get(store.id)
                assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(store)
            }

        @Test
        fun `should fail when not found`() =
            runTest {
                val store = store()
                val ex =
                    assertFailsWith<StoreException.NotFound> {
                        repository.update(store)
                    }
                assertThat(ex.storeId).isEqualTo(store.id)
            }
    }

    @Nested
    inner class Delete {
        @Test
        fun `should delete store`() =
            runTest {
                val ownerId = persistence.persist(user()).id
                val store = persistence.persist(store(ownerId = ownerId))

                repository.delete(store.id)
                val ex =
                    assertFailsWith<StoreException.NotFound> {
                        repository.get(store.id)
                    }
                assertThat(ex.storeId).isEqualTo(store.id)
            }

        @Test
        fun `should fail when not found`() =
            runTest {
                val storeId = storeId()
                val ex =
                    assertFailsWith<StoreException.NotFound> {
                        repository.delete(storeId)
                    }
                assertThat(ex.storeId).isEqualTo(storeId)
            }
    }
}
