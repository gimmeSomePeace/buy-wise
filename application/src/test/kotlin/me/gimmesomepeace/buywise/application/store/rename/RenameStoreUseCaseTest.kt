package me.gimmesomepeace.buywise.application.store.rename

import io.mockk.*
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.store.StoreException
import me.gimmesomepeace.buywise.domain.store.StoreRepository
import me.gimmesomepeace.buywise.domain.store.store
import me.gimmesomepeace.buywise.domain.store.storeId
import me.gimmesomepeace.buywise.domain.user.userId
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class RenameStoreUseCaseTest {
    private val repository = mockk<StoreRepository>()
    private val useCase = RenameStoreUseCase(repository)

    @Test
    fun `should rename store when it exists and belongs to user`() = runTest {
        val ownerId = userId()
        val storeId = storeId()
        val store = store(id = storeId, ownerId = ownerId, name = "Old Name")
        val newName = "New Name"

        coEvery { repository.get(storeId) } returns store
        coEvery { repository.update(any()) } just runs

        useCase.execute(ownerId, storeId, newName)

        coVerify(exactly = 1) {
            repository.update(match { it.name == newName })
        }
    }

    @Test
    fun `should throw NotFound when store belongs to another user`() = runTest {
        val ownerId = userId()
        val storeId = storeId()
        val anotherUserId = userId()

        coEvery { repository.get(storeId) } returns store(id = storeId, ownerId = anotherUserId)

        assertFailsWith<StoreException.NotFound> {
            useCase.execute(ownerId, storeId, "New Name")
        }

        coVerify(exactly = 0) { repository.update(any()) }
    }

    @Test
    fun `should throw NotFound when store not found`() = runTest {
        val storeId = storeId()
        coEvery { repository.get(storeId) } throws StoreException.NotFound(storeId)

        assertFailsWith<StoreException.NotFound> {
            useCase.execute(userId(), storeId, "New Name")
        }

        coVerify(exactly = 0) { repository.update(any()) }
    }
}
