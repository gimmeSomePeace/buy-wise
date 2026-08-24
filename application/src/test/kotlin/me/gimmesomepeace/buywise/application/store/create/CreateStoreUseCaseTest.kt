package me.gimmesomepeace.buywise.application.store.create

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.shared.IdGenerator
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.StoreRepository
import me.gimmesomepeace.buywise.domain.store.storeId
import me.gimmesomepeace.buywise.domain.user.userId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreateStoreUseCaseTest {
    private val idGenerator =
        mockk<IdGenerator<StoreId>>()
    private val repository =
        mockk<StoreRepository>()
    private val useCase =
        CreateStoreUseCase(
            idGenerator,
            repository,
        )

    @Test
    fun `should create store with correct owner`() =
        runTest {
            val ownerId = userId()
            val name = "My Store"

            coEvery {
                idGenerator.generate()
            } returns
                storeId()
            coEvery { repository.add(any()) } just
                runs

            val result =
                useCase.execute(
                    ownerId,
                    name,
                )

            assertThat(
                result.ownerId,
            ).isEqualTo(ownerId)
            assertThat(
                result.name,
            ).isEqualTo(name)

            coVerify(exactly = 1) {
                repository.add(
                    match {
                        it.ownerId ==
                            ownerId
                    },
                )
            }
        }
}
