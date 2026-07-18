package me.gimmesomepeace.buywise.application.store.rename

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.store.storeRepository
import me.gimmesomepeace.buywise.domain.store.store
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RenameStoreUseCaseTest {
    @Test
    fun `should rename existing store`() =
        runTest {
            val store = store(name = "Test Store")
            val repository = storeRepository(store)

            val useCase =
                RenameStoreUseCase(
                    storeRepository = repository,
                )

            useCase.execute(
                storeId = store.id,
                newName = "Test store's new name",
            )

            val actual = repository.get(store.id)

            assertThat(actual.name)
                .isEqualTo("Test store's new name")
        }
}
