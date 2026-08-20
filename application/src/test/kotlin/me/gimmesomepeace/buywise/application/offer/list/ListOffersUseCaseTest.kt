package me.gimmesomepeace.buywise.application.offer.list

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.offer.OfferListItem
import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.application.offer.offerListItem
import me.gimmesomepeace.buywise.application.shared.Page
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.user.userId
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ListOffersUseCaseTest {
    private val query = mockk<OfferQuery>()
    private val useCase = ListOffersUseCase(query)

    @Test
    fun `should return offers for owner`() = runTest {
        val ownerId = userId()
        val page = Page(
            items = listOf(offerListItem(ownerId = ownerId)),
            cursor = null,
        )

        coEvery { query.list(any(), any()) } returns page

        val result = useCase.execute(ownerId, PageRequest(pageSize = 20))

        assertThat(result.items).hasSize(1)
        assertThat(result.items.first().ownerId).isEqualTo(ownerId)
    }

    @Test
    fun `should return empty page when no offers`() = runTest {
        val ownerId = userId()
        val emptyPage = Page<OfferListItem>(items = emptyList(), cursor = null)

        coEvery { query.list(any(), any()) } returns emptyPage

        val result = useCase.execute(ownerId, PageRequest(pageSize = 20))

        assertThat(result.items).isEmpty()
    }

    // TODO: переделать
//    @Test
//    fun `should pass ownerId to query filter`() = runTest {
//        val ownerId = userId()
//        val page = Page<OfferListItem>(items = emptyList(), cursor = null)
//
//        coEvery { query.list(any(), any()) } returns page
//
//        useCase.execute(ownerId, PageRequest(pageSize = 20))
//
//        coVerify(exactly = 1) {
//            query.list(
//                match { filter -> filter.ownerId == ownerId },
//                any(),
//            )
//        }
//    }
}
