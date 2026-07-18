package me.gimmesomepeace.buywise.application.offer.price.change

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.offer.offerRepository
import me.gimmesomepeace.buywise.domain.offer.offer
import me.gimmesomepeace.buywise.domain.offer.offerId
import me.gimmesomepeace.buywise.domain.shared.usd
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChangePriceUseCaseTest {
    @Test
    fun `should change price of existing offer`() =
        runTest {
            val offerId = offerId()
            val repository =
                offerRepository(
                    offer(offerId, 10.usd()),
                )

            ChangePriceUseCase(repository)
                .execute(offerId, 5.usd())

            assertThat(repository.get(offerId).unitPrice)
                .isEqualTo(5.usd())
        }
}
