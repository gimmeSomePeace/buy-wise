package me.gimmesomepeace.buywise.application.offer.get

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.application.offer.offerDetails
import me.gimmesomepeace.buywise.domain.offer.OfferException
import me.gimmesomepeace.buywise.domain.offer.offerId
import me.gimmesomepeace.buywise.domain.user.userId
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlin.test.assertFailsWith

class GetOfferUseCaseTest {
    private val query = mockk<OfferQuery>()
    private val useCase = GetOfferUseCase(query)

    @Test
    fun `should return offer when it exists and belongs to user`() =
        runTest {
            val ownerId = userId()
            val offerId = offerId()
            val expected = offerDetails(id = offerId, ownerId = ownerId)

            coEvery { query.find(offerId) } returns expected

            val actual = useCase.execute(ownerId, offerId)

            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected)
        }

    @Test
    fun `should throw NotFound when offer not found`() =
        runTest {
            val ownerId = userId()
            val offerId = offerId()

            coEvery { query.find(offerId) } returns null

            assertFailsWith<OfferException.NotFound> {
                useCase.execute(ownerId, offerId)
            }
        }

    @Test
    fun `should throw NotFound when offer belongs to another user`() =
        runTest {
            val ownerId = userId()
            val anotherOwnerId = userId()
            val offerId = offerId()

            coEvery { query.find(offerId) } returns
                offerDetails(id = offerId, ownerId = anotherOwnerId)

            assertFailsWith<OfferException.NotFound> {
                useCase.execute(ownerId, offerId)
            }
        }
}
