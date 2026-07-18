package me.gimmesomepeace.buywise.application.store.create

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.store.storeRepository
import me.gimmesomepeace.buywise.domain.store.storeId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreateStoreUseCaseTest {
    @Test
    fun `should create new store`() =
        runTest {
            val storeId = storeId()
            val repository = storeRepository()

            val useCase =
                CreateStoreUseCase(
                    storeRepository = repository,
                    idGenerator = { storeId },
                )

            useCase.execute(
                name = "Test store",
            )

            val actual = repository.get(storeId)

            assertThat(actual.name)
                .isEqualTo("Test store")
        }
}
