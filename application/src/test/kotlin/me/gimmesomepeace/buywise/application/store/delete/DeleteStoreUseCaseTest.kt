package me.gimmesomepeace.buywise.application.store.delete

import io.mockk.*
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.store.StoreException
import me.gimmesomepeace.buywise.domain.store.StoreRepository
import me.gimmesomepeace.buywise.domain.store.store
import me.gimmesomepeace.buywise.domain.store.storeId
import me.gimmesomepeace.buywise.domain.user.userId
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class DeleteStoreUseCaseTest {
    private val repository = mockk<StoreRepository>()
    private val useCase = DeleteStoreUseCase(repository)

    @Test
    fun `should delete store when it exists and belongs to user`() = runTest {
        val ownerId = userId()
        val storeId = storeId()
        val store = store(id = storeId, ownerId = ownerId)

        coEvery { repository.get(storeId) } returns store
        coEvery { repository.delete(storeId) } just runs

        useCase.execute(ownerId, storeId)

        coVerify(exactly = 1) { repository.delete(storeId) }
    }

    @Test
    fun `should throw NotFound when store belongs to another user`() = runTest {
        val ownerId = userId()
        val storeId = storeId()
        val anotherUserId = userId()

        coEvery { repository.get(storeId) } returns store(id = storeId, ownerId = anotherUserId)

        assertFailsWith<StoreException.NotFound> {
            useCase.execute(ownerId, storeId)
        }

        coVerify(exactly = 0) { repository.delete(any()) }
    }

    @Test
    fun `should throw NotFound when store not found`() = runTest {
        val storeId = storeId()
        coEvery { repository.get(storeId) } throws StoreException.NotFound(storeId)

        assertFailsWith<StoreException.NotFound> {
            useCase.execute(userId(), storeId)
        }

        coVerify(exactly = 0) { repository.delete(any()) }
    }
}
