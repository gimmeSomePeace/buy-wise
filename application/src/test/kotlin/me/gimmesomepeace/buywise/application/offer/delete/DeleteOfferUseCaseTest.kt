package me.gimmesomepeace.buywise.application.offer.delete

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.domain.offer.OfferException
import me.gimmesomepeace.buywise.domain.offer.OfferRepository
import me.gimmesomepeace.buywise.domain.offer.offer
import me.gimmesomepeace.buywise.domain.offer.offerId
import me.gimmesomepeace.buywise.domain.store.storeId
import me.gimmesomepeace.buywise.domain.user.userId
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class DeleteOfferUseCaseTest {
    private val offerRepository = mockk<OfferRepository>()
    private val offerQuery = mockk<OfferQuery>()
    private val useCase =
        DeleteOfferUseCase(
            repository = offerRepository,
            query = offerQuery,
        )

    @Test
    fun `should delete user's offer`() =
        runTest {
            val ownerId = userId()
            val offerId = offerId()
            val storeId = storeId()
            val offer = offer(id = offerId, storeId = storeId)

            coEvery { offerRepository.get(offerId) } returns offer
            coEvery { offerRepository.delete(offerId) } just runs
            coEvery {
                offerQuery.existsByIdAndOwner(offerId, ownerId)
            } returns true

            useCase.execute(ownerId, offerId)

            coVerify(exactly = 1) { offerRepository.delete(offerId) }
        }

    @Test
    fun `should throw NotFound for another user's offer`() =
        runTest {
            val ownerId = userId()
            val offerId = offerId()
            val storeId = storeId()
            val offer = offer(id = offerId, storeId = storeId)

            coEvery { offerRepository.get(offerId) } returns offer
            coEvery {
                offerQuery.existsByIdAndOwner(offerId, ownerId)
            } returns false

            assertFailsWith<OfferException.NotFound> {
                useCase.execute(ownerId, offerId)
            }

            coVerify(exactly = 0) { offerRepository.delete(any()) }
        }

    @Test
    fun `should throw NotFound when offer not found`() =
        runTest {
            val offerId = offerId()
            coEvery {
                offerRepository.get(offerId)
            } throws OfferException.NotFound(offerId)
            coEvery {
                offerQuery.existsByIdAndOwner(offerId, any())
            } returns false

            assertFailsWith<OfferException.NotFound> {
                useCase.execute(userId(), offerId)
            }

            coVerify(exactly = 0) { offerRepository.delete(any()) }
        }
}
