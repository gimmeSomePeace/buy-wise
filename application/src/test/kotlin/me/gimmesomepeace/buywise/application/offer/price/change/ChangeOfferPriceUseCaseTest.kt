package me.gimmesomepeace.buywise.application.offer.price.change

import io.mockk.*
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.domain.offer.OfferException
import me.gimmesomepeace.buywise.domain.offer.OfferRepository
import me.gimmesomepeace.buywise.domain.offer.offer
import me.gimmesomepeace.buywise.domain.offer.offerId
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.domain.store.storeId
import me.gimmesomepeace.buywise.domain.user.userId
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class ChangeOfferPriceUseCaseTest {
    private val offerRepository = mockk<OfferRepository>()
    private val offerQuery = mockk<OfferQuery>()
    private val useCase = ChangeOfferPriceUseCase(
        repository = offerRepository,
        query = offerQuery,
    )

    @Test
    fun `should change price when offer exists and belongs to user`() = runTest {
        val ownerId = userId()
        val offerId = offerId()
        val storeId = storeId()
        val offer = offer(id = offerId, storeId = storeId, unitPrice = 100.usd())
        val newPrice = 200.usd()

        coEvery { offerRepository.get(offerId) } returns offer
        coEvery { offerQuery.existsByIdAndOwner(offerId, ownerId) } returns true
        coEvery { offerRepository.update(any()) } just runs

        useCase.execute(ownerId, offerId, newPrice)

        coVerify(exactly = 1) {
            offerRepository.update(match { it.unitPrice == newPrice })
        }
    }

    @Test
    fun `should throw NotFound when offer belongs to another user`() = runTest {
        val ownerId = userId()
        val offerId = offerId()
        val offer = offer(id = offerId)

        coEvery { offerRepository.get(offerId) } returns offer
        coEvery { offerQuery.existsByIdAndOwner(offerId, ownerId) } returns false

        assertFailsWith<OfferException.NotFound> {
            useCase.execute(ownerId, offerId, 200.usd())
        }

        coVerify(exactly = 0) { offerRepository.update(any()) }
    }

    @Test
    fun `should throw NotFound when offer not found`() = runTest {
        val offerId = offerId()
        coEvery { offerRepository.get(offerId) } throws OfferException.NotFound(offerId)
        coEvery { offerQuery.existsByIdAndOwner(offerId, any()) } returns false

        assertFailsWith<OfferException.NotFound> {
            useCase.execute(userId(), offerId, 200.usd())
        }

        coVerify(exactly = 0) { offerRepository.update(any()) }
    }
}
