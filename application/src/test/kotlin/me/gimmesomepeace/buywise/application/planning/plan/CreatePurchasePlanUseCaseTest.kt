package me.gimmesomepeace.buywise.application.planning.plan

import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.application.basket.basketRepository
import me.gimmesomepeace.buywise.application.offer.offerRepository
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.offer.offer
import me.gimmesomepeace.buywise.domain.offer.offerId
import me.gimmesomepeace.buywise.domain.planning.BasketPurchasePlanner
import me.gimmesomepeace.buywise.domain.planning.StoreCountLimit
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.qty
import me.gimmesomepeace.buywise.domain.shared.usd
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreatePurchasePlanUseCaseTest {
    @Test
    fun `should create purchase plan`() =
        runTest {
            val productId = productId()
            val offerId = offerId(productId = productId)

            val basket =
                basket {
                    add(productId, 2.qty())
                }
            val basketRepository = basketRepository(basket)

            val offerRepository =
                offerRepository(
                    offer(
                        offerId = offerId,
                        unitPrice = 100.usd(),
                    ),
                )

            val useCase =
                CreatePurchasePlanUseCase(
                    basketRepository = basketRepository,
                    offerRepository = offerRepository,
                )

            val expected =
                BasketPurchasePlanner.plan(
                    basket = basket,
                    offers = offerRepository.availableOffers(),
                    maxStores = StoreCountLimit.Unlimited,
                )

            val result = useCase.execute(StoreCountLimit.Unlimited)

            assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected)
        }
}
