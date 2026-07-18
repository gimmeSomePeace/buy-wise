package me.gimmesomepeace.buywise.application.offer.create

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.offer.offerRepository
import me.gimmesomepeace.buywise.domain.offer.offerId
import me.gimmesomepeace.buywise.domain.shared.usd
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreateOfferUseCaseTest {
    @Test
    fun `should create a new offer`() =
        runTest {
            val repository = offerRepository()

            val offerId = offerId()
            CreateOfferUseCase(
                idGenerator = { offerId },
                offerRepository = repository,
            ).execute(
                productId = offerId.productId,
                storeId = offerId.storeId,
                unitPrice = 1.usd(),
            )

            val offer = repository.get(offerId)
            assertThat(offer.id).isEqualTo(offerId)
        }
}
