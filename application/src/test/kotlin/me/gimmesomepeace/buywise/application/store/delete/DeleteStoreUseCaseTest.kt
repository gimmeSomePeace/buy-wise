package me.gimmesomepeace.buywise.application.store.delete

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.store.storeRepository
import me.gimmesomepeace.buywise.domain.store.StoreException
import me.gimmesomepeace.buywise.domain.store.store
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DeleteStoreUseCaseTest {
    @Test
    fun `should delete existing store`() =
        runTest {
            val store = store()
            val repository = storeRepository(store)

            val useCase =
                DeleteStoreUseCase(
                    storeRepository = repository,
                )

            useCase.execute(
                storeId = store.id,
            )

            val result =
                runCatching {
                    repository.delete(store.id)
                }

            assertThat(result.exceptionOrNull())
                .isInstanceOf(StoreException.NotFound::class.java)
        }
}
