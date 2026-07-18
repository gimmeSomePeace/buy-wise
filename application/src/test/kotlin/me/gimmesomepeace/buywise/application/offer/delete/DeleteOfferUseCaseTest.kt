package me.gimmesomepeace.buywise.application.offer.delete

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.offer.offerRepository
import me.gimmesomepeace.buywise.domain.offer.OfferException
import me.gimmesomepeace.buywise.domain.offer.offer
import me.gimmesomepeace.buywise.domain.offer.offerId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DeleteOfferUseCaseTest {
    @Test
    fun `should delete existing offer`() =
        runTest {
            val offerId = offerId()
            val repository =
                offerRepository(
                    offer(offerId),
                )

            DeleteOfferUseCase(repository)
                .execute(offerId = offerId)

            val result =
                runCatching {
                    repository.delete(offerId)
                }

            assertThat(result.exceptionOrNull())
                .isInstanceOf(OfferException.NotFound::class.java)
        }
}
