package me.gimmesomepeace.buywise.application.planning.plan

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.gimmesomepeace.buywise.domain.basket.BasketRepository
import me.gimmesomepeace.buywise.domain.basket.basket
import me.gimmesomepeace.buywise.domain.offer.OfferRepository
import me.gimmesomepeace.buywise.domain.planning.BasketPurchasePlanner
import me.gimmesomepeace.buywise.domain.planning.StoreCountLimit
import me.gimmesomepeace.buywise.domain.planning.availableOffer
import me.gimmesomepeace.buywise.domain.planning.offer.AvailableOfferCatalog
import me.gimmesomepeace.buywise.domain.product.productId
import me.gimmesomepeace.buywise.domain.shared.qty
import me.gimmesomepeace.buywise.domain.shared.usd
import me.gimmesomepeace.buywise.domain.store.storeId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreatePurchasePlanUseCaseTest {
    private val basketRepository = mockk<BasketRepository>()
    private val offerRepository = mockk<OfferRepository>()
    private val useCase =
        CreatePurchasePlanUseCase(
            basketRepository = basketRepository,
            offerRepository = offerRepository,
        )

    @Test
    fun `should create purchase plan`() =
        runTest {
            val productId = productId()
            val storeId = storeId()

            val offer =
                availableOffer(
                    storeId = storeId,
                    productId = productId,
                    unitPrice = 100.usd(),
                )

            val basket =
                basket {
                    add(productId, 2.qty())
                }

            val offerCatalog = AvailableOfferCatalog(listOf(offer))
            coEvery { basketRepository.find() } returns basket
            coEvery { offerRepository.availableOffers() } returns offerCatalog

            val result = useCase.execute()

            val expected =
                BasketPurchasePlanner.plan(
                    basket = basket,
                    offers = offerCatalog,
                    maxStores = StoreCountLimit.Unlimited,
                )

            assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected)
        }
}
