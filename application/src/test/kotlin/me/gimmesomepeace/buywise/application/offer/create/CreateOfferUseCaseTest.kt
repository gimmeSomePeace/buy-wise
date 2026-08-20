package me.gimmesomepeace.buywise.application.offer.create

import io.mockk.*
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.offer.OfferQuery
import me.gimmesomepeace.buywise.application.offer.offerDetails
import me.gimmesomepeace.buywise.domain.offer.OfferRepository
import me.gimmesomepeace.buywise.domain.offer.offerId
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.domain.store.storeId
import org.junit.jupiter.api.Test

class CreateOfferUseCaseTest {
    private val offerRepository = mockk<OfferRepository>()
    private val offerQuery = mockk<OfferQuery>()
    private val offerId = offerId()

    private val useCase = CreateOfferUseCase(
        idGenerator = { offerId },
        offerRepository = offerRepository,
        offerQuery = offerQuery
    )

    @Test
    fun `should create a new offer with correct parameters`() = runTest {
        val productId = productId()
        val storeId = storeId()
        val unitPrice = 1.usd()

        coEvery { offerRepository.add(any()) } just runs
        coEvery { offerQuery.find(offerId) } returns offerDetails(id = offerId)

        useCase.execute(
            productId = productId,
            storeId = storeId,
            unitPrice = unitPrice,
        )

        coVerify(exactly = 1) {
            offerRepository.add(
                match { offer -> offer.id == offerId }
            )
        }
    }
}
