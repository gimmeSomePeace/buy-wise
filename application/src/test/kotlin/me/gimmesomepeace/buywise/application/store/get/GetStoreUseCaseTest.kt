package me.gimmesomepeace.buywise.application.store.get

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.store.StoreQuery
import me.gimmesomepeace.buywise.application.store.storeDetails
import me.gimmesomepeace.buywise.domain.store.StoreException
import me.gimmesomepeace.buywise.domain.store.storeId
import me.gimmesomepeace.buywise.domain.user.userId
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlin.test.assertFailsWith

class GetStoreUseCaseTest {
    private val query = mockk<StoreQuery>()
    private val useCase = GetStoreUseCase(query)

    @Test
    fun `should return store when it exists and belongs to user`() =
        runTest {
            val ownerId = userId()
            val storeId = storeId()
            val expected = storeDetails(id = storeId, ownerId = ownerId)

            coEvery { query.find(storeId) } returns expected

            val actual = useCase.execute(ownerId, storeId)

            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected)
        }

    @Test
    fun `should throw NotFound when store not found`() =
        runTest {
            val ownerId = userId()
            val storeId = storeId()

            coEvery { query.find(storeId) } returns null

            assertFailsWith<StoreException.NotFound> {
                useCase.execute(ownerId, storeId)
            }
        }

    @Test
    fun `should throw NotFound when store belongs to another user`() =
        runTest {
            val ownerId = userId()
            val anotherOwnerId = userId()
            val storeId = storeId()

            coEvery { query.find(storeId) } returns
                storeDetails(id = storeId, ownerId = anotherOwnerId)

            assertFailsWith<StoreException.NotFound> {
                useCase.execute(ownerId, storeId)
            }
        }
}
