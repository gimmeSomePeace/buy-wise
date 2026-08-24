package me.gimmesomepeace.buywise.application.store.list

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.application.store.StoreListItem
import me.gimmesomepeace.buywise.application.store.StoreQuery
import me.gimmesomepeace.buywise.application.store.storeListItem
import me.gimmesomepeace.buywise.domain.user.userId
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ListStoresUseCaseTest {
    private val query = mockk<StoreQuery>()
    private val useCase = ListStoresUseCase(query)

    @Test
    fun `should return stores for owner`() =
        runTest {
            val ownerId = userId()
            val page =
                Page(
                    items =
                        listOf(
                            storeListItem(
                                ownerId = ownerId,
                            ),
                        ),
                    cursor = null,
                )

            coEvery {
                query.list(
                    any(),
                    any(),
                )
            } returns
                page

            val result =
                useCase.execute(
                    ownerId,
                    PageRequest(pageSize = 20),
                )

            assertThat(result.items).hasSize(1)
            assertThat(
                result.items.first().ownerId,
            ).isEqualTo(ownerId)
        }

    @Test
    fun `should return empty page when no stores`() =
        runTest {
            val ownerId = userId()
            val emptyPage =
                Page<StoreListItem>(
                    items = emptyList(),
                    cursor = null,
                )

            coEvery {
                query.list(
                    any(),
                    any(),
                )
            } returns
                emptyPage

            val result =
                useCase.execute(
                    ownerId,
                    PageRequest(pageSize = 20),
                )

            assertThat(result.items).isEmpty()
        }

    @Test
    fun `should pass ownerId to query filter`() =
        runTest {
            val ownerId = userId()
            val page =
                Page<StoreListItem>(
                    items = emptyList(),
                    cursor = null,
                )

            coEvery {
                query.list(
                    any(),
                    any(),
                )
            } returns
                page

            useCase.execute(
                ownerId,
                PageRequest(pageSize = 20),
            )

            coVerify(exactly = 1) {
                query.list(
                    any(),
                    match { filter ->
                        filter.ownerId ==
                            ownerId
                    },
                )
            }
        }
}
